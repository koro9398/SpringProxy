package com.example.springproxy.catservice;

import com.example.springproxy.model.Cat;
import com.example.springproxy.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Простой сервис. Не должен проксироваться.
 */
@Service("CatServiceImpl")
@RequiredArgsConstructor
public class CatServiceImpl implements CatService {
    private final CatRepository catRepository;

    @Override
    public Optional<Cat> getById(Long id) {
        return catRepository.findById(id);
    }

    @Override
    public List<Cat> getAll() {
        return catRepository.findAll();
    }
}
