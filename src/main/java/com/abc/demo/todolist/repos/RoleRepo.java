package com.abc.demo.todolist.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.demo.todolist.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

}
