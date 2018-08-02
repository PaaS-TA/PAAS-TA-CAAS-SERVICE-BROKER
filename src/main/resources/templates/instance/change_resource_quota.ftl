apiVersion: v1
kind: ResourceQuota
metadata:
  name: ${quotaName}
spec:
  hard:
    limits.cpu: "${plan.cpu}"
    limits.memory: ${plan.memory}
    requests.storage: ${plan.disk}