package com.example.springproxy.scope;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SingletonService {
    private final PrototypeNoProxyService prototypeNoProxyService;
    private final PrototypeCglibService prototypeCglibService;

    public int getNoProxyRandomInt() {
        return prototypeNoProxyService.getGeneratedNumber();
    }

    public int getCglibRandomInt() {
        return prototypeCglibService.getGeneratedNumber();
    }
}
