package com.abc.demo.todolist.repos;

import org.springframework.data.repository.CrudRepository;

import com.abc.demo.todolist.model.Todo;

public interface ToDoRepository extends CrudRepository<Todo, String> {
	Iterable<Todo> findByEmail(String email);
}