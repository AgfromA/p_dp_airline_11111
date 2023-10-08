
echo off
minikube start
docker context use default

kubectl --namespace default scale deployment airline-project-deployment --replicas 0
kubectl --namespace default scale deployment airline-payments-deployment --replicas 0
timeout /t 5
cd I:\MyProjectJIDEA\p_dp_airline_1
docker build -f Dockerfile_Project -t airline-project .
docker build -f Dockerfile_Payments -t airline-payments .
minikube image load airline-project --overwrite
minikube image load airline-payments --overwrite
kubectl --namespace default scale deployment airline-project-deployment --replicas 1
kubectl --namespace default scale deployment airline-payments-deployment --replicas 1
minikube dashboard