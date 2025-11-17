# Spring Framework JSP Application

A Spring Framework 6.x web application using JSP, MyBatis, and PostgreSQL.

## Tech Stack

- **Framework**: Spring Framework 6.1.14 (Spring MVC, Spring JDBC)
- **Server**: Apache Tomcat 11 (JDK 21)
- **Database**: PostgreSQL
- **Build**: Gradle 9.2.0
- **View**: JSP with JSTL

## Getting Started

### Prerequisites

- JDK 21
- Docker & Docker Compose
- Gradle 9.2.0 (or use included gradlew)

### Development Mode (with Hot Reloading)

1. Start the exploded WAR builder in continuous mode (Terminal 1):

```bash
./gradlew explodeWar --continuous
```

2. Start Docker containers (Terminal 2):

```bash
docker-compose up -d
```

3. Access the application:
```
http://localhost:8080
```

### Production Build

Build and deploy the WAR file:

```bash
./gradlew buildAndDeploy
docker-compose up -d
```

## Available Gradle Tasks

- `./gradlew explodeWar` - Explode WAR to build/exploded for hot reloading
- `./gradlew explodeWar --continuous` - Continuous build mode (auto-rebuild on changes)
- `./gradlew deployWar` - Deploy WAR file to target directory
- `./gradlew buildAndDeploy` - Clean, build, and deploy in one command

## Docker Services

- **Tomcat**: http://localhost:8080
- **PostgreSQL**: localhost:5432
  - Database: `spring-framework-db`
  - User: `user`
  - Password: `password`

## Development Notes

- The application uses exploded WAR deployment for hot reloading during development
- Tomcat is configured with `reloadable="true"` for automatic class reloading
- Database connection settings are configured via environment variables in docker-compose.yml
- Init scripts in `init-scripts/` directory are automatically executed on PostgreSQL startup

## Stopping the Application

```bash
docker-compose down
```