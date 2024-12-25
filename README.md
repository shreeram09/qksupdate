# qksupdate

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/qksupdate-1.0.0-SNAPSHOT-runner`

## Migrating quarkus-bom to latest version
execute below maven goal and observe the output
```shell
./mvnw quarkus:update
```
this takes help of openrewrite plugin which uses recipe got generated at `%USERPROFILE%/AppData/Local/Temp/` 
```shell
./mvnw.cmd -B -e org.openrewrite.maven:rewrite-maven-plugin:5.39.0:run -DplainTextMasks=**/META-INF/services/**,**/*.txt,**/src/main/codestarts/**/*.java,**/*.md,**/*.kt,**/src/test/resources/__snapshots__/**/*.java,**/*.adoc -Drewrite.configLocation=quarkus-project-recipe.yaml -Drewrite.recipeArtifactCoordinates=io.quarkus:quarkus-update-recipes:1.0.22 -DactiveRecipes=io.quarkus.openrewrite.Quarkus -Drewrite.pomCacheEnabled=false

```
If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.
