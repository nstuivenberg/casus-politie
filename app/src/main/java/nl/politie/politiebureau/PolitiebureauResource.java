package nl.politie.politiebureau;

import lombok.extern.slf4j.Slf4j;
import nl.politie.politiebureau.dto.PolitiebureausDTO;
import nl.politie.politiebureau.exception.ErrorResponse;
import nl.politie.politiebureau.exception.InvalidInputException;
import nl.politie.politiebureau.service.PolitiebureauService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Slf4j
@Path("politiebureaus")
public class PolitiebureauResource {
    @Inject
    private PolitiebureauService politiebureauService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Haal politiebureaus op binnen een bepaald bereik op basis van latitude en longitude. Dit endpoint mnaakt van de aarde een perfecte sphere die met rechthoekige vakken ingedeeld is. Niet accuraat op grote afstanden.",
            description = "Retourneert een lijst van politiebureaus binnen de opgegeven straal vanaf de gespecificeerde latitude en longitude"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Succesvol: De lijst van politiebureaus is opgehaald.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PolitiebureausDTO.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Fout: Ongeldige invoer voor lat, lon of radius. Zorg ervoor dat de waarden geldige getallen zijn.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Fout: Er is een fout opgetreden bij het verwerken van de gegevens."
            )
    })
    public Response getBureausWithinRange(
            @Parameter(description = "Latitude van de locatie", required = true)
            @NotNull @QueryParam("lat") String latitudeString,

            @Parameter(description = "Longitude van de locatie", required = true)
            @NotNull @QueryParam("lon") String longitudeString,

            @Parameter(description = "Straal voor het zoeken, in kilometers (standaard 5)")
            @DefaultValue("5") @QueryParam("radius") String radiusString) {

        log.info("Start verwerking van getBureausWithinRange.");

        long startTime = System.nanoTime();

        double latitude = validateInput(latitudeString, "latitude");
        double longitude = validateInput(longitudeString, "longitude");
        double radius = validateInput(radiusString, "radius");

        PolitiebureausDTO result = PolitiebureausDTO
                .builder()
                .politiebureaus(politiebureauService.findBureausWithinRange(latitude, longitude, radius))
                .build();

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        log.info("Verwerking van getBureausWithinRange voltooid. Doorlooptijd: {} nanoseconden.", duration);

        return Response.ok(result).build();
    }


    @GET
    @Path("fast")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Haal politiebureaus op binnen een bepaald bereik op basis van latitude en longitude. Dit endpoint maakt gebruik van een meer aarde-achtige sphere die met rechthoekige vakken ingedeeld is. Accurater op grotere afstanden. Daarnaast is dit endpoint na het inladen van alle politiebureaus zo'n vier tot zes keer zo snel.",
            description = "Retourneert een lijst van politiebureaus binnen de opgegeven straal vanaf de gespecificeerde latitude en longitude"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Succesvol: De lijst van politiebureaus is opgehaald.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PolitiebureausDTO.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Fout: Ongeldige invoer voor lat, lon of radius. Zorg ervoor dat de waarden geldige getallen zijn.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Fout: Er is een fout opgetreden bij het verwerken van de gegevens."
            )
    })
    public PolitiebureausDTO getBureausWithinRangeFast(@NotNull @QueryParam("lat") String latitudeString,
                                                       @NotNull @QueryParam("lon") String longitudeString,
                                                       @DefaultValue("5") @QueryParam("radius") String radiusString) {
        log.info("Start verwerking van getBureausWithinRangeFast.");
        long startTime = System.nanoTime();

        double latitude = validateInput(latitudeString, "latitude");
        double longitude = validateInput(longitudeString, "longitude");
        double radius = validateInput(radiusString, "radius");

        PolitiebureausDTO result = PolitiebureausDTO
                .builder()
                .politiebureaus(politiebureauService.findBureausWithinRangeFast(latitude, longitude, radius))
                .build();

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        log.info("Verwerking van getBureausWithinRangeFast voltooid. Doorlooptijd: {} nanoseconden.", duration);

        return result;
    }

    /**
     * Controleert de invoer door te proberen deze om te zetten naar een {@link BigDecimal}.
     * Als de invoer ongeldig is (bijvoorbeeld als het geen geldig getal is), wordt een {@link InvalidInputException} gegooid.
     *
     * @param input De invoerwaarde die gecontroleerd moet worden. Dit moet een string zijn die een numerieke waarde vertegenwoordigt.
     * @param value Het value van de waarde (bijvoorbeeld "latitude" of "longitude") die gecontroleerd wordt, gebruikt voor foutmelding.
     * @return De gecontroleerde waarde als een {@link BigDecimal}.
     * @throws InvalidInputException Wanneer de invoer geen geldig getal is, wordt een Exception gegooid met een gedetailleerde foutmelding.
     */
    double validateInput(String input, String value) {
        try {
            return Double.parseDouble(input.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            log.error("Fout bij het verwerken van {}: {}", value, e.getMessage());
            throw new InvalidInputException(
                    "Ongeldige waarde voor " + value,
                    "Ongeldige waarde voor " + value + ": " + input
            );
        }
    }

}
