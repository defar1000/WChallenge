package co.com.wolox.WChallenge.repository;

import co.com.wolox.WChallenge.model.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Album, Integer> { }
