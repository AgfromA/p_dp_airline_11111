## Security приложения
Security отвечает за аутентификацию и выдачу JWT токена.\
Gateway за авторизацию.

### Security
POST запрос на <code>/api/auth/login</code> с телом: \

```json
{
    "username": username,
    "password": password
}
```


В ответ приходит токен. Этот токен помещаем в заголовки запросов к gateway на порт 8083:
`Authorization: Bearer <token>`

### Gateway
Смотрит на endpoint запроса и решает, пропускать пользователя или нет.


#### Структура routerValidatorConfig.yml
Здесь конфигурируются права доступа
```yaml
router-validator: 
    openEndpoints:
        - uri: "/path_to_endpoint"
          methods:
            - HttpMethod1
            - HttpMethod2
    authorityEndpoints:
      ROLE_ADMIN:
        - uri: "/admin"
          methods:
            - GET
        - uri: "/api/**"
      All_ROLES: #пускает любого аутентифицированного пользователя
        - uri: "/api/payments"
          methods:
            - POST
```
Где ```methods``` - не обязательный параметр.\


