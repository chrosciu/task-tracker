# Docker

## Budowanie obrazu za pomocą Dockerfile

```shell
docker build -t chrosciu/task-tracker:1 .
```

## Budowanie obrazu za pomocą narzędzia Jib (wykorzystuje plugin do Mavena)

```shell
mvn clean compile jib:dockerBuild
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

# Kubernetes

## Instalacja deploymentu

```shell
kubectl apply -f k8s/task-tracker-deployment.yml
```

Aby sprawdzić czy deployment i pody są widoczne:

```shell
kubectl get deployments
```

```shell
kubectl get pods
```

## Przejrzenie logów z poda

```shell
kubectl logs <pod-id>
```

Z przełącznikiem `-f` można śledzić log w trybie ciągłym

## Wyświetlenie informacji na tematów deploymentów i podów

```shell
kubectl describe deployments
```

```shell
kubectl describe pods
```

## Udostępnienie portu z poda

Domyślnie pod jest widoczny tylko w klastrze. 
Można go wystawić za pomocą serwisu, ale czasami prościej jest "na szybko" sforwardować tymczasowo port poda (np. w celu debugowania)

```shell
kubectl port-forward pod/<pod-id> 8080
```

W efekcie nasza aplikacje będzie tymczasowo dostępna na porcie 8080

## Instalacja serwisu

```shell
kubectl apply -f k8s/task-tracker-service.yml
```

Sprawdzenie czy jest widoczny:

```shell
kubectl show services
```

Jeśli wszystko gra, to w tym momencie nasza aplikacja powinna być wystawiona trwale na porcie 8080

## Wyświetlenie informacji o serwisie

```shell
kubectl describe services
```

## Skalowanie ilości podów

```shell
kubectl scale --replicas=3 deployment task-tracker
```

## Usunięcie zasobów

```shell
kubectl delete -f k8s/task-tracker-service.yml
```

```shell
kubectl delete -f k8s/task-tracker-deployment.yml
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

## Wyświetlenie dostępnych kontekstów

Narzędzie `kubectl` może pracować w wielu kontekstach - standardowo zaczynamy od jednego (Docker Desktop).
Kolejny kontekst będzie reprezentował klaster na GKE (Google Kubernetes Engine) - dodajemy go za pomocą narzędzia gcloud.
Dokładna składnia będzie do odczytania w panelu sterowania GKE.

Konteksty wyświetlamy następująco:

```shell
kubectl config get-contexts
```

## Przełączanie się między kontekstami

```shell
kubectl config use-context <context-id>
```

# Google Kubernetes Engine

## Włączanie logowania

Z niewiadomych przyczyn klaster nie ma włączonej domyślnie opcji zbierania logów na dashboardzie co utrudnia debugowanie ewentualnych problemów na podach.

By włączyć logowanie należy wydać polecenie:

```shell
gcloud services enable logging.googleapis.com
```

Następnie można sprawdzić czy logowanie jest faktycznie włączone:

```shell
gcloud services list --enabled --filter="NAME=logging.googleapis.com"
```

# Kubernetes - rolling update

## Wdrożenie nowej wersji kontenera w ramach deploymentu

```shell
kubectl set image deployment/task-tracker task-tracker=chrosciu/task-tracker:2
```

Można również:
* przeedytować plik `k8s/task-tracker-deployment.yml` i wykonać `kubectl apply -f k8s/task-tracker-deployment.yml`
* przeedytować deskryptor deploymentu w Kubernetes Dashobard - po zapisaniu nastąpi redeploy

## Sprawdzenie historii wdrożeń dla danego deploymentu

```shell
kubectl rollout history deployment/task-tracker
```

## Wycofanie ostatniego wdrożenia

```shell
kubectl rollout undo deployment/task-tracker
```

## Cofnięcie do konkretnej wersji wdrożenia

```shell
kubectl rollout undo deployment/task-tracker --to-revision=1
```

## Listing wszystkich eventów w ramach klastra

Posortowane od najstarszego, pozwala na inwestygację jeśli coś nie działa

```shell
kubectl get events --sort-by=.metadata.creationTimestamp
```

# Baza danych z użyciem kontenerów

Aby postawić bazę danych na kontenerze niezbędne jest poradzenie sobie z dwoma wyzwaniami:
* sieć - kontenery muszą się nawzajem widzieć (domyślnie bowiem każdy kontener działa w izolowanej sieci)
* wolumeny - kontener musi mieć możliwość utrwalania danych tak aby przeżyły one jego restart

## Tworzenie sieci

```shell
docker network create task-tracker-net
```

Stworzona sieć jest domyślnego typu (bridge). Będzie można do niej dołączać kontenery - będą się one nawzajem widzieć (po nazwie)

## Lista sieci i informacje o nich

```shell
docker network ls
```

```shell
docker network inspect task-tracker-net
```

## Tworzenie wolumenu

```shell
docker volume create task-tracker-data
```

Stworzony wolumen będzie dostępny w ramach systemu plików Dockera. W systemie Windows jest to ścieżka: `\\wsl$\docker-desktop-data\data\docker\volumes`
Kontener można uruchomić bez podania wolumenu, ale wtedy zostanie stworzony wolumen anonimowy.

## Lista wolumenów i informacje o nich

```shell
docker volume ls
```

```shell
docker volume inspect task-tracker-data
```

## Uruchomienie bazy danych

```shell
docker run --name task-tracker-db --network task-tracker-net -p 5432:5432 -v task-tracker-data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=admin -e POSTGRES_USER=admin -e POSTGRES_DB=task-tracker -d postgres:16
```

Uruchomiona w ten sposób baza jest dostępna lokalnie na porcie 5432 i można się do niej podłączyć zewnętrznym klientem

## Uruchomienie aplikacji

```shell
docker run --name task-tracker --network task-tracker-net -p 8080:8080 -e APP_DESCRIPTION='Docker run app' -e SPRING_DATASOURCE_URL='jdbc:postgresql://task-tracker-db:5432/task-tracker' -d chrosciu/task-tracker:3
```

## Usunięcie sieci

```shell
docker network rm task-tracker-net
```

## Usunięcie wolumenu

```shell
docker volume rm task-tracker-data
```

**Wolumen można usunąć tylko po tym gdy zostanie usunięty kontener(y), który(e) go używa(ją) !**

## Sieć z użyciem docker compose

Przy użyciu `docker compose` można pominąć ręczne tworzenie sieci.

W takim przypadku zostanie stworzona sieć o nazwie takiej jak nazwa folderu gdzie znajduje się plik `docker-compose.yml`

W naszym przypadku zdecydowałem się jednak na użycie istniejącej sieci (za pomocą property `external` ustawionego na `true`)