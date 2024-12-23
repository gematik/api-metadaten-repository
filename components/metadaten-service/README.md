# Metadaten-Service zur Anwendungs-Discovery

## Features
- Schema-Upload über Endpoint
- Schemabindung an bestimmte Anbieter-Stammdaten
- Metadaten-Upload über Endpoint
- Metadatenbindung an bestimmte Anbieter-Stammdaten
- Schemavalidierung
- Bestimmte Endpoints der OpenAPI-Definition werden abgebildet
- OpenAPI-Definition wird auf Basis der vonhandenen Resources dynamisch gerendert
- statisches Infomodel
- Nutzungsbeispiel

## Feature-Backlog
- Persistierung (bisher Datenhaltung nur flüchtig zur Laufzeit)
- n Discovery Objects pro Stammdatensatz
- sauberes Exception-Handling
- vollständige Abbildung der OpenAPI-Definition
- weitere Nutzungsbeispiele
- Redirect zu Anbieter-Endpoint
- dynamisches Infomodel auf OWL-Basis (optional)
- tbc..


## Webanwendung laufen lassen im dev mode

```sh
./gradlew quarkusDev
```

## Testfälle ausführen
```sh
./gradlew test
```
oder IDE: `verification/test`
