apiVersion: v1
kind: ServiceAccount
metadata:
 name: ${userName}
 namespace: ${spaceName}
automountServiceAccountToken: false