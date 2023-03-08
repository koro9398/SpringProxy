package com.example.springproxy.catservice;

import com.example.springproxy.model.Cat;

import java.util.List;
import java.util.Optional;

public interface CatService {
    Optional<Cat> getById(Long id);

    List<Cat> getAll();
}
