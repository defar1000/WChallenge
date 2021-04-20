package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.exceptions.ApiException.NotFoundException;
import co.com.wolox.WChallenge.model.User;
import co.com.wolox.WChallenge.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private RestTemplate restTemplate;
    private Settings settings;

    @Autowired
    public UserService(RestTemplate restTemplate, Settings settings) {
        this.restTemplate = restTemplate;
        this.settings = settings;
    }

    public User getUser(int id) throws NotFoundException {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("users").pathSegment(id + "");
        try{
            return restTemplate.getForObject(builder.build().toUri(), User.class);
        } catch (HttpClientErrorException e) {
            throw getNotFoundException();
        }
    }

    public List<User> getAll(String email) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("users");
        List<User> users = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), User[].class));
        if (email.isEmpty()) return users;
        users = users.stream().filter(user -> user.getEmail().equals(email)).collect(Collectors.toList());
        if (users.isEmpty()) throw getNotFoundException();
        return users;
    }

    private HttpClientErrorException getNotFoundException() {
        return new NotFoundException("User not found");
    }
}
