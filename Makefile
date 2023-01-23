.PHONY: all init-db show-sample-data

all: init-db show-sample-data generate-jooq-code run

init-db:
	@echo "---------------"
	@echo "init db\n"
	docker compose exec postgres /bin/bash -c 'psql -Upostgres -d testdb -a -f /tmp/init.sql'

show-sample-data:
	@echo "---------------"
	@echo "show sample data\n"
	docker compose exec postgres /bin/bash -c 'psql -Upostgres -d testdb -c "select c.*, r.name from customers c left join customer_role cr on c.id = cr.customer_id left join roles r on cr.role_id = r.id;"'

generate-jooq-code:
	@echo "---------------"
	@echo "generate jOOQ code\n"
	./gradlew clean generateJooq

run:
	@echo "---------------"
	@echo "run application\n"
	./gradlew bootRun
	# ./gradlew build && java -jar build/libs/zenn-0.0.1-SNAPSHOT.jar
