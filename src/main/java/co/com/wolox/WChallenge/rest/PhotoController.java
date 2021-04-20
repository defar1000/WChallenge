package co.com.wolox.WChallenge.rest;

import co.com.wolox.WChallenge.model.Photo;
import co.com.wolox.WChallenge.service.AlbumService;
import co.com.wolox.WChallenge.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("photos")
public class PhotoController {

    private PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping()
    public ResponseEntity<List<Photo>> getAll() {
        return new ResponseEntity<>(photoService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Photo>> getUserPhotos (@PathVariable int userId){
        return new ResponseEntity<>(photoService.getPhotosOfUser(userId), HttpStatus.OK);
    }

}
