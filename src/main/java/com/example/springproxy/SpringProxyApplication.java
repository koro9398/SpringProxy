package com.example.springproxy;

import com.example.springproxy.simpleservice.InterfacedService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class SpringProxyApplication {
    /**
     * Лямбда-реализация интерфейса. Из-за применённого аспекта и, собственно, реализации в виде лямбды,
     * будет проксирован с помощью Java Dynamic Proxy.
     *
     * @return InterfacedService
     * @see com.example.springproxy.aspect.SimpleServiceAspect
     */
    @Bean
    @Qualifier("LambdaInterfacedService")
    public InterfacedService interfacedService() {
        return () -> "LambdaService.someMethod() executed";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringProxyApplication.class, args);
    }
}
