package com.example.Hrms.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hrms.Entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>
{

   Optional<Users> findByEmail(String email);
   List<Users> findByRoleIgnoreCase(String role);
   Optional<Users> findByNameIgnoreCase(String name);
   
}
