# Wallet API

Finaya Digital Wallet.

<hr/>

| Technology | Version |
|   :---:    |  :---:  |
| Java       | 21      |
| Spring     | 3.5.9   |
| PostgreSQL | 15      |
| Docker     | >29     |

## Project Structure
```
src/
└─ main/
   └─ java/
      └─ tech.finaya.wallet/
         ├─ adapter/
         │   ├─ inbounds/
         │   │   ├─ controllers/               # Controllers
         │   │   │   ├─ api/                   # API contracts
         │   │   │   └─ internal/              # Internal controllers
         │   │   ├─ dto/
         │   │   │   ├─ requests/              # Request DTOs
         │   │   │   └─ responses/             # Response DTOs
         │   └─ outbounds/
         │       ├─ persistence/
         │       │   ├─ adapters/              # Persistence adapters
         │       │   ├─ jpa/                   # JPA/Hibernate implementations
         │       │   └─ repositories/          # Repository interfaces (Ports)
         ├─ config/                            # Spring configuration
         ├─ domain/
         │   ├─ exceptions/                    # Domain exceptions                   
         │   ├─ models/                        # Domain entities
         │   │   ├─ builders/                  # Builders
         │   │   ├─ factories/                 # Domain object factories
         │   │   ├─ webhook/                   # Domain webhook
         │   │   └─ enums/                     # Enums used in the domain
         │   └─ usecases/                      # Use cases
         └─ infrastructure/
             ├─ exceptions/                    # Infrastructure exceptions
             └─ mappers/                       # Mapping classes (DTO ↔ Entity)
```
## MER

![mer](./docs/mer.png)

## How to run locally?

*Linux and MAC:*
1. Open a terminal in the application's root directory and run: `make up`.
2. Open your browser and go to the address http://localhost:8080/.

* Use `make down` to stop all services.

*Windows:*
1. Open a terminal in the application's root directory and run: `docker compose up -d`.
2. Open your browser and go to the address http://localhost:8080/.

* Use `docker compose down` to stop all services.

## How to run tests?

### Unit

*Linux and MAC:*
1. Open a terminal in the application's root directory and run: `make unit-test`.

*Windows:*
1. Open a terminal in the application's root directory and run: `./gradlew test`.

### Integration

*Linux and MAC:*
1. Open a terminal in the application's root directory and run: `make integration-test`.

*Windows:*
1. Open a terminal in the application's root directory and run: `./gradlew integrationTest`.

### All tests and report

*Linux and MAC:*
1. Open a terminal in the application's root directory and run: `make all-tests-with-report`.
2. Open the [report](./build/jacocoHtml/index.html).

*Windows:*
1. Open a terminal in the application's root directory and run: `./gradlew clean test integrationTest jacocoTestReport`.
2. Open the [report](./build/jacocoHtml/index.html).

## Links

[Health](http://localhost:8080/actuator/health)

[Info](http://localhost:8080/actuator/info)

[Swagger](http://localhost:8080/swagger-ui.html)

[TODO](./docs/TODO.md)

[Code Design](./docs/code-design.md)

[Trade-Offs](./docs/trade-offs.md)