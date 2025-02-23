package nl.politie.politiebureau.dto;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolitiebureauDTO {
    @Schema(description = "De naam van het politiebureau", example = "Pop-up Bureau Utrecht")
    private String name;
    @Schema(description = "De plaats van het politiebureau", example = "Utrecht")
    private String plaats;
    @Schema(description = "De geografische latitude van het politiebureau", example = "52.3676")
    private String latitude;
    @Schema(description = "De geografische longitude van het politiebureau", example = "4.9041")
    private String longitude;
}
