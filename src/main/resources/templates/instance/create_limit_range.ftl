apiVersion: v1
kind: LimitRange
metadata:
  name: ${rangeName}
spec:
  limits:
  - default:
      cpu: 200m
      memory: 1Gi
    type: Container
