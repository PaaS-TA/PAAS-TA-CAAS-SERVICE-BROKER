apiVersion: v1
kind: ServiceAccount
metadata:
 name: ${userName}
 namespace: ${spaceName}
secrets:
  - name: ${userName}-token