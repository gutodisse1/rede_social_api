package com.example.api_v1.photos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/v1/photos")
public class PhotosController {

    private static final String COMPLETE_PATH = "./src/main/resources/static/images/photos/";
    /**
	 * LETS CREATE SOME FAKE PHOTOS
	 */
    private static List<PhotoModel> photos = new ArrayList<PhotoModel>();
    
    public PhotosController(){
        PhotoModel photo01;
        photo01 = new PhotoModel(0, 0, StoragePhotos.getPhoto(COMPLETE_PATH+"neyjr_001.png"));
        photos.add(photo01);
        photo01 = new PhotoModel(1, 0, StoragePhotos.getPhoto(COMPLETE_PATH+"neyjr_002.png"));
        photos.add(photo01);

        photo01 = new PhotoModel(2, 1, StoragePhotos.getPhoto(COMPLETE_PATH+"martavsilva10_001.png"));
        photos.add(photo01);
        photo01 = new PhotoModel(3, 1, StoragePhotos.getPhoto(COMPLETE_PATH+"martavsilva10_002.png"));
        photos.add(photo01);

        photo01 = new PhotoModel(4, 2, StoragePhotos.getPhoto(COMPLETE_PATH+"gutodisse_001.jpg"));
        photos.add(photo01);
        photo01 = new PhotoModel(5, 2, StoragePhotos.getPhoto(COMPLETE_PATH+"gutodisse_002.jpg"));
        photos.add(photo01);
    }

    public Integer createNewPhoto(Integer userId, String name, MultipartFile file){
        PhotoModel newPhoto;

        Boolean photoUploadSucess = StoragePhotos.createImage(name, file);
        Integer photoId;
        
        if( photoUploadSucess) {
            photoId = photos.size();
            newPhoto = new PhotoModel(photoId, userId, StoragePhotos.getPhoto(COMPLETE_PATH+name));
            photos.add(newPhoto);
        } else {
            photoId = -1;
        }
        return photoId;
    }
    
    public PhotoModel searchByContentId(Integer contentId) {
        if(contentId >= 0 && contentId < photos.size()){
            return photos.get(contentId);
        }
        return null;
    }


    public List<PhotoModel> searchByUserId(Integer userId) {
        List<PhotoModel> photosUser = new ArrayList<PhotoModel>();

        for (PhotoModel photo : photos) {
            if(photo.getUserId() == userId){
                photosUser.add(photo);
            }
        }

        return photosUser;
    }
}