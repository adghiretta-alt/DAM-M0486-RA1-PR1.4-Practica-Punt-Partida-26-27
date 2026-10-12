package com.project.exemples;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EscripturaSimpleJacksonJSON {
    public static void main(String[] args) {
        // Crea un Map amb les dades que es volen escriure al fitxer JSON.
        // LinkedHashMap conserva l'ordre d'inserció de les claus.
        Map<String, Object> persona = new LinkedHashMap<>();
        persona.put("nom", "Pau López");
        persona.put("edat", 30);
        persona.put("ocupacio", "Dissenyador gràfic");
        persona.put("correu", "pau.lopez@example.com");

        // Crea un altre Map per a la direcció de la persona (objecte aniuat)
        Map<String, String> direccio = new LinkedHashMap<>();
        direccio.put("carrer", "Carrer de la Llum");
        direccio.put("ciutat", "València");
        direccio.put("codi_postal", "46001");
        persona.put("direccio", direccio);

        // Crea la llista de telèfons (es convertirà en un array JSON)
        Map<String, String> telefonCasa = new LinkedHashMap<>();
        telefonCasa.put("tipus", "casa");
        telefonCasa.put("numero", "963456789");

        Map<String, String> telefonMobil = new LinkedHashMap<>();
        telefonMobil.put("tipus", "mòbil");
        telefonMobil.put("numero", "612345678");

        persona.put("telefon", List.of(telefonCasa, telefonMobil));

        // Defineix la ruta del fitxer on es guardarà el JSON
        File file = new File("data/exemples/json_escriptura_simple_output_jackson.json");

        // Crea el mapper (Jackson 3) que farà l'escriptura del fitxer JSON
        JsonMapper mapper = JsonMapper.builder().build();

        try {
            // Escriu el Map com a JSON al fitxer, amb sagnat per fer-lo llegible
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, persona);
            System.out.println("JSON escrit correctament a " + file.getPath());
        } catch (JacksonException e) {
            System.err.println("No s'ha pogut escriure " + file + ": " + e.getOriginalMessage());
        }
    }
}
