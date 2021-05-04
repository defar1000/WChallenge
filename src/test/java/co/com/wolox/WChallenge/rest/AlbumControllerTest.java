package co.com.wolox.WChallenge.rest;

import co.com.wolox.WChallenge.Settings;
import co.com.wolox.WChallenge.exceptions.ApiException.NotFoundException;
import co.com.wolox.WChallenge.model.Album;
import co.com.wolox.WChallenge.model.Album.SharedUser;
import co.com.wolox.WChallenge.model.User;
import co.com.wolox.WChallenge.repository.AlbumRepository;
import co.com.wolox.WChallenge.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@EnableConfigurationProperties(value = Settings.class)
@TestPropertySource("classpath:application.properties")
@SpringBootTest
class AlbumControllerTest {

    @Autowired
    private Settings settings;
    @Autowired
    private UserService userService;
    @Autowired
    private AlbumController albumController;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private AlbumRepository albumRepository;

    @Test
    void getAllTest() {
        when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(Album[].class)))
                .thenReturn(getAlbumsArrayWhitSharedUsers());
        List<Album> albumsResponse = albumController.getAll().getBody();
        assertEquals(6, albumsResponse.size());
    }

    @Test
    void notFoundAlbumTest() throws IOException {
        when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(Album.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertNull(albumController.getAlbum(0, getMockResponse()));
    }

    @Test
    void getUserAlbumsTest() throws IOException {
        when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(Album[].class)))
                .thenReturn(getAlbumsArrayWhitSharedUsers());
        assertEquals(2, albumController.getUserAlbums(5).getBody().size());
    }

    @Test
    void registerSharedAlbumTest() throws IOException {
        Album[] albums = getAlbumsArrayWhitSharedUsers();
        SharedUser sharedUser = new SharedUser(2, true, false);
        when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(Album.class)))
                .thenReturn(albums[0]);
        when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(User.class)))
                .thenReturn(new User());
        when(albumRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(albums[0]));
        when(albumRepository.save(albums[0]))
                .thenReturn(albums[0]);
        Album albumResponse = albumController.registerSharedAlbum(1, sharedUser, null).getBody();
        assertEquals(2, albumResponse.getSharedUsers().size());
    }

    @Test
    void noSharedAlbumsTest() throws IOException {
        when(albumRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        assertNull(albumController.updateSharedAlbum(1, null, getMockResponse()));
        assertNull(albumController.getSharedUsers(1, "r", getMockResponse()));
    }

    @Test
    void noPermissionAlbumsTest() throws IOException {
        when(albumRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(getAlbumsArrayWhitSharedUsers()[0]));
        assertNull(albumController.updateSharedAlbum(1, new SharedUser(2, true, false), getMockResponse()));
    }

    @Test
    void updateSharedAlbumTest() throws IOException {
        Album[] albums = getAlbumsArrayWhitSharedUsers();
        when(albumRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(albums[0]));
        when(albumRepository.save(albums[0]))
                .thenReturn(albums[0]);
        Album album = albumController.updateSharedAlbum(1, new SharedUser(1, true, false), null).getBody();
        assertEquals(albums[0], album);
    }

    @Test
    void getSharedUsersTest() throws IOException {
        Album[] albums = getAlbumsArrayWhitSharedUsers();
        when(albumRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(albums[0]));
        when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(User[].class)))
                .thenReturn(getUsers());
        List<User> users = albumController.getSharedUsers(1,"r", null).getBody();
        assertEquals(1, users.size());
    }

    @Test
    void badRequestGetSharedUsersTest() throws IOException {
        Album[] albums = getAlbumsArrayWhitSharedUsers();
        when(albumRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(albums[0]));
        when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(User[].class)))
                .thenReturn(getUsers());
        assertNull(albumController.getSharedUsers(1,"g", getMockResponse()));
    }

    private Album[] getAlbumsArrayWhitSharedUsers() {
        Album[] albums = new Album[6];
        for (int i=1; i<=5; i++){
            Album album = new Album();
            album.setId(i);
            album.setTitle("title"+i);
            album.setUserId(i);
            album.setSharedUsers(Sets.newSet(getSharedUser()));
            albums[i-1]=album;
        }
        Album album = new Album();
        album.setId(6);
        album.setTitle("title"+6);
        album.setUserId(5);
        album.setSharedUsers(Sets.newSet(getSharedUser()));
        albums[5]=album;
        return albums;
    }

    private SharedUser getSharedUser() {
        return new SharedUser(1, true, false);
    }

    private HttpServletResponse getMockResponse() throws IOException {
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = Mockito.mock(PrintWriter.class);
        when(httpServletResponse.getWriter()).thenReturn(writer);
        return httpServletResponse;
    }

    private User[] getUsers(){
        User[] users = new User[2];
        for (int i=1; i<=2; i++){
            User user = new User();
            user.setId(i);
            user.setName("name"+i);
            users[i-1]=user;
        }
        return users;
    }

}