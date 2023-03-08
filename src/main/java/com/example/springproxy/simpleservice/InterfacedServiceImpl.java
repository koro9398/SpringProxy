package com.example.springproxy.simpleservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Простой сервис. Из-за применённого аспекта должен быть проксирован.
 * Но несмотря на наличие интерфейса, проксируется всё равно с помощью CGLIB.
 * @see com.example.springproxy.aspect.SimpleServiceAspect
 */
@Component
@Qualifier("InterfacedServiceImpl")
public class InterfacedServiceImpl implements InterfacedService {
    @Override
    public String someMethod() {
        return "InterfacedServiceImpl.someMethod() executed";
    }
}
