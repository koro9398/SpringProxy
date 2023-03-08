package com.example.springproxy.manualproxy;

public class ManualProxyImpl implements ManualProxy {
    @Override
    public String someMethod() {
        return this.getClass().getName();
    }

    public String anotherMethod() {
        return this.getClass().getName();
    }
}
