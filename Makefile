build:
	gradle clean bootJar
	docker build --rm -f Dockerfile -t alevalv/retipy-rest:latest .
start:
	docker run --name retipy-rest -d -p 80:80 alevalv/retipy-rest:latest
pause:
	docker stop retipy-rest
resume:
	docker start retipy-rest
stop:
	docker stop retipy-rest
	docker rm retipy-rest
clean:
	if [ -d "build" ]; then rm -r build; fi
	docker rmi alevalv/retipy-rest:latest
