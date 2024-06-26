## Postman
Postman - это стандартный инструмент разработчиков, тестировщиков и даже аналитиков для тестирования API. Он нужен для выполнения заранее созданных запросов к тестируемому приложению. 

[Скачать тут](https://www.postman.com/downloads/)

## Импорт данных в Postman

Благодаря maven-плагину springdoc-openapi-maven-plugin, при комплияции проекта генерируется .json файл со спецификацией [OpenAPI](./guide_swagger.md) и помещается в корень репозитория. Этот файл необходимо импортировать в ваш Postman для создания коллекции запросов:
1) Откройте Postman, в верхней левой части окна найдите кнопку Import и нажмите на нее
2) Выберите файл из нашего репозитория **S7_Airlines_API.postman_collection.json**

Вы можете использовать эти запросы для тестирования кода, который вы написали, для ознакомления с работой приложения или для поиска багов.


### Аутентификация и авторизация с помощью JWT через Postman

Существует два основных подхода аутентификации пользователей: токены и сессии. 
- Сессии - это классический подход. Пользователь передает логин и пароль серверу, сервер проверяет полученные данные и сохраняет сессию либо в БД, либо In-memory (в оперативной памяти), пользователю возвращается Session ID - сгенерированная строка, которую пользователь передает в http-заголовке при последующих запросах к серверу, а сервер по этому Session ID ищет информацию о ранее залогиненном пользователе. В результате имеем stateful подход. Главный недостаток такого подхода в необходимости серверу хранить информацию о сессии, что может негативно сказаться на работе высоконагруженного приложения.
- Токены - более современных подход. Пользователь передает логин и пароль серверу, сервер проверяет полученные данные, но вместо сохранения сессии генерируется так называемый токен, который хранит в себе зашифрованную информацию о залогиненом пользователей. Пользователь сохраняет этот токен и использует в дальнейших запросах серверу. При этом сервер не хранит информацию о ранее выданном токене, а проверяет его подлинность в момент получения запроса без обращения к какому-либо хранилищу. Это stateless подход, и он позволяет более гибко и комфортно масштабировать высоконагруженные приложения. Основной реализацией такого подхода является JWT.

**JWT (JSON Web Token)** служит для безопасной передачи информации между двумя участниками (в нашем случае клиентом и сервером) [подробнее от JWT тут](https://struchkov.dev/blog/what-is-jwt/). Токен содержит:
- логин(email) и роль пользователя
- дату создания
- дату, после которой токен не валиден

Данные токена подписаны **HMAC-256** с использованием секретного ключа, известного только серверу аутентификации и серверу приложений(в нашем случае это одно приложение) - т.о. сервер приложений, при получении токена, сможет проверить не менялись ли его данные с момента создания.
1) Пользователь посылает логин/пароль серверу аутентификации `/api/auth/login`
2) Сервер аутентифицирует пользователя и возвращает ему **JWT**
3) К каждому запросу на сервер приложений пользователь прикрепляет **JWT** в `Header` вида ```Authorization: Bearer Token```
4) На основе **JWT** сервер приложений авторизует пользователя и предоставляет доступ к ресурсу (или не предоставляет - если пользователь не прошел авторизацию или время валидности токена истекло)

В продакшене наше приложение выглядит так. Все запросы пользователь делает на gateway модуль (8083 порт). Модуль анализирует, на какой endpoint, какого микросервиса клиент делает запрос и решает, пускать его или нет.



Аутентификация на сервере:
- зайти в **Postman**
- создать POST запрос на ```/api/auth/login``` _*(Напрямую на security модуль **8084** порт или через gateway **8083** порт)*_
- в качестве тела запроса выбрать raw -> JSON
- вписать username(email) и password в JSON и выполнить запрос <code>{"password": "admin", "username": "admin@mail.ru"}</code>
- Если аутентификация прошла успешно сервер вернет **JWT** в виде ```accessToken``` и ```refreshToken```.

Авторизация на сервере:
- Теперь, чтобы выполнить любой авторизированный запрос к серверу нужно в **header** запроса создать ключ **Authorization** и в качестве значения написать ```Bearer ``` + полученный ранее токен (Bearer обязательно с пробелом).
  или зайти во вкладку ```Authorization```, в выпадающем списке блока ```Type``` выбрать ```Bearer Token```, в ```Token``` вставить полученный ```accessToken```
- Обязательно делаем запросы через шлюз (_*8083 порт*_). Именно gateway решает, пускать на ресурс или нет, извлекая из токена роли пользователя. Подробнее о работе модуля ниже.
- Если время валидности токена истекло, тогда сделать POST запрос на ```/api/auth/token```
- вписать ```refreshToken``` и выполнить запрос
- сервер вернет ```accessToken```
- Чтобы получить новый ```accessToken``` и ```refreshToken``` необходимо сделать POST запрос на ```/api/auth/refresh```
- Если время валидности ```refreshToken``` токена истекло повторить процесс аутентификации

Подробная инструкция о реализации в нашем приложении [JWT тут](https://struchkov.dev/blog/jwt-implementation-in-spring/).

## Gateway модуль

Это шлюз, который переадресует пользователя на конкретный микросервис, в зависимости от пути запроса. Также здесь приложение авторизует пользователя.

### Маршрутизация

В `application.yml` декларативно описаны соответствия пути запроса, к адресу микросервиса, где реально находится контролер. Например:
```yaml
spring:
  cloud:
    gateway:
      default-filters:
      - AuthenticationFilter # Фильтр, который применится к запросу, после успешной маршрутизации. Подробнее об этом ниже.
      routes:
        - id: airline-project_accounts_api # id маршрута
          uri: http://localhost:8084 # На какой uri будет перенаправлен запрос
          predicates:
            - Path=/api/auth/** # Если в пути есть подстрока, отправит на этот маршрут 
```
Вспомним, что 8083 порт шлюза. Обращаясь на http://localhost:8083/api/auth, шлюз увидит, что нам нужен endpoint `/api/auth` и перенаправит запрос пользователя на http://localhost:8084/api/auth.

### Авторизация

В `routerValidatorConfig.yml` декларативно описаны открытые для не аутентифицированных пользователей пути и методы с которыми к ним можно обратиться:
```yaml
  openEndpoints:
    - uri: "/api/auth/token" # Открыт для всех методов этого endpoint'а, потому что поле `methods` не описано
    - uri: "/api/aircrafts/**" # Открыт для любых дочерних к /api/aircrafts/ endpoint'ов
      methods: # открыт только для перечисленных ниже методов
        - GET
        - POST 
```
А также защищенные endpoint'ы:
```yaml
  authorityEndpoints:
    ROLE_ADMIN: # Роль для которой открываем доступ
      - uri: "/admin"
      - uri: "/api/**"
    ALL_ROLES: # Здесь перечислены endpoint'ы открытые для любого аутентифицированного пользователя
      - uri: "/api/payments"
        methods:
          - POST
      - uri: "/api/example/**"
        methods:
          - POST
```

#### Как это работает?

После нахождения маршрута описанного в `application.yml`, запрос попадает в `AuthenticationFilter`. Далее все работает по блок-схеме:
![image](./images/AutenticationFilterScheme.png)

Чтобы клиенту приходил ответ с телом ошибки, бросается исключение `AuthorizationException(message, httpStatus)`, которое ловиться `GatewayExceptionHandler` - этакий аналог `@ControllerAdvice`, но из мира Webflux