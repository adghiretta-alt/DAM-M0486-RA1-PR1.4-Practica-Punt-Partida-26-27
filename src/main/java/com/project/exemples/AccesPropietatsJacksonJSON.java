package com.project.exemples;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;

public class AccesPropietatsJacksonJSON {
    public static void main(String[] args) {
        // Defineix la ruta del fitxer JSON
        String filePath = "data/exemples/json_lectura_simple_input.json";

        // Crea el mapper per llegir el fitxer JSON (Jackson 3)
        JsonMapper mapper = JsonMapper.builder().build();

        try {
            // Llegeix el fitxer JSON i el converteix en un JsonNode
            JsonNode rootNode = mapper.readTree(new File(filePath));

            // Accedeix a les propietats simples (a Jackson 3, asString() substitueix asText())
            String nom = rootNode.get("nom").asString();
            int edat = rootNode.get("edat").asInt();
            String ocupacio = rootNode.get("ocupacio").asString();
            String correu = rootNode.get("correu").asString();

            // Accedeix a l'objecte aniuat 'direccio'
            JsonNode direccioNode = rootNode.get("direccio");
            String carrer = direccioNode.get("carrer").asString();
            String ciutat = direccioNode.get("ciutat").asString();
            String codiPostal = direccioNode.get("codi_postal").asString();

            // Accedeix a l'array 'telefon'
            JsonNode telefonArray = rootNode.get("telefon");

            System.out.println("Nom: " + nom);
            System.out.println("Edat: " + edat);
            System.out.println("Ocupació: " + ocupacio);
            System.out.println("Correu: " + correu);
            System.out.println("Direcció: ");
            System.out.println("  Carrer: " + carrer);
            System.out.println("  Ciutat: " + ciutat);
            System.out.println("  Codi Postal: " + codiPostal);

            // Recorre l'array 'telefon' (un JsonNode és iterable) i mostra cada número de telèfon
            System.out.println("Telèfons:");
            for (JsonNode telefon : telefonArray) {
                String tipus = telefon.get("tipus").asString();
                String numero = telefon.get("numero").asString();
                System.out.println("  " + tipus + ": " + numero);
            }
        } catch (JacksonException e) {
            System.err.println("No s'ha pogut llegir " + filePath + ": " + e.getOriginalMessage());
        }
    }
}
