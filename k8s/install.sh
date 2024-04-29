kubectl apply -f namespace.yaml
kubectl apply -f secret.yaml
kubectl apply -f configmap.yaml
kubectl apply -f clients/deployment.yaml
kubectl apply -f clients/service.yaml