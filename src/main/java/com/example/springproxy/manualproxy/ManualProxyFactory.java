package com.example.springproxy.manualproxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Ручное создание прокси двух типов.
 */
@Component
public class ManualProxyFactory {
    private static final String INTERESTED_METHOD = "someMethod";

    public ManualProxy getCglibProxy() {
        var target = new ManualProxyImpl();
        return (ManualProxy) Enhancer.create(ManualProxyImpl.class, new CglibInterceptor(target));
    }

    public ManualProxy getDynamicProxy() {
        var classLoader = ManualProxy.class.getClassLoader();
        var target = new ManualProxyImpl();
        return (ManualProxy) Proxy.newProxyInstance(classLoader, target.getClass().getInterfaces(), new DynamicHandler(target));
    }


    private static class CglibInterceptor implements MethodInterceptor {
        private final Object target;

        private CglibInterceptor(Object target) {
            this.target = target;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            var result = proxy.invoke(target, args);
            if (INTERESTED_METHOD.equals(method.getName())) {
                return "Cglib Proxy: " + result;
            }
            return result;
        }
    }

    private static class DynamicHandler implements InvocationHandler {
        private final Object target;

        DynamicHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            var result = method.invoke(target, args);
            if (INTERESTED_METHOD.equals(method.getName())) {
                return "Dynamic Proxy: " + result;
            }
            return result;
        }
    }
}
