Накосячил с Liquibase, что делать?
- Удали все таблицы (вариант не очень грамотный, но самый простой). В терминале выполните команду <code>docker exec -it postgreS7 psql -h localhost -p 5432 -U root -W -d airline_db</code>, 
- введите пароль root  и нажвите enter, выполните команду <code>\dt</code>, чтобы увидеть все существующие таблицы. 
- Для манипулирования таблицами используйте стандартный SQL-синтаксис. 
- Чтобы удалить все таблицы, выполните команду <code>DROP SCHEMA public CASCADE;</code>, затем <code>CREATE SCHEMA public;</code>

Не работает FrontendApplication (airline-frontend http://localhost:8082/)?
- Отображается ошибка, в конце стектрейса <code>Caused by: java.util.zip.ZipException: zip END header not found</code>. У вас по какой-то неведомой причине не скачался артефакт, необходимый Vaadin'у для генерации фронта. Подложите его самостоятельно по пути <code>C:\Users\\{Ваш_Юзер}.vaadin\node-v18.16.0-win-x64.zip</code> (.vaadin - это скрытая папка), попросите коллегу отправить вам этот файл
- Проверить путь до проекта. В нем не должно быть кирилицы. Пример:
  C:\Users\pro10\Desktop\Проект\p_dp_airline_1   (Не верно)
  C:\Users\pro10\Desktop\Project\p_dp_airline_1  (Верно)

Ошибки при компиляции <code>cannot access...</code> или <code>cannot find symbol</code>
- Если не помогает <code>mvn clean install</code> корневого pom, попробуйте удалить папку .idea, затем в интерфейсе Идеи File -> Invalidate Caches...

Что делать, если в feign клиенте ошибка java.lang.IllegalStateException: PathVariable annotation was empty on param 0?
- Проверьте методы в Controllers. В аннотациях должно стоять имя параметра. Вот так: (@PathVariable("id") Long id);