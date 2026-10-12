# Pràctica PR1.4 - JSON en Java (punt de partida, curs 2026-2027)

Aquest projecte és el punt de partida de la pràctica PR1.4 del mòdul MP0486 Accés a dades (CFGS DAM), dedicada a llegir, modificar i guardar documents JSON amb Java.


### Requisits ###

* JDK 25 (LTS) recomanat. El projecte compila amb qualsevol JDK a partir de la versió 21.
* Maven 3.9 o superior.
* Git.


### Estructura del projecte ###

* `src/main/java/com/project/exemples`: exemples de referència (Jakarta JSON-P, Jackson 3, Gson, Moshi, org.json, JSON-B i un client i un servidor HTTP que intercanvien JSON).
* `src/main/java/com/project/objectes`: classes del model (`Persona` i `Llibre`).
* `src/main/java/com/project/pr14`: classes que heu de completar (busqueu `CODI PRÀCTICA`).
* `src/test/java`: tests JUnit que ha de superar el vostre codi.
* `data/exemples` i `data/pr14`: fitxers JSON d'entrada i de sortida.
* `doc`: hi heu de deixar la memòria (`memoria.pdf`).


### Jackson 3 ###

Aquest curs fem servir **Jackson 3** (`tools.jackson.core:jackson-databind`). Si trobeu exemples a internet amb `import com.fasterxml.jackson.databind...`, són de Jackson 2. Per adaptar-los:

* Canvieu els paquets `com.fasterxml.jackson.core` i `com.fasterxml.jackson.databind` per `tools.jackson.core` i `tools.jackson.databind`.
* Les anotacions (`@JsonIgnore`, `@JsonProperty`...) **no canvien**: continuen a `com.fasterxml.jackson.annotation`.
* Les excepcions de Jackson 3 (`JacksonException`) són no comprovades (*unchecked*): el compilador no us obligarà a capturar-les, però **les heu de gestionar igualment**.


### Compilació i tests ###

```bash
mvn clean compile
mvn test
```

Per executar només els tests d'una classe o d'un grup de classes:
```bash
mvn test -Dtest=PR14GestioLlibreriaJakartaMainTest
mvn test -Dtest=PR14GestioLlibreriaJacksonMainTest
mvn test -Dtest="PR14*"
```


### Execució ###

Amb els scripts del projecte (Windows i Linux/macOS):
```bash
.\run.ps1 com.project.pr14.PR14GestioLlibreriaJakartaMain
./run.sh com.project.pr14.PR14GestioLlibreriaJacksonMain
```

Directament amb Maven:
```bash
mvn compile exec:java "-Dexec.mainClass=com.project.pr14.PR14GestioLlibreriaJacksonMain"
```

Amb el .jar generat (cal afegir les dependències al classpath):
```bash
mvn package -DskipTests dependency:copy-dependencies -DincludeScope=runtime
# Linux/macOS
java -cp "target/project-pr14-1.0.0.jar:target/dependency/*" com.project.pr14.PR14GestioLlibreriaJacksonMain
# Windows
java -cp "target/project-pr14-1.0.0.jar;target/dependency/*" com.project.pr14.PR14GestioLlibreriaJacksonMain
```


### Exemple client/servidor ###

Primer engegueu el servidor i, des d'un altre terminal, executeu el client. El client envia un JSON amb un nom i un any de naixement i el servidor respon amb l'any en què aquella persona farà 120 anys.

```bash
./run.sh com.project.exemples.ExempleServidor
./run.sh com.project.exemples.ExempleClient
```


### Visual Studio Code: restabliment de l'entorn de programació Java ###

Si Visual Studio Code no es comporta com esperem i hem provat a solucionar-ho sense èxit podem provar aquestes dues solucions:

* Recarregar la finestra: obre la paleta d'ordres (**Ctrl+Maj+P**), escriu "**Developer: Reload Window**" i prem Retorn.

* Netejar l'espai de treball: si recarregar no funciona, obre de nou la paleta d'ordres (**Ctrl+Maj+P**), escriu "**Java: Clean Java Language Server Workspace**" i prem Retorn. Aquesta és una solució molt eficaç per a molts problemes relacionats amb Java a VS Code. Se't demanarà que recarreguis i tornis a escanejar el projecte.
