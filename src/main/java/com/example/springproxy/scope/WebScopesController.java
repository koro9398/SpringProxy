package com.example.springproxy.scope;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/scope")
public class WebScopesController {
    private final RequestService requestService;
    private final SessionService sessionService;
    private final ScopeBean requestInterfacedService;
    private final ScopeBean sessionInterfacedService;

    public WebScopesController(
            RequestService requestService,
            SessionService sessionService,
            @Qualifier("RequestInterfacedService") ScopeBean requestInterfacedService,
            @Qualifier("SessionInterfacedService") ScopeBean sessionInterfacedService
    ) {
        this.requestService = requestService;
        this.sessionService = sessionService;
        this.requestInterfacedService = requestInterfacedService;
        this.sessionInterfacedService = sessionInterfacedService;
    }

    @GetMapping("/initSession")
    public ResponseEntity<HttpStatus> getSessionObject(HttpSession httpSession) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requestScopeValue")
    public int request() {
        return requestService.getGeneratedNumber();
    }

    @GetMapping("/requestScopeBeanName")
    public String requestBeanName() {
        return requestService.getClass().getName();
    }

    @GetMapping("/sessionScopeValue")
    public int session() {
        return sessionService.getGeneratedNumber();
    }

    @GetMapping("/sessionScopeBeanName")
    public String sessionBeanName() {
        return sessionService.getClass().getName();
    }


    @GetMapping("/requestInterfacedScopeValue")
    public int requestInterfaced() {
        return requestInterfacedService.getGeneratedNumber();
    }

    @GetMapping("/requestInterfacedScopeBeanName")
    public String requestInterfacedBeanName() {
        return requestInterfacedService.getClass().getName();
    }

    @GetMapping("/sessionInterfacedScopeValue")
    public int sessionInterfaced() {
        return sessionInterfacedService.getGeneratedNumber();
    }

    @GetMapping("/sessionInterfacedScopeBeanName")
    public String sessionInterfacedBeanName() {
        return sessionInterfacedService.getClass().getName();
    }
}
