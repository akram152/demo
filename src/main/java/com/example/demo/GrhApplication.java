package com.example.demo;

import com.example.demo.Enum.Role;
import com.example.demo.entities.Roles;
import com.example.demo.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class GrhApplication/* implements CommandLineRunner*/ {

    public static void main(String[] args) {
        SpringApplication.run(GrhApplication.class, args);
    }

   /* @Autowired
    RolesRepository repository;

    @Override
    public void run(String... args) throws Exception {
        Roles roleRh = new Roles();
        roleRh.setName(Role.RH);
        Roles roleDaf = new Roles();
        roleDaf.setName(Role.DAF);
        Roles roleDev = new Roles();
        roleDev.setName(Role.DEV);
        List<Roles> roles = List.of(roleRh, roleDaf, roleDev);
        repository.saveAll(roles);
    }*/
}
