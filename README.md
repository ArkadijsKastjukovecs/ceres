## Requirements to Run the Application

- **Java**: JDK 21 or higher must be configured on your machine.
- **Maven**: Maven 3.9.x or higher for dependencies and builds.
- **Docker**: Docker 20.10.x or higher and docker-compose 1.29.x or higher for containerization support

---

## Setup Instructions

1. Clone the repository.
2. On the project root run

```bash
  ./mvnw clean spring-boot:run
```

## Done

- Scheduling, and initial data loading
- Data storage using mysql
- Code generation using jooq
- Required apis
- Error handling

## To be done

- Documentation
- Parametrized db configuration (currently hardcoded)
- Logging
- Test coverage
