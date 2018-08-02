apiVersion: v1
kind: Secret
metadata:
  annotations:
    kubernetes.io/service-account.name: ${userName}
  name: ${tokenName}
  namespace: ${spaceName}
type: kubernetes.io/service-account-token