package models;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class UserAddress {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private UserGeo geo;
}
