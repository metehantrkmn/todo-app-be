package com.metehan.todoApi.todo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends MongoRepository<UserDocument, String> {

    UserDocument findByEmail(String email);

}
