package com.example.springproxy;

import com.example.springproxy.catservice.CatService;
import com.example.springproxy.catservice.CatServiceTransactionalImpl;
import com.example.springproxy.repository.CatRepository;
import com.example.springproxy.catservice.CatServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.springproxy.Constants.SPRING_CGLIB_ENHANCER_MARK;
import static com.example.springproxy.Constants.DYNAMIC_PROXY_ENHANCER_MARK;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс тестов, аналогичный {@link CatServiceTests}, но с параметром {@code spring.aop.proxy-target-class=false}.<br/>
 * Без него классы, реализующие интерфейс, проксируются с помощью CGLIB. Явная установка параметра в false отключает
 * поведение по умолчанию, и такие классы проксируются с помощью Java Dynamic Proxy.
 */
@SpringBootTest
@ActiveProfiles("aopFalse")
public class CatServiceParamFalseTests {
    @Autowired
    @Qualifier("CatServiceImpl")
    private CatService catService;

    @Autowired
    @Qualifier("CatServiceTransactionalImpl")
    private CatService catServiceTransactional;

    @Autowired
    private CatRepository repository;

    /**
     * Проверка спринговых прокси.<br/>
     * {@code catService} - не будет проксироваться, так как нет дополнительной логики.<br/>
     * {@code catServiceTransactional} - проксируется Java Dynamic Proxy из-за применённых транзакций и параметра aop.<br/>
     * {@code repository} - проксируется Java Dynamic Proxy, так как это в принципе только интерфейс.
     */
    @Test
    public void testCats() {
        // Проверяем типы прокси
        assertFalse(catService.getClass().getName().contains(SPRING_CGLIB_ENHANCER_MARK));
        assertFalse(catService.getClass().getName().contains(DYNAMIC_PROXY_ENHANCER_MARK));
        // С spring.aop.proxy-target-class=false получаем динамический прокси, а не CGLIB
        assertTrue(catServiceTransactional.getClass().getName().contains(DYNAMIC_PROXY_ENHANCER_MARK));
        assertTrue(repository.getClass().getName().contains(DYNAMIC_PROXY_ENHANCER_MARK));

        // Проверяем, что прокси вполне себе соотносятся с исходным классом
        assertTrue(catService instanceof CatService);
        assertTrue(catService instanceof CatServiceImpl);
        assertTrue(catServiceTransactional instanceof CatService);
        // С spring.aop.proxy-target-class=false получаем динамический прокси,
        // а значит не пройдём проверку типов на конкретный класс
        assertFalse(catServiceTransactional instanceof CatServiceTransactionalImpl);
        // Тут у нас есть только интерфейс
        assertTrue(repository instanceof CatRepository);
    }
}
