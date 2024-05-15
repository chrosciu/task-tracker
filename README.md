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

# Kubernetes Dashboard

Jest to narzędzie umożliwiające zarządzanie klastrem Kubernetes za pomocą aplikacji webowej. 
Jako takie nie jest niezbędne do pracy z Kubernetesem, ale znacząco ją ułatwia, szczególnie dla osób początkujących.

## Instalacja

Najnowsza wersja dashboardu wymaga instalacji z użyciem managera pakietów Helm tak jak jest to opisane w dokumentacji:

[https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/)

Helm sam w sobie jest dość rozbudowanym narzędziem a jego wprowadzenie zajęłoby zbyt dużo czasu i zaciemniło cel warsztatów - dlatego też zainstalujemy nieco starszą wersję w sposób "klasyczny".
W tym celu użyjemy pliku deskryptora `k8s/kubernetes-dashboard.yml` - instalujemy go komendą:

```shell
kubectl apply -f k8s/kubernetes-dashboard.yml
```

Plik ten pochodzi ze starszego releasu narzędzia i dodatkowo jest zmodyfikowany przeze mnie tak, aby wyłączyć konieczność autoryzacji przy wejściu na dashboard.

Service wystawiany przez dashboard jest typu `ClusterIP` (domyślnego) co uniemożliwia dostęp do niego spoza klastra. 
Aby móc się z nim skomunikować niezbędne jest włączenie proxy pozwalającego na tymczasowy i kontrolowany dostęp do klastra: 

```shell
kubectl proxy
```

Nie podajemy portu, zatem będzie użyty domyślny (8001).

Teraz już możemy dostać się do dashboardu pod adresem:

[http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/workloads?namespace=default](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/workloads?namespace=default)

## Deinstalacja

```shell
kubectl delete -f k8s/kubernetes-dashboard.yml
```
