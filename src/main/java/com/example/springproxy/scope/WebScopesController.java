package com.example.springproxy.scope;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/scope")
@RequiredArgsConstructor
public class WebScopesController {
    private final RequestService requestService;
    private final SessionService sessionService;

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
}
