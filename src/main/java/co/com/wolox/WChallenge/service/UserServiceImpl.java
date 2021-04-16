package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.model.User;
import co.com.wolox.WChallenge.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    private RestTemplate restTemplate;
    private Settings settings;

    @Autowired
    public UserServiceImpl(RestTemplate restTemplate, Settings settings) {
        this.restTemplate = restTemplate;
        this.settings = settings;
    }

    @Override
    public User getUserById(int id) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("users").pathSegment(id + "");
        return restTemplate.getForObject(builder.build().toUri(), User.class);
    }

    @Override
    public User getUserByEmail(String email) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("users");
        List<User> users = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), User[].class));
        users = users.stream().filter(user -> user.getEmail().equals(email)).collect(Collectors.toList());
        return users.isEmpty()?new User():users.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("users");
        User[] users = restTemplate.getForObject(builder.build().toUri(), User[].class);
        return Arrays.asList(users);
    }
}
