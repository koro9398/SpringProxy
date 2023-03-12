package com.example.springproxy.scope;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Random;

@SessionScope //proxyMode() default ScopedProxyMode.TARGET_CLASS
@Service
public class SessionService implements ScopeBean {
    private final int number = (new Random()).nextInt();

    @Override
    public int getGeneratedNumber() {
        return number;
    }
}
