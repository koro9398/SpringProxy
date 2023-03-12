package com.example.springproxy;

import static org.junit.jupiter.api.Assertions.*;

import com.example.springproxy.scope.ScopeBean;
import com.example.springproxy.scope.SingletonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class ScopeTests {
    private static final String SINGLETON_BEAN = "singletonService";
    private static final String PROTOTYPE_DYNAMIC_BEAN = "prototypeDynamicService";
    private static final String PROTOTYPE_CGLIB_BEAN = "prototypeCglibService";
    private static final String PROTOTYPE_NO_PROXY_BEAN = "prototypeNoProxyService";

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Singleton бин всегда в одном и том же экземпляре без проксирования.
     */
    @Test
    void testSingletonScope() {
        var bean1 = applicationContext.getBean(SINGLETON_BEAN);
        var bean2 = applicationContext.getBean(SINGLETON_BEAN);
        assertEquals(bean1, bean2);
        assertFalse(bean1.getClass().getName().contains(Constants.SPRING_CGLIB_ENHANCER_MARK));
        assertFalse(bean1.getClass().getName().contains(Constants.DYNAMIC_PROXY_ENHANCER_MARK));
        assertTrue(bean1 instanceof SingletonService);
    }

    /**
     * Prototype-бин требует дополнительной логики при использовании в других (например, singleton) бинах.
     */
    @Test
    void testPrototypeScope() {
        // По умолчанию фабрика просто создаёт каждый раз бин с таким скоупом.
        var noProxy1 = (ScopeBean) applicationContext.getBean(PROTOTYPE_NO_PROXY_BEAN);
        var noProxy2 = (ScopeBean) applicationContext.getBean(PROTOTYPE_NO_PROXY_BEAN);
        // Если добавить параметр proxyMode в @Scope, то фабрика будет каждый раз возвращать singleton-прокси,
        // который будет создавать новый объект
        var dynamic1 = (ScopeBean) applicationContext.getBean(PROTOTYPE_DYNAMIC_BEAN);
        var dynamic2 = (ScopeBean) applicationContext.getBean(PROTOTYPE_DYNAMIC_BEAN);
        var cglib1 = (ScopeBean) applicationContext.getBean(PROTOTYPE_CGLIB_BEAN);
        var cglib2 = (ScopeBean) applicationContext.getBean(PROTOTYPE_CGLIB_BEAN);

        // Простые "прототипы" через getBean() - разные
        assertNotEquals(noProxy1, noProxy2);
        // Проксированные "прототипы" через getBean - один и тот же прокси-объект
        assertEquals(dynamic1, dynamic2);
        assertEquals(cglib1, cglib2);
        // Но при вызове методов везде всё равно получаем разные инстансы и, соответственно, разный результат
        assertNotEquals(noProxy1.getGeneratedNumber(), noProxy2.getGeneratedNumber());
        assertNotEquals(dynamic1.getGeneratedNumber(), dynamic2.getGeneratedNumber());
        assertNotEquals(cglib1.getGeneratedNumber(), cglib2.getGeneratedNumber());

        // Проверим созданные прокси
        assertFalse(noProxy1.getClass().getName().contains(Constants.SPRING_CGLIB_ENHANCER_MARK));
        assertFalse(noProxy1.getClass().getName().contains(Constants.DYNAMIC_PROXY_ENHANCER_MARK));
        assertFalse(noProxy2.getClass().getName().contains(Constants.SPRING_CGLIB_ENHANCER_MARK));
        assertFalse(noProxy2.getClass().getName().contains(Constants.DYNAMIC_PROXY_ENHANCER_MARK));

        assertTrue(dynamic1.getClass().getName().contains(Constants.DYNAMIC_PROXY_ENHANCER_MARK));
        assertTrue(dynamic2.getClass().getName().contains(Constants.DYNAMIC_PROXY_ENHANCER_MARK));

        assertTrue(cglib1.getClass().getName().contains(Constants.SPRING_CGLIB_ENHANCER_MARK));
        assertTrue(cglib2.getClass().getName().contains(Constants.SPRING_CGLIB_ENHANCER_MARK));


        // При инъекции prototype в singleton может быть несколько неожиданный результат.
        // Если просто указать зависимость на prototype scoped бин, то заинжектится первый созданный инстанс и всё.
        // Для получения нового инстанса каждый раз при обращении к нему нужно использовать другие подходы
        // (инъекция через @Lookup, инъекция и вызов ObjectFactory<T>.getObject(), использование ApplicationContext, явное указание прокси, ....)
        var singleton = (SingletonService) applicationContext.getBean(SINGLETON_BEAN);
        assertEquals(singleton.getNoProxyRandomInt(), singleton.getNoProxyRandomInt());
        assertNotEquals(singleton.getCglibRandomInt(), singleton.getCglibRandomInt());
    }
}
