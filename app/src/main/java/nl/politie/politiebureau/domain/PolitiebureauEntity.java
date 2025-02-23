package nl.politie.politiebureau.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolitiebureauEntity {
    private String name;
    private String plaats;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
