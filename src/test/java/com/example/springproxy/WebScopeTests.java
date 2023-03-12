package com.example.springproxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebScopeTests {
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Заголовки запроса.
     */
    private HttpEntity<?> requestEntity;

    /**
     * Метод для выполнения пустого запроса, из которого можем вытащить данные сессии для последующих запросов.
     */
    @BeforeEach
    public void init() {
        ResponseEntity<Void> response = restTemplate.getForEntity("/scope/initSession", Void.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.COOKIE, response.getHeaders().get(HttpHeaders.SET_COOKIE));
        this.requestEntity = new HttpEntity<>(httpHeaders);
    }

    /**
     * Request-scope бин: независимо от наличия сессии на каждый новый запрос один и тот же прокси,
     * но новый конкретный объект, и, соответственно, разные значения
     */
    @Test
    void testRequestScope() {
        String beanName1;
        String beanName2;

        // Вне сессии
        // Проверка прокси
        beanName1 = getForObject("/scope/requestScopeBeanName", String.class);
        beanName2 = getForObject("/scope/requestScopeBeanName", String.class);
        assertEquals(beanName1, beanName2);
        assertTrue(beanName1.contains(Constants.SPRING_CGLIB_ENHANCER_MARK));
        assertTrue(beanName2.contains(Constants.SPRING_CGLIB_ENHANCER_MARK));

        // Проверка конкретного объекта
        assertNotEquals(
                getForObject("/scope/requestScopeValue", Integer.class),
                getForObject("/scope/requestScopeValue", Integer.class)
        );


        // Внутри одной сессии
        // Проверка прокси
        beanName1 = exchange("/scope/requestScopeBeanName", String.class);
        beanName2 = exchange("/scope/requestScopeBeanName", String.class);
        assertEquals(beanName1, beanName2);
        assertTrue(beanName1.contains(Constants.SPRING_CGLIB_ENHANCER_MARK));
        assertTrue(beanName2.contains(Constants.SPRING_CGLIB_ENHANCER_MARK));

        // Проверка конкретного объекта
        assertNotEquals(
                exchange("/scope/requestScopeValue", Integer.class),
                exchange("/scope/requestScopeValue", Integer.class)
        );
    }

    /**
     * Session-scope бин.
     * Вне сессии: один прокси, разные объекты, разные значения.
     * Внутри одной сессии: один прокси, один объект, одинаковые значения.
     */
    @Test
    void testSessionScopeWithinSession() {
        String beanName1;
        String beanName2;

        // Вне сессии
        // Проверка прокси
        beanName1 = getForObject("/scope/sessionScopeBeanName", String.class);
        beanName2 = getForObject("/scope/sessionScopeBeanName", String.class);
        assertEquals(beanName1, beanName2);
        assertTrue(beanName1.contains(Constants.SPRING_CGLIB_ENHANCER_MARK));
        assertTrue(beanName2.contains(Constants.SPRING_CGLIB_ENHANCER_MARK));

        // Проверка конкретного объекта
        assertNotEquals(
                getForObject("/scope/sessionScopeValue", Integer.class),
                getForObject("/scope/sessionScopeValue", Integer.class)
        );


        // Внутри одной сессии
        // Проверка прокси
        beanName1 = exchange("/scope/sessionScopeBeanName", String.class);
        beanName2 = exchange("/scope/sessionScopeBeanName", String.class);
        assertEquals(beanName1, beanName2);
        assertTrue(beanName1.contains(Constants.SPRING_CGLIB_ENHANCER_MARK));
        assertTrue(beanName2.contains(Constants.SPRING_CGLIB_ENHANCER_MARK));

        // Проверка конкретного объекта
        assertEquals(
                exchange("/scope/sessionScopeValue", Integer.class),
                exchange("/scope/sessionScopeValue", Integer.class)
        );
    }

    // Вызов exchange для передачи дополнительных заголовков.
    private <T> T exchange(String url, Class<T> clazz) {
        return restTemplate.exchange(url, HttpMethod.GET, this.requestEntity, clazz).getBody();
    }

    // Простой вызов getForObject.
    private <T> T getForObject(String url, Class<T> clazz) {
        return restTemplate.getForObject(url, clazz);
    }
}
