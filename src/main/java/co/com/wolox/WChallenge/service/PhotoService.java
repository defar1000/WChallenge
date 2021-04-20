package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.Settings;
import co.com.wolox.WChallenge.model.Album;
import co.com.wolox.WChallenge.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private AlbumService albumService;
    private RestTemplate restTemplate;
    private Settings settings;

    @Autowired
    public PhotoService(RestTemplate restTemplate, Settings settings, AlbumService albumService) {
        this.restTemplate = restTemplate;
        this.settings = settings;
        this.albumService = albumService;
    }

    public List<Photo> getAll() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("photos");
        Photo[] photos = restTemplate.getForObject(builder.build().toUri(), Photo[].class);
        return Arrays.asList(photos);
    }

    public List<Photo> getPhotosOfUser(int userId) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("photos");
        List<Photo> photos = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), Photo[].class));
        List<Integer> albumIds = albumService.getUserAlbums(userId).stream().map(Album::getId).collect(Collectors.toList());
        return photos.stream().filter(photo -> albumIds.contains(photo.getAlbumId())).collect(Collectors.toList());
    }
}
