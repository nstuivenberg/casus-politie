package nl.politie.politiebureau.dto;

import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@Builder
@Schema(description = "Lijst van politiebureaus binnen het bereik")
public class PolitiebureausDTO {
    @Schema(description = "Lijst van politiebureaus die binnen het opgegeven bereik vallen")
    private final List<PolitiebureauDTO> politiebureaus;
}
