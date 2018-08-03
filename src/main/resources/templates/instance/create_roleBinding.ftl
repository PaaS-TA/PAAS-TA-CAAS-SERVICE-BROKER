apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: ${roleName}-binding
  namespace: ${spaceName}
subjects:
- kind: ServiceAccount
  name: ${userName}
roleRef:
  kind: Role
  name: ${roleName}
  apiGroup: rbac.authorization.k8s.io
