# Projekt fullstack
Autoscaler w Kubernetes to narzędzie automatyzujące skalowanie aplikacji poprzez dostosowywanie liczby podów w odpowiedzi na zmieniające się obciążenie. Horizontal Pod Autoscaler (HPA) monitoruje metryki, takie jak zużycie CPU i pamięci, i dynamicznie skaluje aplikacje, aby zapewnić optymalną wydajność oraz efektywne wykorzystanie zasobów. HPA może być skonfigurowany do skalowania na podstawie wielu metryk jednocześnie, co zapewnia elastyczność i odporność aplikacji w zmiennych warunkach obciążenia.  
  
W ramach tego projektu przygotowano przykładową aplikację do uruchomenia w klastrze Kubernetes (folder /app). 

## Opis wykorzystanej aplikacji
Do niniejszego projektu wykorzystano część backendową aplikacji pozwalającej na zarządzanie książkami (uproszczeona wersja portalu goodreads.com). Aplikacja została napisana przy wykorzystaniu języka Java 17 oraz frameworka Spring boot 2.6. Dla uproszczenia wykorzystano bazę danych H2. 

## Opis manifestu basic-deployment.yml
```yml
apiVersion: apps/v1
kind: Deployment
metadata:
    ...
spec:
  replicas: 1 
```
- `replicas: 1` - początkowa liczba replik ustawiana na 1

```yml
apiVersion: apps/v1
kind: Deployment
    ...
    spec:
    ...
        spec:
        containers:
        - image: fenrir7734/bookly-backend:latest
            name: bookly-backend
            resources:
            requests:
                cpu: 100m
                memory: 512Mi
            limits:
                cpu: 400m
                memory: 1028Mi
```
- `resources` - definiuje zasoby dla kontenera:
    - `requests`: Minimalne zasoby, które kontener rezerwuje (100m CPU i 512Mi pamięci).
    - `limits`: Maksymalne zasoby, które kontener może używać (400m CPU i 1028Mi pamięci).

```yml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: bookly-hpa
spec:
  ...
  minReplicas: 1
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 50
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 70
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 60 
      policies:
        - type: Percent
          value: 50
          periodSeconds: 60
    scaleDown:
      stabilizationWindowSeconds: 60
      policies:
        - type: Percent
          value: 50
          periodSeconds: 60
```
- `minReplicas: 1` - Minimalna liczba replik, którą może mieć aplikacja.
- `maxReplicas: 10` - Maksymalna liczba replik, którą może mieć aplikacja.
- `metrics` - Skalowanie na podstawie wykorzystania zasobów:
  - `cpu` - Skalowanie przy średnim wykorzystaniu CPU powyżej 50%.
  - `memory` - Skalowanie przy średnim wykorzystaniu pamięci powyżej 70%.
- `behavior` - Definiuje zasady skalowania:
  - `scaleUp` i `scaleDown` - Stabilizacja na 60 sekund i skalowanie o 50% w okresie 60 sekund. Ustawienie okna stabilizacji na 60 sekund oznacza, że autoskaler będzie czekał 60 sekund, zanim zareaguje na zmiany w metrykach. Zapobiega to nagłym wahaniom liczby replik w odpowiedzi na krótkotrwałe piki obciążenia np. Spring ma wysokie wykorzystanie zasobów podczas uruchomienia, przez co, jeżeli nie zastosujemy `stabilizationWindowSeconds`, może się zdażyć że autoskaler stworzy dodatkowe repliki już przy uruchamianiu aplikacji, bo wykryje duże obciążenie.
  - `periodSeconds` - skalowanie może zachodzić co 30 sekund

## Instrukcja uruchomienia
W celu uruchomienia projektu w klastrze kubernates wymagane są:
- Docker
- Minikube
- Linux/MacOS lub WSL

### Zbudowanie obrazu aplikacji (opcjonalne)
Krok ten jest opcjonalny ponieważ obraz aplikacji został już zbudowany i znajduje się w [repozytorium na DockerHub](https://hub.docker.com/repository/docker/fenrir7734/bookly-backend/general). Jeżeli jednak chcemy zbudować obraz należy:
1. wejść do katalogu `/app`
2. wykonać po kolei polecenia (`<username>` należy zastąpić odpowiednią nazwą użytkownika)
```shell
docker build -t bookly-backend .
docker login
docker tag bookly-backend <username>/bookly-backend
docker push <username>/bookly-backend
```
Po wykonaniu tych kroków na DockerHub, we wskazanym repozytorium powinien znajdować się już obraz. W dalszej części tej instrukcji zakładam że wykorzystany został już istniejący obraz `fenrir7734/bookly-backend` .

### Uruchomienie aplikacji w klastrze k8s
1. Wejść do katalogu w którym znajduje się plik `basic-deployment.yml`
2. Wykonać polecenie
```
kubectl apply -f basic-deployment.yml
```
