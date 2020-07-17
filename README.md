# featuretoggle-example

フィーチャー・トグルの実現例

## 検証環境

- Docker 19.03, Minikube 1.12.0
- Java 8 以降, Maven 3.6.3
- make 3.81

## ビルド・実行方法

```bash
# Docker ServerをMinikubeにする
eval $(minikube -p minikube docker-env)
# Dockerイメージのビルド
# Dockerレジストリを使わずローカルイメージを用いる. imagePullPolicy: Alwaysは不可
make build
# Kubernetesのyamlを適用しDeployment, Service, Ingressを作成する
make apply
# リクエスト送信
curl http://$(minikube ip)/mypath/MyServlet
```
