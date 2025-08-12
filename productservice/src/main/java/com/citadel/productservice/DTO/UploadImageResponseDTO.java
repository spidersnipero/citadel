package com.citadel.productservice.DTO;

public class UploadImageResponseDTO {

    private String signedURL;
    private String imageKey;

    public UploadImageResponseDTO(String signedURL, String imageKey) {
        this.signedURL = signedURL;
        this.imageKey = imageKey;
    }

    public String getSignedURL() {
        return signedURL;
    }

    public void setSignedURL(String signedURL) {
        this.signedURL = signedURL;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
