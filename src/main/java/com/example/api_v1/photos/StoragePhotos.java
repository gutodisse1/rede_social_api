package com.example.api_v1.photos;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

public class StoragePhotos {

  public static Boolean createImage(String name, MultipartFile file) {
    try {
      File image = new File(
          "/home/nomedousuario/SideProjects/Zup/BlogPost/demo/src/main/resources/static/images/photos/" + name);
      BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(image));
      stream.write(file.getBytes());
      stream.close();

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static String getPhoto(String imagePath) {
    String base64Image = "";
    File file = new File(imagePath);
    try (FileInputStream imageInFile = new FileInputStream(file)) {
      // Reading a Image file from file system
      byte imageData[] = new byte[(int) file.length()];
      imageInFile.read(imageData);
      base64Image = Base64.getEncoder().encodeToString(imageData);
    } catch (FileNotFoundException e) {
      System.out.println("Image not found" + e);
    } catch (IOException ioe) {
      System.out.println("Exception while reading the Image " + ioe);
    }
    return base64Image;
  }

  public StoragePhotos() {

  }

}