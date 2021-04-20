package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.Settings;
import co.com.wolox.WChallenge.exceptions.ApiException.NotFoundException;
import co.com.wolox.WChallenge.model.Album;
import co.com.wolox.WChallenge.model.Album.SharedUser;
import co.com.wolox.WChallenge.model.User;
import co.com.wolox.WChallenge.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private static final String ALBUM_NOT_FOUND = "Album not found";
    private static final String NO_PERMISSIONS_ASSIGNED = "No permissions assigned";
    private static final String FORMAT_METHOD_NAME = "is%sPermissions";
    private RestTemplate restTemplate;
    private Settings settings;
    private AlbumRepository albumRepository;
    private UserService userService;

    @Autowired
    public AlbumService(RestTemplate restTemplate, Settings settings, AlbumRepository albumRepository,
                        UserService userService) {
        this.restTemplate = restTemplate;
        this.settings = settings;
        this.albumRepository = albumRepository;
        this.userService = userService;
    }

    public List<Album> getAll() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("albums");
        List<Album> albums = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), Album[].class));
        return getSharedUsers(albums);
    }

    public Album getAlbum(int id) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("albums").pathSegment(id+"");
        try {
            Album album = restTemplate.getForObject(builder.build().toUri(), Album.class);
            return getSharedUser(album);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException(ALBUM_NOT_FOUND);
        }
    }

    public List<Album> getUserAlbums(int userId) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("albums");
        List<Album> albums = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), Album[].class));
        return albums.stream().filter(album -> album.getUserId() == userId).collect(Collectors.toList());
    }

    public Album registerSharedAlbum(int id, SharedUser sharedUser) throws NotFoundException {
        Album album = albumRepository.findById(id).orElse(getAlbum(id));
        if (album==null) throw new NotFoundException(ALBUM_NOT_FOUND);
        Set<SharedUser> sharedUsers = Optional.ofNullable(album.getSharedUsers()).orElse(new HashSet<>());
        userService.getUser(sharedUser.getUserId());
        sharedUsers.add(sharedUser);
        album.setSharedUsers(sharedUsers);
        return albumRepository.save(album);
    }

    public Album updateSharedAlbum(int id, SharedUser sharedUser) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (!albumOptional.isPresent()) throw new NotFoundException(NO_PERMISSIONS_ASSIGNED + " or " + ALBUM_NOT_FOUND);
        Album album = albumOptional.get();
        Set<SharedUser> sharedUsers = album.getSharedUsers();
        if (sharedUsers.contains(sharedUser)) {
            sharedUsers.remove(sharedUser);
            sharedUsers.add(sharedUser);
        } else throw new NotFoundException(NO_PERMISSIONS_ASSIGNED);
        return albumRepository.save(album);
    }

    public List<User> getSharedUsers(int id, String permission) throws NoSuchMethodException {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (!albumOptional.isPresent()) throw new NotFoundException(NO_PERMISSIONS_ASSIGNED + " or " + ALBUM_NOT_FOUND);
        Album album = albumOptional.get();
        Method method = SharedUser.class.getMethod(String.format(FORMAT_METHOD_NAME, permission.toUpperCase()));
        List<Integer> userIds = album.getSharedUsers().stream()
                .filter(sharedUser -> {
                    try {
                        return (Boolean) method.invoke(sharedUser);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(SharedUser::getUserId)
                .collect(Collectors.toList());
        return userService.getAll("").stream().filter(user -> userIds.contains(user.getId())).collect(Collectors.toList());
    }

    private List<Album> getSharedUsers(List<Album> albums) {
        albums.forEach(this::getSharedUser);
        return albums;
    }

    private Album getSharedUser(Album album) {
        albumRepository.findById(album.getId()).ifPresent(albumDB -> album.setSharedUsers(albumDB.getSharedUsers()));
        return album;
    }

}
