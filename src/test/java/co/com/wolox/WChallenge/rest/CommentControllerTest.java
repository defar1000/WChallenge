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
import org.springframework.web.util.UriComponentsBuilder;

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

    @Test
    void getComments() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("comments");

        Mockito.when(restTemplate.getForObject(builder.build().toUri(), Comment[].class)).thenReturn(getCommentsArray());
        ResponseEntity<List<Comment>> commentsResponse = commentController.getComments("","");
        assertEquals(commentsResponse.getBody().get(2).getName(), "name3");
    }

    @Test
    void getCommentsFilteredEmail() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("comments");

        Mockito.when(restTemplate.getForObject(builder.build().toUri(), Comment[].class)).thenReturn(getCommentsArray());
        ResponseEntity<List<Comment>> commentsResponse = commentController.getComments("","email5");
        assertEquals(commentsResponse.getBody().get(0).getEmail(), "email5");
    }

    @Test
    void getCommentsFilteredName() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("comments");

        Mockito.when(restTemplate.getForObject(builder.build().toUri(), Comment[].class)).thenReturn(getCommentsArray());
        ResponseEntity<List<Comment>> commentsResponse = commentController.getComments("name1","");
        assertEquals(commentsResponse.getBody().get(0).getName(), "name1");
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