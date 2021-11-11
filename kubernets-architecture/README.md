# Introduction

This documentation has been written to create an implementation of data pipeline using kubernets.

Note: the comands above has been written in Windows Powershell. For replication purposes, we recomend to use the same terminal.


# Create Kubernetes Cluster 

``` 
eksctl create cluster  --name data-k8s-cluster-poc  --version 1.21  --region us-east-1  --nodegroup-name data-k8s-workers-poc  --node-type t2.large  --nodes 3 
```

# Prepare namespaces
```
kubectl apply -f .\01_create_cluster\02_namespaces-files\.
```


# Prepare ingress aws load
```
curl -o iam_policy.json https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/v2.3.0/docs/install/iam_policy.json

aws iam create-policy --policy-name AWSLoadBalancerControllerIAMPolicy --policy-document file://iam_policy.json

eksctl utils associate-iam-oidc-provider --cluster data-k8s-cluster-poc --approve

eksctl create iamserviceaccount --cluster=data-k8s-cluster-poc --namespace=kube-system --name=aws-load-balancer-controller --attach-policy-arn=arn:aws:iam::235494797394:policy/AWSLoadBalancerControllerIAMPolicy --override-existing-serviceaccounts --approve

helm repo add eks https://aws.github.io/eks-charts

helm repo update

helm install aws-load-balancer-controller eks/aws-load-balancer-controller -n kube-system --set clusterName=data-k8s-cluster-poc --set serviceAccount.create=false --set serviceAccount.name=aws-load-balancer-controller
```
# airflow
```
helm repo add apache-airflow https://airflow.apache.org

helm repo update

helm install airflow apache-airflow/airflow --namespace orchestration
```

# Delete cluster
```
eksctl delete cluster --name data-k8s-cluster-poc
```

