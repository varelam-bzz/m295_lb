# Steam from wish

## Beschreibung
`Steam from wish` ist eine *RESTful-API* für die Verwaltung einer Sammlung von Spielen und den dazugehörigen Herausgebern. 
Er bietet Endpoints für das **Erstellen**, **Aktualisieren**, **Abrufen** und **Löschen** von *Spielen* sowie für die **Suche** nach Spielen 
anhand **verschiedener Attribute** wie Name und Release Date.

## Visuals
### Datenbankdiagramm
![Datenbankdiagramm](pictures/steam_from_wish_erd.png)

### Klassendiagramm
![Klassendiagramm](pictures/steam_from_wish_uml.png)

### Screenshot der Testdurchführung
![1. Screenshot der Testdurchführung](pictures/tests_1.png)
![2. Screenshot der Testdurchführung](pictures/tests_2.png)
![Testdurchführung Resultat](pictures/tests_result.png)
*Bemerkung über Tests unter [Bemerkungen](#Bemerkungen)*

## Validierungsregeln
- Name des Spiels (`name`, *String*): Nicht mehr als 45 Zeichen lang.
- Preis des Spiels (`price`, *BigDecimal*): Nicht mehr als CHF 300.
- Anzahl Käufe des Spiels (`purchases`, *Integer*): Muss 0 oder Positiv sein.

## Berechtigungsmatrix
| Endpoint                      | Role           | Access Type   |
|-------------------------------|----------------|---------------|
| `/games/ping`                 | All            | Read          |
| `/games`                      | All            | Read          |
| `/games/count`                | All            | Read          |
| `/games/exists/{id}`          | All            | Read          |
| `/games/byId/{id}`            | All            | Read          |
| `/games/byName/{name}`        | All            | Read          |
| `/games/byReleaseDate/{date}` | All            | Read          |
| `/games`                      | ADMIN          | Create/Update |
| `/games/bulk`                 | ADMIN          | Create        |
| `/games`                      | ADMIN          | Update        |
| `/games/{id}`                 | ADMIN, CLEANER | Delete        |
| `/games`                      | ADMIN, CLEANER | Delete        |

## Bemerkungen
- Es wird angenommen, dass ein Spielname eindeutig ist.
- Bei einem Fehler `Preview not enabled`, muss der *TomCat Server* mit der folgenden *VM-Option* gestartet werden: `--enable-preview`
- Bei den Tests funktionieren Tests mit `id` nicht, da die `id` auf `auto_increment` gesetzt ist.

Damit die Tests zu 100% funktionieren, kann man den Folgenden Skript in MySQL laufen lassen:
```mysql
DELETE FROM steam.game;
DELETE FROM steam.publisher;
ALTER table steam.game AUTO_INCREMENT = 1;
```

## OpenAPI Dokumentation der Services (Ressourcen)


## Autor
[Matias Varela Cousillas](https://github.com/varelam-bzz)

## Zusammenfassung
Diese Dokumentation 