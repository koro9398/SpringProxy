package com.example.springproxy.scope;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Random;

@RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
@Service("RequestInterfacedService")
public class RequestInterfacedService implements ScopeBean {
    private final int number = (new Random()).nextInt();

    @Override
    public int getGeneratedNumber() {
        return number;
    }
}
