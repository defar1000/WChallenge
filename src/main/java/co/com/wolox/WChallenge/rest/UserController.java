package co.com.wolox.WChallenge.rest;

import co.com.wolox.WChallenge.exceptions.ApiException.NotFoundException;
import co.com.wolox.WChallenge.model.User;
import co.com.wolox.WChallenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id, HttpServletResponse response) throws IOException {
        try {
            return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            answer(response, e);
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(@RequestParam(value = "email", defaultValue = "") String email, HttpServletResponse response) throws IOException {
        try {
            return new ResponseEntity<>(userService.getAll(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            answer(response, e);
            return null;
        }
    }

    private void answer(HttpServletResponse response, NotFoundException e) throws IOException {
        response.setStatus(e.getStatusCode().value());
        response.getWriter().write(e.getMessage());
        response.getWriter().flush();
    }
}
