up-no-cache:
	@echo "Starting app, postgresql and pgadmin without cache..."
	docker compose build --no-cache && docker compose up

up:
	@echo "Starting app, postgresql and pgadmin..."
	docker compose up

down:
	@echo "Stopping app, postgresql and pgadmin..."
	docker compose down
