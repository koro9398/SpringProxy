package com.example.springproxy.scope;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Random;

@SessionScope(proxyMode = ScopedProxyMode.INTERFACES)
@Service("SessionInterfacedService")
public class SessionInterfacedService implements ScopeBean {
    private final int number = (new Random()).nextInt();

    @Override
    public int getGeneratedNumber() {
        return number;
    }
}
