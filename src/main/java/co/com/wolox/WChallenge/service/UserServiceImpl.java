package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.model.User;
import co.com.wolox.WChallenge.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    Settings settings;

    @Override
    public User getUserById(int id) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("users").pathSegment(id + "");
        User user = restTemplate.getForObject(builder.build().toUri(), User.class);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("users");
        List<User> users = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), User[].class));
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst().get();
    }

    @Override
    public List<User> getAllUsers() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.host(settings.getHost()).scheme(settings.getProtocol()).pathSegment("users");
        User[] users = restTemplate.getForObject(builder.build().toUri(), User[].class);
        return Arrays.asList(users);
    }
}
