package co.com.wolox.WChallenge.rest;

import co.com.wolox.WChallenge.Settings;
import co.com.wolox.WChallenge.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@EnableConfigurationProperties(value = Settings.class)
@TestPropertySource("classpath:application.properties")
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private Settings settings;
    @Autowired
    private CommentController commentController;
    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp(){
        Mockito.when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.eq(Comment[].class)))
                .thenReturn(getCommentsArray());
    }

    @Test
    void getCommentsTest() {
        ResponseEntity<List<Comment>> commentsResponse = commentController.getComments("","");
        assertEquals("name3", commentsResponse.getBody().get(2).getName());
    }

    @Test
    void getCommentsFilteredEmailTest() {
        ResponseEntity<List<Comment>> commentsResponse = commentController.getComments("","email5");
        assertEquals("email5", commentsResponse.getBody().get(0).getEmail());
    }

    @Test
    void getCommentsFilteredNameTest() {
        ResponseEntity<List<Comment>> commentsResponse = commentController.getComments("name1","");
        assertEquals("name1", commentsResponse.getBody().get(0).getName());
    }

    private Comment[] getCommentsArray() {
        Comment[] comments = new Comment[5];
        for (int i=1; i<=5; i++){
            Comment comment = new Comment();
            comment.setId(i);
            comment.setBody("body"+i);
            comment.setEmail("email"+i);
            comment.setName("name"+i);
            comment.setPostId(i);
            comments[i-1]=comment;
        }
        return comments;
    }
}