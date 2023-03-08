package com.example.springproxy.simpleservice;

import org.springframework.stereotype.Component;

/**
 * Простой сервис. Будет проксироваться CGLIB из-за применённого аспекта.
 * @see com.example.springproxy.aspect.SimpleServiceAspect
 */
@Component
public class SimpleService {
    public String someMethod() {
        return "SimpleService.someMethod() executed";
    }
}
