minikube start
rem останавливаем существующие поды приложения, чтобы впоследствии закешировать актуальный образ
kubectl --namespace default scale deployment airline-project-deployment --replicas 0
kubectl --namespace default scale deployment airline-payments-deployment --replicas 0
rem приостанавливаем выполнение скрипта на 10 секунд, чтобы контейнер успел остановиться
timeout /t 15
rem удаляем закешированный в миникубе образ приложения (airline-project)
rem если на этом шаге в логах видите ошибку, попробуйте увеличить время на предыдущем шаге
minikube image rm airline-project
rem загружаем актуальный образ приложения в кеш миникуба из локального репозитория (airline-project)
minikube image load airline-project
rem удаляем закешированный в миникубе образ приложения (airline-payments)
minikube image rm airline-payments
rem загружаем актуальный образ приложения в кеш миникуба из локального репозитория (airline-payments)
minikube image load airline-payments
rem запускаем поду приложения с новым образом
kubectl --namespace default scale deployment airline-project-deployment --replicas 1
kubectl --namespace default scale deployment airline-payments-deployment --replicas 1
minikube dashboard