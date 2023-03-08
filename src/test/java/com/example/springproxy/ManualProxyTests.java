package com.example.springproxy;

import static com.example.springproxy.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.springproxy.manualproxy.ManualProxy;
import com.example.springproxy.manualproxy.ManualProxyFactory;
import com.example.springproxy.manualproxy.ManualProxyImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ManualProxyTests {
    @Autowired
    ManualProxyFactory factory;

    /**
     * Проверка вручную созданных прокси.<br/>
     * {@code cglib} - CGLIB, как наследник оригинального класса позволяет вызывать и другие методы.<br/>
     * {@code dynamic} - Java Dynamic Proxy, проксирует только методы интерфейса.
     */
    @Test
    void testManualProxy() {
        var cglib = factory.getCglibProxy();
        var dynamic = factory.getDynamicProxy();

        // Проверяем типы прокси
        assertTrue(cglib.getClass().getName().contains(CGLIB_ENHANCER_MARK));
        assertTrue(dynamic.getClass().getName().contains(DYNAMIC_PROXY_ENHANCER_MARK));

        // Проверяем, что прокси вполне себе соотносятся с исходным классом
        assertTrue(cglib instanceof ManualProxy);
        assertTrue(cglib instanceof ManualProxyImpl);
        assertTrue(dynamic instanceof ManualProxy);
        // Кроме Dynamic Proxy, так как он имеет ссылку на объект, а в иерархии только реализует те же интерфейсы
        assertFalse(dynamic instanceof ManualProxyImpl);

        // Проверяем применение прокси
        var cglibMethodMark = "Cglib Proxy: ";
        var dynamicMethodMark = "Dynamic Proxy: ";
        assertEquals(cglibMethodMark + ManualProxyImpl.class.getName(), cglib.someMethod());
        assertEquals(dynamicMethodMark + ManualProxyImpl.class.getName(), dynamic.someMethod());
        assertEquals(ManualProxyImpl.class.getName(), ((ManualProxyImpl) cglib).anotherMethod());
        // Нет возможности вызвать данный метод, так как он не является частью интерфейса
        // assertEquals(ManualProxyImpl.class.getName(), dynamic.anotherMethod());
    }
}
