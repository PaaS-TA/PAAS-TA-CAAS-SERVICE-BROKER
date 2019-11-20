apiVersion: v1
kind: Secret
metadata:
  name: ${secretName}
  namespace: ${spaceName}
data:
  .dockerconfigjson: ${configJson}
type: kubernetes.io/dockerconfigjson