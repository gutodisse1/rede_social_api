package com.example.api_v1.photos;

public class PhotoModel {
    private Integer photoId;
    private Integer userId;
    private String photoBase64;
    // private List<String> keywords;
    
    public PhotoModel(Integer photoId, Integer userId, String photoBase64 ){
        this.photoId = photoId;
        this.userId  = userId;
        this.photoBase64 = photoBase64;
    }
    
    public Integer getPhotoId() {
        return photoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

}