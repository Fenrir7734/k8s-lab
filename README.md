# Projekt fullstack
Autoscaler w Kubernetes to narzędzie automatyzujące skalowanie aplikacji poprzez dostosowywanie liczby podów w odpowiedzi na zmieniające się obciążenie. Horizontal Pod Autoscaler (HPA) monitoruje metryki, takie jak zużycie CPU i pamięci, i dynamicznie skaluje aplikacje, aby zapewnić optymalną wydajność oraz efektywne wykorzystanie zasobów. HPA może być skonfigurowany do skalowania na podstawie wielu metryk jednocześnie, co zapewnia elastyczność i odporność aplikacji w zmiennych warunkach obciążenia. Główne aspekty działania autoskalera horyzontalnego to:
- Autoskaler ciągle monitoruje kluczowe metryki wydajności, takie jak CPU, pamięć, czy ruch sieciowy. Metryki te są zbierane przez system monitorowania, który może być częścią chmury lub zewnętrznym narzędziem (Prometheus, Grafana).
- Na podstawie zdefiniowanych progów i reguł skalowania, autoskaler analizuje zebrane metryki. Gdy określone progi zostaną przekroczone (np. średnie użycie CPU przekracza 80%), autoskaler podejmuje decyzję o zwiększeniu liczby instancji.
- W zależności od obciążenia, autoskaler może dodawać nowe instancje (skalowanie w górę) lub usuwać niepotrzebne (skalowanie w dół). Proces ten odbywa się dynamicznie, co pozwala na efektywne wykorzystanie zasobów i optymalizację kosztów.
  
W ramach tego projektu przygotowano przykładową aplikację do uruchomenia w klastrze Kubernetes (folder /app). 

## Opis wykorzystanej aplikacji
Do niniejszego projektu wykorzystano część backendową aplikacji pozwalającej na zarządzanie książkami (uproszczeona wersja portalu goodreads.com). Aplikacja została napisana przy wykorzystaniu języka Java 17 oraz frameworka Spring boot 2.6. Dla uproszczenia wykorzystano bazę danych H2. 

## Opis manifestu bookly-deployment.yml
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
                    memory: 1024Mi
                limits:
                    cpu: 400m
                    memory: 2048Mi
            readinessProbe:
                httpGet:
                    path: /actuator/health
                    port: 8080
                initialDelaySeconds: 20
                periodSeconds: 5
```
- `resources` - definiuje zasoby dla kontenera:
    - `requests`: Minimalne zasoby, które kontener rezerwuje (100m CPU i 1024Mi pamięci).
    - `limits`: Maksymalne zasoby, które kontener może używać (400m CPU i 2048Mi pamięci).
- `readinessProbe` - sprawdza, czy aplikacja jest gotowa do obsługi ruchu. Kubernetes przekierowuje ruch tylko do podów, które przejdą tę kontrolę. Jest to wymagane ponieważ Spring potrzebuje co najmniej kilkudziesięciu sekund na rozruch, zanim będzie mógł obsługiwać żądania. Bez tego sprawdzenia autoskaler stworzy nowy pod i od razu zacznie do niego przekierowywać ruch, gubiąc tym samym żadania, które nie mogą być jeszcze obsłużone. `path` określa ścieżkę endpointa do którego będą wysyłane requesty metodą GET, w celu sprawdzenia gotowości poda. `initialDelaySeconds` określa liczbę sekund od stworzenia poda do pierwszego sprawdzenia jego gotowości. `periodSeconds` określa w sekundach jak często wykonywane będzie sprawdzanie gotowości.


```yml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: bookly-hpa
spec:
  ...
  minReplicas: 1
  maxReplicas: 5
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
        averageUtilization: 80
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 60 
      policies:
        - type: Percent
          value: 50
          periodSeconds: 60
    scaleDown:
      stabilizationWindowSeconds: 20
      policies:
        - type: Percent
          value: 50
          periodSeconds: 20
```
- `minReplicas: 1` - Minimalna liczba replik, którą może mieć aplikacja.
- `maxReplicas: 10` - Maksymalna liczba replik, którą może mieć aplikacja.
- `averageUtilization` - średni procentowy wskaźnik wykorzystania zasobów (np. CPU, pamięci) dla wszystkich podów w danym Deployment. Wyliczany jest na podstawie aktualnego zużycia zasobów w stosunku do zadeklarowanych limitów.
- `metrics` - Skalowanie na podstawie wykorzystania zasobów:
  - `cpu` - Skalowanie przy średnim wykorzystaniu CPU powyżej 50%.
  - `memory` - Skalowanie przy średnim wykorzystaniu pamięci powyżej 70%.
- `behavior` - Definiuje zasady skalowania:
  - `scaleUp` i `scaleDown` - Stabilizacja na 60 sekund (20 w przypadku `scaleDown`) i skalowanie o 50% w okresie 60 sekund (20 w przypadku `scaleDown`). Ustawienie okna stabilizacji na 60 sekund oznacza, że autoskaler będzie czekał 60 sekund, zanim zareaguje na zmiany w metrykach. Zapobiega to nagłym wahaniom liczby replik w odpowiedzi na krótkotrwałe piki obciążenia np. Spring ma wysokie wykorzystanie zasobów podczas uruchomienia, przez co, jeżeli nie zastosujemy `stabilizationWindowSeconds`, może się zdarzyć, że autoskaler stworzy dodatkowe repliki już przy uruchamianiu aplikacji, bo wykryje duże obciążenie.
  - `periodSeconds` - skalowanie może zachodzić co 60 sekund w przypadku `scaleUp` i co 20 sekund dla `scaleDown`

### YouTube - demostracja działania
[![youtube video](https://img.youtube.com/vi/o3Ts15qxS50/0.jpg)](https://youtu.be/o3Ts15qxS50)

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
1. Wejść do katalogu w którym znajduje się plik `bookly-deployment.yml`
2. Wykonać polecenie
```
kubectl apply -f bookly-deployment.yml
```
