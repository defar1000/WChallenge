package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.model.Photo;

import java.util.List;

public interface PhotoService {
    List<Photo> getAll();
    List<Photo> getPhotosOfUser(int userId);
}
