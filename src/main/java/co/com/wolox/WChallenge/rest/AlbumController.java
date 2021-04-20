package co.com.wolox.WChallenge.rest;

import co.com.wolox.WChallenge.exceptions.ApiException.NotFoundException;
import co.com.wolox.WChallenge.model.Album.SharedUser;
import co.com.wolox.WChallenge.model.Album;
import co.com.wolox.WChallenge.model.User;
import co.com.wolox.WChallenge.service.AlbumService;
import co.com.wolox.WChallenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("albums")
public class AlbumController {

    private AlbumService albumService;
    private UserService userService;

    @Autowired
    public AlbumController(AlbumService albumService, UserService userService) {
        this.albumService = albumService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<Album>> getAll() {
        return new ResponseEntity<>(albumService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable int id, HttpServletResponse response) throws IOException {
        try {
            return new ResponseEntity<>(albumService.getAlbum(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            answer(response, e);
            return null;
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Album>> getUserAlbums(@PathVariable int userId) throws IOException {
            return new ResponseEntity<>(albumService.getUserAlbums(userId), HttpStatus.OK);
    }

    /**
     * @param id         id del album al que se le daran permisos
     * @param sharedUser ejemplo de requestBody
     *                   {
     *                   "userId": "1",
     *                   "rPermissions": true,
     *                   "wPermissions": false
     *                   }
     * @return returna toda la informacion del album al que se dieron permisos
     */
    @PostMapping("/{id}")
    public ResponseEntity<Album> registerSharedAlbum(@PathVariable int id, @RequestBody SharedUser sharedUser, HttpServletResponse response) throws IOException {
        try {
            return new ResponseEntity<>(albumService.registerSharedAlbum(id, sharedUser), HttpStatus.OK);
        } catch (NotFoundException e) {
            answer(response, e);
            return null;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateSharedAlbum(@PathVariable int id, @RequestBody SharedUser sharedUser, HttpServletResponse response) throws IOException {
        try {
            return new ResponseEntity<>(albumService.updateSharedAlbum(id, sharedUser), HttpStatus.OK);
        } catch (NotFoundException e) {
            answer(response, e);
            return null;
        }
    }

    @GetMapping("/{id}/sharedUsers")
    public ResponseEntity<List<User>> getSharedUsers(@PathVariable int id, @RequestParam String permission, HttpServletResponse response) throws IOException {
        try {
            return new ResponseEntity<>(albumService.getSharedUsers(id, permission), HttpStatus.OK);
        } catch (NotFoundException e) {
            answer(response, e);
            return null;
        } catch (NoSuchMethodException e) {
            response.getWriter().write(e.getMessage());
            response.getWriter().flush();
            return null;
        }
    }

    private void answer(HttpServletResponse response, NotFoundException e) throws IOException {
        response.setStatus(e.getStatusCode().value());
        response.getWriter().write(e.getMessage());
        response.getWriter().flush();
    }

}
