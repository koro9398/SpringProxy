### Изучение кейсов по созданию и проверке GLIB/Dynamic прокси.<br/>
### Вопросы, которые рассматривались:
1. Как и в каких случаях создаются прокси;
   * cross-cutting-concern, дополнительное управление созданием экземпляра, влияние на код в runtime.
2. Как они выглядят в runtime, как отличить тип прокси;
   * у них разный суффикс: `$$EnhancerByCGLIB` vs `$Proxy`.
3. Как с ними работать.
   * общая идея в том, чтобы они были неотличимы от оригинала, так что можно выполнять все те же операции, что и над исходным объектом.


* ### Простые сервисы и data-репозитории (`CatServiceTests`):
1. Простой `@Service` просто так не проксируется;
2. `@Service` + `@Transactional` = CGLIB proxy;
3. Data-репозиторий реализуется с помощью Java Dynamic proxy.

* ### Прокси, создаваемые с помощью аспектов (`SimpleServiceTests`):
1. Бин, не реализующий интерфейсы, проксируется CGLIB;
2. Бин, реализующий интерфейсы, тоже всё равно проксируется CGLIB;
3. Бин, объявленный в виде лямбды и `@Bean` проксируется Dynamic Proxy.
4. Дополнительная информация: `org.springframework.aop.framework.DefaultAopProxyFactory#createAopProxy`

* ### Singleton- и Prototype-scoped бины (`ScopeTests`):
1. Сами по себе не проксируются;
2. Singleton можно проксировать, но бессмысленно;
3. Для Prototype тоже используется другой подход, но можно проксировать.
4. До какой-то версии спринга нельзя было проксировать Singleton и Prototype, сейчас не используется, но допустимо.

* ### WebContext scope (`WebScopeTests`):
1. Request- и Session-scoped бины по умолчанию должны использовать проксирование, специальные аннотации
используют `proxyMode = ScopedProxyMode.TARGET_CLASS`.

* ### Созданные вручную прокси двух типов (`ManualProxyTests`):
1. Dynamic proxy: `Proxy` + `InvocationHandler`
2. CGLIB реализуется с помощью `Enhancer` + `MethodInterceptor`
3. Свои собственные CGLIB-прокси немного отличаются от тех, что создаёт спринг: `$$EnhancerByCGLIB` vs `$$EnhancerBySpringCGLIB` 
