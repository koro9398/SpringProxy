package com.example.springproxy.catservice;

import com.example.springproxy.model.Cat;
import com.example.springproxy.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис с поддержкой транзакций. Должен проксироваться CGLIB.
 */
@Service("CatServiceTransactionalImpl")
@RequiredArgsConstructor
public class CatServiceTransactionalImpl implements CatService {
    private final CatRepository catRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Cat> getById(Long id) {
        return catRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cat> getAll() {
        return catRepository.findAll();
    }
}
