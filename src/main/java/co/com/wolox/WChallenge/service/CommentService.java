package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(String name, String email);
}
