package com.example.springproxy.scope;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Random;

@RequestScope //proxyMode() default ScopedProxyMode.TARGET_CLASS
@Service
public class RequestService implements ScopeBean {
    private final int number = (new Random()).nextInt();

    @Override
    public int getGeneratedNumber() {
        return number;
    }
}
