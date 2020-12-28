# Game Of Life

Symulacje uruchamiamy klasą Main znajdującą się w pakiecie agh.cs.gameOfLife<br>
Parametry możemy edytować w pliku parameters.json (wartości przykładowe):<br>

{<br>
  "width": 20, - szerokość mapy<br>
  "height": 20, - wysokość mapy<br>
  "numberOfPlants": 40, - startowa liczba roślinek<br>
  "plantsEnergy": 10, - energia uzyskiwana z jednej roślinki<br>
  "startEnergy": 20, - energia startowa zwierząt<br>
  "jungleRatio": 3, - ilukrotnie powierzchnia dżungli mniejsza jest od powierzchni mapy<br>
  "numberOfAnimals": 20, - startowa liczba zwierząt<br>
  "moveEnergy": 1, - energia tracona przez zwierzaki każdego dnia<br>
  "speed": 4, - liczba odświeżeń symulacji na sekundę<br>
  "numberOfSimulations": 1, - liczba symulacji (niestety nieobsłużone)<br>
  "dayWhenDataSaved": 150<br> - w którym dniu dane zostaną zapisane do pliku .txt<br>
}<br>
Statystyki ściągnięte po podanej liczbie dni zapisują się w pliku o nazwie statistics after [dayWhenDataSaved] days.txt
