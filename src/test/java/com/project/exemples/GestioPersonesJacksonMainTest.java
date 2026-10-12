package com.project.exemples;

import com.project.objectes.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests per a GestioPersonesJacksonMain.
 */
public class GestioPersonesJacksonMainTest {

    private static final String JSON_CONTENT = """
            [
                {"id": 1, "nom": "Joan Garcia", "edat": 34},
                {"id": 2, "nom": "Maria Pérez", "edat": 28},
                {"id": 3, "nom": "Anna López", "edat": 42}
            ]
            """;

    @TempDir
    File tempDir;

    private File tempFile;
    private GestioPersonesJacksonMain app;

    @BeforeEach
    void setup() throws IOException {
        // Crea un fitxer temporal "persones_input.json" dins del directori temporal creat per JUnit
        tempFile = new File(tempDir, "persones_input.json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(JSON_CONTENT);
        }
        // Inicialitza l'objecte GestioPersonesJacksonMain amb el fitxer temporal
        app = new GestioPersonesJacksonMain(tempFile);
    }

    @Test
    void testCarregarPersones() {
        List<Persona> persones = app.carregarPersones();
        assertNotNull(persones, "La llista de persones no hauria de ser nul·la.");
        assertEquals(3, persones.size(), "Hauria d'haver-hi tres persones al document JSON.");
    }

    @Test
    void testModificarEdat() {
        List<Persona> persones = app.carregarPersones();
        app.modificarEdat(persones, 1, 35);
        assertEquals(35, persones.get(0).getEdat(), "L'edat de la persona amb id 1 hauria de ser 35.");
    }

    @Test
    void testAfegirNovaPersona() {
        List<Persona> persones = app.carregarPersones();
        app.afegirNovaPersona(persones, new Persona(4, "Miquel Soler", 30));
        assertEquals(4, persones.size(), "Hauria d'haver-hi quatre persones després d'afegir-ne una nova.");
        assertEquals("Miquel Soler", persones.get(3).getNom(), "El nom de la quarta persona hauria de ser 'Miquel Soler'.");
    }

    @Test
    void testEsborrarPersona() {
        List<Persona> persones = app.carregarPersones();
        app.esborrarPersona(persones, 2);
        assertEquals(2, persones.size(), "Hauria d'haver-hi dues persones després d'esborrar la persona amb id 2.");
        assertTrue(persones.stream().noneMatch(persona -> persona.getId() == 2), "La persona amb id 2 hauria d'haver estat esborrada.");
    }

    @Test
    void testGuardarPersones() {
        List<Persona> persones = app.carregarPersones();
        app.modificarEdat(persones, 1, 35);
        app.afegirNovaPersona(persones, new Persona(4, "Miquel Soler", 30));
        app.esborrarPersona(persones, 2);
        app.guardarPersones(persones);

        // Llegeix el fitxer de sortida i verifica el contingut
        File outputFile = new File(tempFile.getParent(), "persones_output_jackson.json");
        JsonMapper mapper = JsonMapper.builder().build();
        List<Persona> personesGuardades = mapper.readValue(outputFile, new TypeReference<List<Persona>>() {});

        assertEquals(3, personesGuardades.size(), "Hauria d'haver-hi tres persones al fitxer de sortida.");
        assertEquals(35, personesGuardades.get(0).getEdat(), "L'edat de la persona amb id 1 hauria de ser 35 al fitxer de sortida.");
        assertEquals("Miquel Soler", personesGuardades.get(2).getNom(), "El nom de la tercera persona hauria de ser 'Miquel Soler' al fitxer de sortida.");
    }
}
