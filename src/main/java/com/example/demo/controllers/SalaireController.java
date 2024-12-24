package com.example.demo.controllers;

import com.example.demo.entities.Salaire;
import com.example.demo.services.ISalaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("salaire")
public class SalaireController {
    @Autowired
    ISalaireService salaireService;
    @PostMapping("saveSalaire")
    public Salaire saveOrUpdateSalaire(@RequestBody Salaire salaire) {

        return salaireService.saveOrUpdateSalaire(salaire);
    }
}
