Чтобы в логах видеть id запроса от клиента по всем микросервисам, добавьте модулю зависимость:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

Если логирование сломалось, проверьте, нет ли в проблемном модуле зависимости
`spring-boot-starter-logging`. Библиотека должна быть только в `common` модуле. Именно он отвечает за шаблон логирования во всем приложении, и конфигурируется в файле `log4j2.yml`.

Исключить библиотеку из нужного стартера можно, например так:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```