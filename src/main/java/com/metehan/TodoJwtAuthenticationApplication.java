package com.metehan;

import com.metehan.todoApi.todo.TodoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TodoJwtAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoJwtAuthenticationApplication.class, args);
	}

}

