package co.com.wolox.WChallenge.rest;

import co.com.wolox.WChallenge.model.Comment;
import co.com.wolox.WChallenge.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("comments")
public class CommentsController {
    private CommentService commentService;

    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping()
    public ResponseEntity<List<Comment>> getComments(@RequestParam(value = "name", defaultValue = "") String name,
                                                     @RequestParam(value = "email", defaultValue = "") String email){
        return new ResponseEntity<>(commentService.getComments(name, email), HttpStatus.OK);
    }
}
