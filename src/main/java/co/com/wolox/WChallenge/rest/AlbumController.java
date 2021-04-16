package co.com.wolox.WChallenge.rest;

import co.com.wolox.WChallenge.model.Album;
import co.com.wolox.WChallenge.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("albums")
public class AlbumController {

    private AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping()
    public ResponseEntity<List<Album>> getAll() {
        return new ResponseEntity<>(albumService.getAllAlbums(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Album>> getAlbumsOfUser(@PathVariable int userId){
        return new ResponseEntity<>(albumService.getAlbumsOfUser(userId), HttpStatus.OK);
    }
}
