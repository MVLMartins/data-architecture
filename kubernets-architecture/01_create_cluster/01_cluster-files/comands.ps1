eksctl create cluster  --name data-k8s-cluster-poc  --version 1.21  --region us-east-1  --nodegroup-name data-k8s-workers-poc  --node-type t2.large  --nodes 3 

eksctl create cluster -f .\01_create_cluster\01_cluster-files\config.yaml