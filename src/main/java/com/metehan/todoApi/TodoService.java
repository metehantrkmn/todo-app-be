package com.metehan.todoApi;

import com.metehan.todoApi.dto.Todos;
import com.metehan.todoApi.exception.NoTodosExistException;
import com.metehan.todoApi.exception.TodoAlreadyExistsException;
import com.metehan.todoApi.todo.Todo;
import com.metehan.todoApi.todo.TodoRepository;
import com.metehan.todoApi.todo.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;

    public ResponseEntity addTodo(String email, Todo todo){
        /*
         * find proper document by email
         * check if todo heading already exists
         * ifnot then add todo to the todos list
         * save the document to update
         * */
        UserDocument document = repository.findByEmail(email);
        if (document.getTodos() != null){
            for(Todo t: document.getTodos()){
                if(todo.getHeading().equals(t.getHeading()))
                    throw new TodoAlreadyExistsException("todo with this heading already eist");
            }
        }else{
            document.setTodos(new ArrayList<Todo>());
        }
        //if you save a document already exists then it updates the document
        document.addTodo(todo);
        repository.save(document);
        return ResponseEntity.ok(repository.findByEmail(email));
    }

    public ResponseEntity getTodos(String email){
        UserDocument document = repository.findByEmail(email);
        var todos = Todos.builder()
                        .todos(document.getTodos())
                        .build();
        return ResponseEntity.ok(todos);
    }

    public ResponseEntity updateTodo(String email, String heading,Todo todo){
        /*
        get docment by email
        find todo by heading usind a foreach loop
        change the values
        save
         */

        UserDocument document = repository.findByEmail(email);

        if(document.getTodos() != null){
            for(Todo t: document.getTodos()){
                if(t.getHeading().equals(heading))
                    t.setDescription(todo.getDescription());
            }
        }else{
            throw new NoTodosExistException("no such todo exists");
        }
        repository.save(document);

        return ResponseEntity.ok(document.getTodos());
    }

    public ResponseEntity deleteTodo(String email, String heading){

        /*find the document by email
          find the todo by heading
          throw notodosExist Exception
          make todo equal null
          and save document to update
         */

        UserDocument document = repository.findByEmail(email);

        if(document.getTodos() == null)
            throw new NoTodosExistException("no such todo with that heading exists");

        for (int i = 0; i<document.getTodos().size(); i++){
            if(document.getTodos().get(i).getHeading().equals(heading))
                document.getTodos().remove(i);
        }
        repository.save(document);
        return ResponseEntity.ok(repository.findByEmail(email));
    }

}
