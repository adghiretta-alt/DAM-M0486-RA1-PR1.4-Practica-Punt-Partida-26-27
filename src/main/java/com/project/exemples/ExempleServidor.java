package com.project.exemples;

import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Headers;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ExempleServidor {

    private static final Path REQUESTS_DIR = Paths.get(
            System.getProperty("user.dir"),
            "data",
            "requests"
    );

    public static void main(String[] args) {

        // Configuració del servidor Undertow
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new BlockingHandler(exchange -> {

                    exchange.getResponseHeaders().put(
                            Headers.CONTENT_TYPE,
                            "application/json; charset=UTF-8"
                    );

                    // Llegir el mètode HTTP i el cos de la petició
                    String metode = exchange.getRequestMethod().toString();
                    String requestBody = new String(
                            exchange.getInputStream().readAllBytes(),
                            StandardCharsets.UTF_8
                    );

                    System.out.println("Petició rebuda: " + metode);
                    System.out.println("Cos de la petició: " + requestBody);

                    // Guardar cada petició sense impedir la resposta si falla
                    guardarPeticio(metode, requestBody);

                    // Permetre només peticions POST
                    if (metode.equalsIgnoreCase("POST")) {

                        try {
                            // Convertir el JSON rebut en un objecte JsonObject
                            try (JsonReader jsonReader = Json.createReader(
                                    new StringReader(requestBody))) {

                                JsonObject personaJson = jsonReader.readObject();

                                // Recuperar les dades del JSON
                                String nom = personaJson.getString("nom");
                                int anyNaixement = personaJson.getInt("anyNaixement");

                                // Calcular l'any en què la persona complirà 120 anys
                                int any120 = anyNaixement + 120;

                                // Crear la resposta JSON
                                JsonObject responseJson = Json.createObjectBuilder()
                                        .add("nom", nom)
                                        .add("anyComplira120", any120)
                                        .build();

                                // Convertir la resposta JSON en String
                                StringWriter stringWriter = new StringWriter();

                                try (JsonWriter jsonWriter =
                                             Json.createWriter(stringWriter)) {
                                    jsonWriter.write(responseJson);
                                }

                                String resposta = stringWriter.toString();

                                // Enviar la resposta al client
                                exchange.getResponseSender().send(resposta);
                            }

                        } catch (Exception e) {
                            // Gestionar els errors de processament
                            exchange.setStatusCode(500);
                            exchange.getResponseSender().send(
                                    "{\"error\":\"Error processant la petició.\"}"
                            );

                            e.printStackTrace();
                        }

                    } else {
                        // Si no és POST, indicar que el mètode no està permès
                        exchange.setStatusCode(405);
                        exchange.getResponseSender().send(
                                "{\"error\":\"Només es permeten peticions POST.\"}"
                        );
                    }
                }))
                .build();

        // Arrencar el servidor
        server.start();

        System.out.println(
                "Servidor en execució a http://localhost:8080"
        );
    }

    private static void guardarPeticio(String metode, String cos) {

        try {
            // Crear el directori si no existeix
            Files.createDirectories(REQUESTS_DIR);

            // Obtenir la data i l'hora actuals
            LocalDateTime ara = LocalDateTime.now();

            String dataHoraFitxer = ara.format(
                    DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")
            );

            // Crear un nom únic per al fitxer
            String nomFitxer = "peticio_"
                    + dataHoraFitxer
                    + "_"
                    + UUID.randomUUID()
                    + ".json";

            Path fitxer = REQUESTS_DIR.resolve(nomFitxer);

            // Construir el JSON amb les dades de la petició
            JsonObject peticioJson = Json.createObjectBuilder()
                    .add("dataHora", ara.toString())
                    .add("metodeHTTP", metode)
                    .add("cos", cos)
                    .build();

            // Escriure el JSON al fitxer
            try (var writer = Files.newBufferedWriter(
                    fitxer,
                    StandardCharsets.UTF_8
            );
                 JsonWriter jsonWriter = Json.createWriter(writer)) {

                jsonWriter.writeObject(peticioJson);
            }

            System.out.println("Petició guardada a: " + fitxer);

        } catch (Exception e) {
            // Un error en guardar no ha d'impedir respondre al client
            System.err.println(
                    "No s'ha pogut guardar la petició: "
                            + e.getMessage()
            );
        }
    }
}
