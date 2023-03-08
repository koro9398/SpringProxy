package com.example.springproxy;

import com.example.springproxy.catservice.CatService;
import com.example.springproxy.catservice.CatServiceTransactionalImpl;
import com.example.springproxy.repository.CatRepository;
import com.example.springproxy.catservice.CatServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.springproxy.Constants.SPRING_CGLIB_ENHANCER_MARK;
import static com.example.springproxy.Constants.DYNAMIC_PROXY_ENHANCER_MARK;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CatServiceTests {
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
     * {@code catServiceTransactional} - проксируется CGLIB из-за применённых транзакций.<br/>
     * {@code repository} - проксируется Java Dynamic Proxy, так как это в принципе только интерфейс.
     */
    @Test
    public void testCats() {
        // Проверяем типы прокси
        assertFalse(catService.getClass().getName().contains(SPRING_CGLIB_ENHANCER_MARK));
        assertFalse(catService.getClass().getName().contains(DYNAMIC_PROXY_ENHANCER_MARK));
        assertTrue(catServiceTransactional.getClass().getName().contains(SPRING_CGLIB_ENHANCER_MARK));
        assertTrue(repository.getClass().getName().contains(DYNAMIC_PROXY_ENHANCER_MARK));

        // Проверяем, что прокси вполне себе соотносятся с исходным классом
        assertTrue(catService instanceof CatService);
        assertTrue(catService instanceof CatServiceImpl);
        assertTrue(catServiceTransactional instanceof CatService);
        assertTrue(catServiceTransactional instanceof CatServiceTransactionalImpl);
        // Тут у нас есть только интерфейс
        assertTrue(repository instanceof CatRepository);
    }
}
