package com.hrms.Human_Resource_Management_System_Back.model.dto;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import lombok.Data;

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
