package com.project.exemples;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;

public class LecturaSimpleJacksonJSON {
    public static void main(String[] args) {
        // Defineix la ruta del fitxer JSON
        File file = new File("data/exemples/json_lectura_simple_input.json");

        // Jackson 3: els mappers són immutables i es creen amb un builder
        JsonMapper mapper = JsonMapper.builder().build();

        try {
            // Llegeix el fitxer JSON i el converteix en un arbre de nodes (JsonNode)
            JsonNode jsonNode = mapper.readTree(file);

            // Mostra el contingut del JSON a la consola
            System.out.println(jsonNode.toPrettyString());
        } catch (JacksonException e) {
            // A Jackson 3 les excepcions són no comprovades (unchecked), però cal gestionar-les igualment
            System.err.println("No s'ha pogut llegir " + file + ": " + e.getOriginalMessage());
        }
    }
}
