package nl.politie.politiebureau.exception;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


@Data
@Schema(description = "Foutantwoord voor het API-verzoek")
public class ErrorResponse {

    @Schema(description = "HTTP statuscode van de fout", example = "400")
    private int status;

    @Schema(description = "Omschrijving van de fout", example = "Ongeldige invoer voor latitude")
    private String message;

    @Schema(description = "Gedetailleerde uitleg van de fout", example = "De waarde voor latitude is geen geldig getal.")
    private String details;

    public ErrorResponse(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }
}

