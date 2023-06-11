# The Convenient Foodie

The Convenient Foodie je aplikacija za naručivanje i brzu dostavu hrane na adresu korisnika. Svjesni smo da je najljepše imati obrok u ugodnosti vlastitog doma, a da je sve više restorana u gradu na različitim udaljenim lokacijama iz kojih potiče naša najomiljenija hrana, među kojima ima i restorana koji ne omogućavaju naručivanje. Naša aplikacija rješava ovaj problem na način da uvezuje sve restorane na jedno mjesto, iz kojeg korisnik uz par klikova može naručiti svoj omiljeni obrok na adresu svog doma.

## Korištene tehnologije
Pri implementaciji projekta korištena je mikroservisna arhitektura. Za implementaciju su korištene sljedeće tehnologije:
- Java Spring Boot
- React
- MySQL

## Članovi tima:
*	*Mujić Mehmed*
*	*Ćatić Irvin*
*	*Hadžibajramović Amila*
*	*Behić Samra*

## Pokretanje aplikacije pomoću Docker-a
Za pokretanje aplikacije pomoću Docker-a, potrebno je preuzeti [Docker](https://www.docker.com) za operativni sistem koji koristite. Nakon pokretanja dockera, potrebno je pomoću terminala se pozicionirati u root projekta i pokrenuti `docker compose up` komandu. Alternativno, iz root projekta možete preuzeti `docker-compose.yml`, i terminal podesiti na lokaciju na kojoj se nalazi ova datoteka, te pokrenuti `docker compose up`.

Pri prvom pokretanju je potrebno sačekati određeno vrijeme dok se sve slike preuzmu.
