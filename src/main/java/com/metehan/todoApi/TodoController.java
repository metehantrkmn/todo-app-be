package com.metehan.todoApi;

import com.metehan.todoApi.todo.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class TodoController {

    private final TodoService service;

    @PostMapping("/api/v1/add-todo")
    public ResponseEntity addTodo(@RequestBody Todo todo){
        return service.addTodo(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                todo
        );
    }

    @GetMapping("/api/v1/get-todos")
    public ResponseEntity getTodos(){
        return service.getTodos(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    @PutMapping("/api/v1/update-todo")
    public ResponseEntity updateTodo(@RequestBody Todo todo){
        //we can change method parameters to only todo and email, todo already have heading parameter
        return service.updateTodo(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                todo.getHeading(),
                todo
        );
    }

    @DeleteMapping("/api/v1/delete-todo")
    public ResponseEntity deleteTodo(@RequestParam String heading){
        return service.deleteTodo(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                heading
        );

    }


}
