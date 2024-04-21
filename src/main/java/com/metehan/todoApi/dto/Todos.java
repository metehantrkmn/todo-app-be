package com.metehan.todoApi.dto;

import com.metehan.todoApi.todo.Todo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Todos {
    private List<Todo> todos;
}
