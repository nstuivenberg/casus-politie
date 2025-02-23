package nl.politie.politiebureau.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolitiebureauEntity {
    private String name;
    private String plaats;
    private double latitude;
    private double longitude;
}
