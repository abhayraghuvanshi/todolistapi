package com.abc.demo.todolist.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.demo.todolist.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
