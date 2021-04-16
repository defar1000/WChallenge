package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.model.Album;

import java.util.List;

public interface AlbumService {
    List<Album> getAllAlbums();
    List<Album> getAlbumsOfUser(int userId);
}
