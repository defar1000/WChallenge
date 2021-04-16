package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.Settings;
import co.com.wolox.WChallenge.model.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlbumServiceImpl implements AlbumService {

    private RestTemplate restTemplate;
    private Settings settings;

    @Autowired
    public AlbumServiceImpl(RestTemplate restTemplate, Settings settings) {
        this.restTemplate = restTemplate;
        this.settings = settings;
    }

    @Override
    public List<Album> getAllAlbums() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("albums");
        Album[] albums = restTemplate.getForObject(builder.build().toUri(), Album[].class);
        return Arrays.asList(albums);
    }

    @Override
    public List<Album> getAlbumsOfUser(int userId) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("albums");
        List<Album> albums = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), Album[].class));
        return albums.stream().filter(album -> album.getUserId() == userId).collect(Collectors.toList());
    }
}
