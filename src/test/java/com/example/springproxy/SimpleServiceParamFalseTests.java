package com.example.springproxy;

import static com.example.springproxy.Constants.SPRING_CGLIB_ENHANCER_MARK;
import static com.example.springproxy.Constants.DYNAMIC_PROXY_ENHANCER_MARK;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.springproxy.simpleservice.InterfacedServiceImpl;
import com.example.springproxy.simpleservice.InterfacedService;
import com.example.springproxy.simpleservice.SimpleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Класс тестов, аналогичный {@link SimpleServiceTests}, но с параметром {@code spring.aop.proxy-target-class=false}.<br/>
 * Без него классы, реализующие интерфейс, проксируются с помощью CGLIB. Явная установка параметра в false отключает
 * поведение по умолчанию, и такие классы проксируются с помощью Java Dynamic Proxy.
 */
@SpringBootTest
@ActiveProfiles("aopFalse")
public class SimpleServiceParamFalseTests {
    @Autowired
    private SimpleService simpleService;

    @Autowired
    @Qualifier("LambdaInterfacedService")
    private InterfacedService lambdaInterfacedService;

    @Autowired
    @Qualifier("InterfacedServiceImpl")
    private InterfacedService interfacedService;

    /**
     * Проверка применения аспекта и последующего проксирования.<br/>
     * - {@code simpleService} - не реализует интерфейсов, проксируется через CGLIB.<br/>
     * - {@code interfacedService} - реализует интерфейс, из-за параметра aop используется Java Dynamic Proxy.<br/>
     * - {@code lambdaInterfacedService} - бин объявлен в виде лямбды, что вынуждает спринг использовать Java Dynamic Proxy.
     */
    @Test
    void testProxy() {
        // Проверяем, что аспект обработал возвращаемую из методов строку
        var proxyPrefix = "Proxied result:";
        assertTrue(simpleService.someMethod().startsWith(proxyPrefix));
        assertTrue(interfacedService.someMethod().startsWith(proxyPrefix));
        assertTrue(lambdaInterfacedService.someMethod().startsWith(proxyPrefix));

        // Проверяем типы прокси
        assertTrue(simpleService.getClass().getName().contains(SPRING_CGLIB_ENHANCER_MARK));
        // С spring.aop.proxy-target-class=false получаем динамический прокси, а не CGLIB
        assertTrue(interfacedService.getClass().getName().contains(DYNAMIC_PROXY_ENHANCER_MARK));
        assertTrue(lambdaInterfacedService.getClass().getName().contains(DYNAMIC_PROXY_ENHANCER_MARK));

        // Проверяем, что прокси вполне себе соотносятся с исходным классом
        assertTrue(simpleService instanceof SimpleService);
        assertTrue(interfacedService instanceof InterfacedService);
        // С spring.aop.proxy-target-class=false получаем динамический прокси,
        // а значит не пройдём проверку типов на конкретный класс
        assertFalse(interfacedService instanceof InterfacedServiceImpl);
        assertTrue(lambdaInterfacedService instanceof InterfacedService);
    }
}
