package com.example.springproxy.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Random;

// Если proxyMode = ScopedProxyMode.INTERFACES, но при этом убрать реализацию интерфейса, то JDK прокси будет создана,
// но по системным или спринговым интерфейсам, и мы не сможем получить никакого полезного доступа к оригинальному
// объекту, что и так подразумевается с JDK прокси.
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
public class PrototypeDynamicService implements ScopeBean {
    private final int number = (new Random()).nextInt();

    @Override
    public int getGeneratedNumber() {
        return number;
    }
}
