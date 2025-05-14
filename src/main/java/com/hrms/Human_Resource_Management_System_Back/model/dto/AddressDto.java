package com.hrms.Human_Resource_Management_System_Back.model.dto;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import lombok.Data;
/**
 * Data transfer object (DTO) for address information.
 * <p>
 * - country: The country of the address.
 * - city: The city of the address.
 * - street: The street of the address.
 * - zip: The zip code of the address.
 * </p>
 */

@Data
public class AddressDto {
    private String country;
    private String city;
    private String street;
    private String zip;

    public Address toEntity() {
        return Address.builder()
                .country(country).city(city)
                .street(street).zip(zip)
                .build();
    }
}
