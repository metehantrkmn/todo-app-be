package com.metehan.todoApi.todo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@Builder
@AllArgsConstructor
public class UserDocument {

    @Id
    private String id;
    private String email;
    private List<Todo> todos;

    public boolean isHeadingExists(String heading){
        for(Todo t: todos){
            if(t.getHeading().equals(heading))
                return true;
        }
        return false;
    }

    public void addTodo(Todo todo){
        todos.add(todo);
    }

}
