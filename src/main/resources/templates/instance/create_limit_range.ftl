apiVersion: v1
kind: LimitRange
metadata:
  name: ${rangeName}
spec:
  limits:
  - default:
      cpu: 300m
      memory: 200Mi
    type: Container
