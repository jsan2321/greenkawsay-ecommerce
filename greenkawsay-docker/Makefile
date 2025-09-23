# Docker Compose - Load Environment Variables
include .env
export

# Docker Compose
up:
	docker compose --env-file .env up -d

down:
	docker compose down

logs:
	docker compose logs -f

restart:
	docker compose restart

# Database
db-psql:
	docker compose exec postgres psql -U ${POSTGRES_USER} -d ${POSTGRES_DB}

db-shell:
	docker compose exec postgres bash

# Database utilities
db-backup:
	docker compose exec postgres pg_dump -U ${POSTGRES_USER} -d ${POSTGRES_DB} > backup_$(shell date +%Y%m%d_%H%M%S).sql

db-restore:
	@echo "Available backup files:"; \
	ls -la *.sql; \
	echo "Which file do you want to restore? (ej: backup_20231201_123456.sql)"; \
	read filename; \
	docker-compose exec -T postgres psql -U ${POSTGRES_USER} -d ${POSTGRES_DB} < $$filename

db-keycloak-psql:
	docker compose exec postgres-keycloak psql -U ${KEYCLOAK_DB_USER} -d ${KEYCLOAK_DB}

# Keycloak
keycloak-logs:
	docker compose logs -f keycloak

keycloak-restart:
	docker compose restart keycloak

# Redis
redis-cli:
	docker compose exec redis redis-cli -a ${REDIS_PASSWORD}

# System
status:
	docker compose ps

clean:
	docker compose down -v

clean-all:
	docker compose down -v --rmi all

# Monitoring
monitor-db:
	docker compose exec postgres top

# Health checks
health:
	docker compose exec postgres pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB}

# CI/CD
ci-test:
	@echo "Starting CI test pipeline..."
	docker compose up -d postgres
	@echo "Waiting for database to be ready..."
	sleep 15
	docker compose exec -T postgres pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB}
	@echo "Running database migrations..."
	cd ../greenkawsay-backend && ./mvnw flyway:migrate -Dspring-boot.run.profiles=dev
	@echo "Running backend tests..."
	cd ../greenkawsay-backend && ./mvnw test -Dspring-boot.run.profiles=dev
	@echo "Cleaning up..."
	docker compose down
	@echo "CI test pipeline completed successfully"

# Development
dev-up: up
	@echo "Docker services started"
	@echo "To view logs: make logs"
	@echo "To connect to the database: make db-psql"
	@echo "Now run your Spring Boot application:"
	@echo "   cd ../greenkawsay-backend && ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev"

# Help
help:
	@echo "GreenKawsay Commands:"
	@echo "  make up           - Start services"
	@echo "  make down         - Stop services"
	@echo "  make logs         - View real-time logs"
	@echo "  make db-psql      - Connect to PostgreSQL"
	@echo "  make status       - Container status"
	@echo "  make clean        - Clean everything (including volumes)"
	@echo "  make health       - Check database health"
	@echo "  make dev-up       - Start and display instructions"
	@echo "  make help         - Show this help"

.PHONY: up down logs restart db-psql db-shell db-backup db-restore status clean clean-all monitor-db health dev-up ci-test help
