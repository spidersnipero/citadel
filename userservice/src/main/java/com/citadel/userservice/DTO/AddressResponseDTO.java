package com.citadel.userservice.DTO;

import java.util.UUID;

public class AddressResponseDTO {


    private UUID addressId;


    private String houseNo;
    private String street;
    private String city;
    private String country;
    private String postalCode;
    private Boolean isDefault;

    public AddressResponseDTO(UUID addressId, String houseNo, String street, String city, String country, String postalCode, Boolean isDefault) {
        this.addressId = addressId;
        this.houseNo = houseNo;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.isDefault = isDefault;
    }


    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
