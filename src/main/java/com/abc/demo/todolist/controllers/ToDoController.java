package com.abc.demo.todolist.controllers;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abc.demo.todolist.model.ToDoBuilder;
import com.abc.demo.todolist.model.Todo;
import com.abc.demo.todolist.repos.ToDoRepository;
import com.abc.demo.todolist.security.config.IAuthenticationFacade;
import com.abc.demo.todolist.validation.ToDoValidationError;
import com.abc.demo.todolist.validation.ToDoValidationErrorBuilder;
@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ToDoController {

	private ToDoRepository repository;
	
	@Autowired
    private IAuthenticationFacade authenticationFacade;

	@Autowired
	public ToDoController(ToDoRepository toDoRepository) {
		this.repository = toDoRepository;
	}

	@GetMapping("/todo")
	public ResponseEntity<Iterable<Todo>> getToDos() {
		String userName = authenticationFacade.getAuthentication().getName();
		
		return ResponseEntity.ok(repository.findByEmail(userName));
	}

	@GetMapping("/todo/{id}")
	public ResponseEntity<Todo> getToDoById(@PathVariable String id) {
		
		Optional<Todo> toDo = repository.findById(id);
		if (toDo.isPresent())
			return ResponseEntity.ok(toDo.get());
		return ResponseEntity.notFound().build();
	}

	@PatchMapping("/todo/{id}")
	public ResponseEntity<Todo> setCompleted(@PathVariable String id) {
		
		Optional<Todo> toDo = repository.findById(id);
		if (!toDo.isPresent())
			return ResponseEntity.notFound().build();
		Todo result = toDo.get();
		result.setCompleted(true);
		repository.save(result);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
		return ResponseEntity.ok().header("Location", location.toString()).build();
	}

	@RequestMapping(value = "/todo", method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<?> createToDo(@Valid @RequestBody Todo toDo, Errors errors) {
		String userName = authenticationFacade.getAuthentication().getName();
		
		toDo.setEmail(userName);
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
		}
		Todo result = repository.save(toDo);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/todo/{id}")
	public ResponseEntity<Todo> deleteToDo(@PathVariable String id) {
		
		repository.delete(ToDoBuilder.create().withId(id).build());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/todo")
	public ResponseEntity<Todo> deleteToDo(@RequestBody Todo toDo) {
		
		repository.delete(toDo);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ToDoValidationError handleException(Exception exception) {
		return new ToDoValidationError(exception.getMessage());
	}
}