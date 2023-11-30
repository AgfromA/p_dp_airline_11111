# Changelog

## [Unreleased]


- #332 Можно отправить во фронт информативное сообщение об ошибке поиска в одну строку: throw new SearchRequestException(errorMessage, HttpStatus.BAD_REQUEST);
- #282 Добавлен View для Aircraft(в т.ч. клиент feign). DTO и API перенесено в Common. DTO теперь использует Mapper. Методы createAircraft() и updateAircraft() в API Теперь возвращает DTO вместо Entity;
- #327 Рефакторинг Security. Аутентифкация на стороне security модуля, авторизация на стороне gateway
- #282 Добавлен View для Aircraft(в т.ч. клиент feign). DTO и API перенесено в Common. DTO теперь использует Mapper. Методы createAircraft() и updateAircraft() в API Теперь возвращает DTO вместо Entity
- #308 Добавлен сервис аутентификации через Google ("airline-oauth"), добавлена кнопка "Вход с google" во view. Работа сервиса описана в README oauth
- #292 Добавлен view для timezone(в т.ч. клиент feign), исправлен вызов delete метода в интерфейсе рест контроллера (в common). Переписан контроллер для Timezone, перенесены example и exampleMapper в project
- #296 Добавлен Mapper через MupStruck, Изменены зависимости в классе PassengerServiceImpl (логика в методах класса не менялась), Удален класс PassengerMapper, Добавлен unit тест для PassengerMapper (тест проходит успешно)
- #223 Настроена и синхронизированна секьюрность в payments с помощью JWT токена, создана сущность Test для проверки.
- #268 добавлен API Gateway
- #231 Добавлены поля CreateTime, BookingStatus, FlightSeat, удалены поля Flight и Category, исправлена соответствующая логика и тесты. Добавлен щедуллер для booking.
- #296 mapstruct for FlightSeat 
- #302 Добавлен Mapper для Timezone через Mapstruct
- #300 mapper через mapstruct для Seat
- #290 Фронт CRUD для SeatDTO, класс SeatDTO исправлен (удален Seat из конструктора), SeatDTO-SeatRestApi-CategoryType перенесены в common
- #189 Добавлена библиотека GeographicLib-Java, добавлен метод для изменения цены FlightSeat в зависимости от расстояния полета
- #229 Произведен рефакторинг PassengerRestController, добавлена логика фильтрации по firstName + lastName, оптимизированы тесты
- #255 Произведен рефакторинг SearchController
- #264 Паспорт сделан обязательным (not null) для PassengerDTO
- #320 Фронт стартовой страницы с поиском
- #321 Фронт для поиска свободных мест и отображения результатов, рефакторинг FlightDTO, FlightSeat, Search, SearchResult
- #321 Рефакторинг DestinationDTO, FlightSeatDTO, фронт Destination, обработка FeignException во фронте
- #286 Добавлен фронт для Flight

## [1.0.7] - 8.09.2023
- #277 Обновлены версии зависимостей в pom файлах и исправлены конфликты.
- #237 Исправлен баг с редактированием Ticket. Убраны аннотации @Data из проекта.
- #257 Переход AccountMapper, BookingMapper на Mapstruct , а также junit тесты к ним.
- #258 Добавлена автоматическая конфигурация OpenApi при запуске приложения.
- #259 в BookingDto поле bookingNumber доступно теперь только для чтения, генерируется в сервисе, удалены тесты в BookingRestControllerIT на валидацию этого поля. bookingData переименовано на bookingDate
- #260 Сделана пагинация во всех контроллерах использующих метод findAll.
- #262 В update тестах добавлена проверка, что количество сущностей не изменилось после обновления, поправлен тест shouldCreateManySeats.
- #263 Смена названий методов контроллеров и сервисов
- #no_task - added vaadin
- #278 Перенос мапперов из контроллера в сервис.
- #285 Фронт для destination
- #306 Добавлен новый микросервис с фронтом(Vaadin), ExampleDTO и ExampleApi перенесен в common. Весь фронт был перенесен на отдельный сервис в airline-frontend.
- #236 Исправлен баг UpdateFlightById, справлен FlightMapper (удален прежний, добавлен FlightMapper интерфейс с использованием MapStruct), написаны unit есты для FlightMapper, интеграционные тесты для FlightController (updateFlightById)
- #294 Добавлен Mapper для Destination через Mapstruct
- #298 Добавлен Mapper для Payment через Mapstruct, а также junit тесты к ним.

## [1.0.6] - 30.07.2023
- #226 Рефакторинг passenger и account - убрано наследование

## [1.0.5] - 19.07.2023
- #230 Мягкое удаление Destination
- #247 Исправлен баг, если передать в convertToTicketEntity несуществующие flightId/flightSeatId/passengerId вылетает NPE
- #244 Замена Put на Patch в Passenger. Новый update в PassengerServiceImpl. null поля в DTO игнорируются. Проверены апишки на Patch методы. Скорректированы зависимости, контроллер и тест.
- #243 Исправлен баг, в Patch Destination теперь возвращается обновленная сущность
- #246 Настройки переменных окружения перенесены из airline-deployment в airline-configmap
- #245 Добавлена configMap для payments.
- #228 рефакторинг метода generate в SeatServiceImpl + тест + в pom.xml убраны дублированные зависимости
- #240 Рефакторинг FlightSeatDTO замена seatId на seatNumber. Поправил FlightSeatDTOTest.
- #239 Добавлена JWT авторизация в SWAGGER. По логину и паролю получаем access и refresh токены. По кнопке Authorize делаем авторизацию c access.
- #252 замена локальных переменных на var + правка () названий переменных
- #251 Исправлена ошибка в enum SeatsNumbersByAircraft. В методе getNumbersOfSeatsByAircraft: заменила  replace() на  replaceAll() + регулярка.

## [1.0.4] - 23.06.2023
- #241 - Исправлен баг с id ticket'а при post запросе. Теперь при неправильных id билета, рейса и пассажира возвращает 400 ошибку вместо 500
- #195 - В configMap настроено обращение к airline-payments при деплое в кубере
- #159 - В deployments kubernetes'а добавлена configMap, хранящая application.yml от airline-project'а при деплое в кубере.
- #221 - фильтры по городам и датам для метода получения всех полетов
- #225 - Отключние секьюрити для профиля noSecurity и обновление гайда по Postman
- #235 - исправлены ошибки пароля и паспорта. Теперь паспорт отображается в JSON корректно, пароль отвечает заданным требованиям 
- #215 Timezone CRUD и интеграционные тесты к ней
- #218 Добавлена пагинация, для Ticket как в других контроллерах
- #242 Фикс ticket API. В тело ответа добавлен id. Добавлена проверка id на null в TicketRestControllerIT

## [1.0.3.hf2] - 14.06.2023
- #238 - фикс странички логина. Можно снова заходить по /login, а не /login.html

## [1.0.3.hf1] - 14.06.2023
- #- Фикс редиректа на форму логина для неавторизованных запросов
- #181 Удаленный дебаг airline_project
- #187 Readiness и Liveness пробы для airline-payment
- #221 Фильтры для получения всех Flight
- #219 Рефакторинг FlightSeat API: добавлены фильтры для поиска всех FlightSeat, добавлен эндпоинт для получения всех непроданных FlightSeat
- #224 Тесты для обработки исключений
- #209 Тесты FlightSeatRestController и SeatRestController
- #208 Объединение фильтров для поиска всех Passenger

## [1.3.0] - 03.06.2023

- #166 Временно убрано удаление Destination
- #220 Рефакторинг создания Passenger
- #210 Обработка исключений
- #214 Добавлен фильтр по timezone для получения всех Destination 
- #213 Правки секурити: увеличение времени жизни jwt токена, убран редирект на страницу логина в случе протухания токена
- #212 Рефакторинг FlightDTO, в теле запроса вместо Destination теперь Airport
- #211 Рефакторинг получения самых дешевых сидений в своей категории

## [1.2.0] - 22.05.2023

- #196 Фикс пагинации для всех контроллеров

## [1.1.0] - 19.05.2023

- #193 Фикс удаления Passenger
- #205 Фикс удаления Flight
- #206 Фильтры для поиска всех Destination
- #200 Рефакторинг Search, возвращение одной сущности с вложенными массивами
- #191 Фикс создания Booking
- #188 Начальная настройка Paypal
- #199 Фикс получения Seat
- #174 Возможность обращения airline-project в airline-payments

## [1.0.0]

- Одному Богу известно, что тут было