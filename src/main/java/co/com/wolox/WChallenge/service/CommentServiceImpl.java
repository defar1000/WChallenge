package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.Settings;
import co.com.wolox.WChallenge.model.Comment;
import co.com.wolox.WChallenge.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentServiceImpl implements CommentService{

    private RestTemplate restTemplate;
    private Settings settings;

    @Autowired
    public CommentServiceImpl(RestTemplate restTemplate, Settings settings) {
        this.restTemplate = restTemplate;
        this.settings = settings;
    }

    @Override
    public List<Comment> getComments(String name, String email) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("comments");
        List<Comment> comments = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), Comment[].class));
        return name.isEmpty() && email.isEmpty()?
                comments:
                comments.stream().filter(comment -> comment.getName().equals(name) || comment.getEmail().equals(email)).collect(Collectors.toList());
    }
}
