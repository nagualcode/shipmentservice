#!/bin/bash

kubectl apply -f postgres-deployment.yaml
kubectl apply -f userservice-deployment.yaml
kubectl apply -f trackingservice-deployment.yaml
kubectl apply -f flyway-job.yaml

