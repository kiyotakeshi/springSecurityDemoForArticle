import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.1"
	id("io.spring.dependency-management") version "1.1.0"
	// https://github.com/etiennestuder/gradle-jooq-plugin#compatibility
	id("nu.studer.jooq") version "8.1"

	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-jooq")


	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	jooqGenerator("org.postgresql:postgresql")
	// https://github.com/etiennestuder/gradle-jooq-plugin/issues/207
	jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
}

jooq {
	version.set(dependencyManagement.importedProperties["jooq.version"])
	edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

	configurations {
		create("main") {
			// ref https://github.com/etiennestuder/gradle-jooq-plugin#generating-the-jooq-sources
			// ref https://www.greptips.com/posts/1350/#jooq-configurations
			generateSchemaSourceOnCompilation.set(false)
			jooqConfiguration.apply {
				jdbc.apply {
					driver = "org.postgresql.Driver"
					// TODO: 環境変数から読み取るようにし、direnv 等で設定する
					// url = System.getenv("POSTGRES_URL")
					// user = System.getenv("POSTGRES_USER")
					// password = System.getenv("POSTGRES_PASSWORD")
					url = "jdbc:postgresql://localhost:15432/testdb"
					user = "postgres"
					password = "password"
				}
				generator.apply {
					name = "org.jooq.codegen.KotlinGenerator"
					database.apply {
						name = "org.jooq.meta.postgres.PostgresDatabase"
						inputSchema = "public"
						// excludes = "flyway_schema_history"
					}
					generate.apply {
						isDeprecated = false
						isTables = true
						// isRecords = true
						// isImmutablePojos = true
						// isFluentSetters = true
					}
					target.apply {
						packageName = "com.example.zenn.jooq.codegen"
						directory = "build/generated-src/jooq/"
					}
					strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
				}
			}
		}
	}
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
