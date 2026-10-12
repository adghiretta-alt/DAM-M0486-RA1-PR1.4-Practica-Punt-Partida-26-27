package com.project.exemples;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExempleClient {

    public static void main(String[] args) {
        // URL del servidor
        String url = "http://localhost:8080/";

        // Crear l'objecte JSON utilitzant Jakarta JSON
        JsonObject personaJson = Json.createObjectBuilder()
                .add("nom", "Núria Puig")
                .add("anyNaixement", 1985)
                .build();

        // Convertir l'objecte JSON a String
        String jsonString = personaJson.toString();

        System.out.println("JSON a enviar: " + jsonString);

        // Crear client HTTP amb HttpClient 5
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Crear la petició POST.
            // ContentType.APPLICATION_JSON afegeix "application/json; charset=UTF-8" i codifica el cos en UTF-8
            // (sense indicar-ho, StringEntity faria servir ISO-8859-1 i els accents arribarien malmesos).
            HttpPost postRequest = new HttpPost(url);
            postRequest.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));

            // Executar la petició: el "response handler" llegeix la resposta i allibera la connexió
            String resposta = client.execute(postRequest, response -> {
                System.out.println("Codi de resposta HTTP: " + response.getCode());
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            });

            System.out.println("Resposta del servidor: " + resposta);
        } catch (IOException e) {
            System.err.println("No s'ha pogut contactar amb el servidor (" + url + "): " + e.getMessage());
        }
    }
}
