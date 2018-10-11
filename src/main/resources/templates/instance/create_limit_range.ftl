apiVersion: v1
kind: LimitRange
metadata:
  name: ${rangeName}
spec:
  limits:
  - default:
      cpu: 100M
      memory: 500Mi
    type: Container
