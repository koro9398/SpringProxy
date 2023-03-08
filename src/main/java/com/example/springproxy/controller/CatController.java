package com.example.springproxy.controller;

import com.example.springproxy.model.Cat;
import com.example.springproxy.catservice.CatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cat")
@RequiredArgsConstructor
public class CatController {
    private final CatServiceImpl service;

    @GetMapping
    public List<Cat> getAllCats() {
        return service.getAll();
    }
}
