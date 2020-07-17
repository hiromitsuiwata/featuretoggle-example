NAME := myapp
VERSION := 1.0

build:
		mvn clean package;
		docker build -t $(NAME):$(VERSION) .

apply:
		kubectl apply -f deployment.yaml

delete:
		kubectl delete -f deployment.yaml

rmi:
		docker rmi $(NAME):$(VERSION)
		# noneになったイメージは削除する
		docker image prune -f
