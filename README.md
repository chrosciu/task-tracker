# Docker

## Budowanie obrazu za pomocą Dockerfile

```shell
docker build -t chrosciu/task-tracker:1 .
```

## Sprawdzenie czy obraz się zbudował

```shell
docker image ls | grep chrosciu/task-tracker
```

## Uruchomienie zbudowanego obrazu

```shell
docker run -p 8080:8080 -e APP_DESCRIPTION='Docker run app' chrosciu/task-tracker:1
```

W tym trybie 
* kontener blokuje terminal
* logi z kontenera są wyrzucane na konsolę
* kombinacja Ctrl+C zatrzymuje kontener i odblokowuje terminal

## Uruchomienie w tle

```shell
docker run -d -p 8080:8080 -e APP_DESCRIPTION='Docker run app' chrosciu/task-tracker:1
```

## Sprawdzenie czy kontener jest uruchomiony

```shell
docker container ps | grep chrosciu/task-tracker
```

## Zatrzymanie kontenera

```shell
docker container stop <id> 
```

## Podglądanie logów kontenera

```shell
docker logs <id>
```

Lub też aby śledzić pojawiające się nowe linie:

```shell
docker logs -f <id>
```

## Wejście do konsoli uruchomionego kontenera

```shell
docker exec -it <id> sh
```

Przełączniki `-i` oraz `-t` są niezbędne aby proces się natychmiast nie zakończył

## Informacje na temat obrazu

```shell
docker image inspect chrosciu/task-tracker:1
```

## Informacje na temat kontenera

```shell
docker container inspect <id>
```

### Usunięcie kontenera

```shell
docker container rm <id>
```

## Usunięcie obrazu

```shell
docker image rm chrosciu/task-tracker:1
```

# Docker Hub

## Logowanie

```shell
docker login
``` 

Lub też za pomocą Docker Desktop (prostsze)

## Wylogowanie

```shell
docker logout
```

Lub też za pomocą Docker Desktop (prostsze)

## Wypchnięcie obrazu

```shell
docker push chrosciu/task-tracker:1
```

# Docker compose

## Tworzenie pliku deskryptora

Można zrobić to na dwa sposoby:
* ręcznie (co nie jest specjalnie trudne i w większości przypadków wystarcza)
* automatycznie za pomocą aplikacji webowej `composerize` [https://www.composerize.com/](https://www.composerize.com/)

## Tworzenie kontenerów + ich uruchamianie 

```shell
docker compose up
```

Polecenie szuka w bieżącym katalogu pliku docker-compose.yml i wczytuje definicje kontenerów w nim zawarte. 
Można także wskazać plik bezpośrednio opcją `-f`
Wersja z przełącznikiem `-d` z kolei powoduje uruchomienie kontenerów w tle

## Zatrzymanie kontenerów + ich zniszczenie

Jeżeli kontenery były uruchomione bez opcji `-d` to wystarczy kombinacja Ctrl+C.
W przeciwnym razie:

```shell
docker compose down
```

### Sprawdzenie tego co zostało uruchomione

```shell
docker compose ps
```