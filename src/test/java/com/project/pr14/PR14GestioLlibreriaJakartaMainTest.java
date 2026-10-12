package com.project.pr14;

import com.project.objectes.Llibre;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests per a PR14GestioLlibreriaJakartaMain.
 */
public class PR14GestioLlibreriaJakartaMainTest {

    private static final String JSON_CONTENT = """
            [
                {"id": 1, "titol": "Els vents del nord", "autor": "Clara Rojas", "any": 1990},
                {"id": 2, "titol": "Muntanyes màgiques", "autor": "Joan Peris", "any": 2005},
                {"id": 3, "titol": "El secret del mar", "autor": "Laura Vidal", "any": 2010}
            ]
            """;

    @TempDir
    File tempDir;

    private File tempFile;
    private PR14GestioLlibreriaJakartaMain app;

    @BeforeEach
    void setup() throws IOException {
        // Crea un fitxer temporal "llibres_input.json" dins del directori temporal creat per JUnit
        tempFile = new File(tempDir, "llibres_input.json");
        Files.writeString(tempFile.toPath(), JSON_CONTENT, StandardCharsets.UTF_8);
        // Inicialitza l'objecte PR14GestioLlibreriaJakartaMain amb el fitxer temporal
        app = new PR14GestioLlibreriaJakartaMain(tempFile);
    }

    @Test
    void testCarregarLlibres() {
        List<Llibre> llibres = app.carregarLlibres();
        assertNotNull(llibres, "La llista de llibres no hauria de ser nul·la.");
        assertEquals(3, llibres.size(), "Hauria d'haver-hi tres llibres al document JSON.");
        assertEquals("Muntanyes màgiques", llibres.get(1).getTitol(), "El títol del segon llibre no s'ha llegit correctament.");
    }

    @Test
    void testCarregarLlibresFitxerInexistent() {
        PR14GestioLlibreriaJakartaMain appSenseFitxer = new PR14GestioLlibreriaJakartaMain(new File(tempDir, "no_existeix.json"));
        List<Llibre> llibres = assertDoesNotThrow(appSenseFitxer::carregarLlibres,
                "Si el fitxer no existeix, l'error s'ha de gestionar dins del mètode.");
        assertNull(llibres, "Si el fitxer no existeix, carregarLlibres() ha de retornar null.");
    }

    @Test
    void testCarregarLlibresJsonMalFormat() throws IOException {
        File fitxerErroni = new File(tempDir, "llibres_erronis.json");
        Files.writeString(fitxerErroni.toPath(), "[{\"id\": 1, \"titol\": ", StandardCharsets.UTF_8);
        PR14GestioLlibreriaJakartaMain appErroni = new PR14GestioLlibreriaJakartaMain(fitxerErroni);
        List<Llibre> llibres = assertDoesNotThrow(appErroni::carregarLlibres,
                "Si el JSON no és vàlid, l'error s'ha de gestionar dins del mètode.");
        assertNull(llibres, "Si el JSON no és vàlid, carregarLlibres() ha de retornar null.");
    }

    @Test
    void testModificarAnyPublicacio() {
        List<Llibre> llibres = app.carregarLlibres();
        app.modificarAnyPublicacio(llibres, 1, 1995);
        assertEquals(1995, llibres.get(0).getAny(), "L'any del llibre amb id 1 hauria de ser 1995.");
        assertEquals(2005, llibres.get(1).getAny(), "Només s'ha de modificar el llibre amb id 1.");
    }

    @Test
    void testAfegirNouLlibre() {
        List<Llibre> llibres = app.carregarLlibres();
        app.afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
        assertEquals(4, llibres.size(), "Hauria d'haver-hi quatre llibres després d'afegir-ne un nou.");
        assertEquals("Històries de la ciutat", llibres.get(3).getTitol(), "El títol del quart llibre hauria de ser 'Històries de la ciutat'.");
    }

    @Test
    void testEsborrarLlibre() {
        List<Llibre> llibres = app.carregarLlibres();
        app.esborrarLlibre(llibres, 2);
        assertEquals(2, llibres.size(), "Hauria d'haver-hi dos llibres després d'esborrar el llibre amb id 2.");
        assertTrue(llibres.stream().noneMatch(llibre -> llibre.getId() == 2), "El llibre amb id 2 hauria d'haver estat esborrat.");
    }

    @Test
    void testGuardarLlibres() throws IOException {
        List<Llibre> llibres = app.carregarLlibres();
        app.modificarAnyPublicacio(llibres, 1, 1995);
        app.afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
        app.esborrarLlibre(llibres, 2);
        app.guardarLlibres(llibres);

        // Llegeix el fitxer de sortida i verifica el contingut
        File outputFile = new File(tempFile.getParent(), "llibres_output_jakarta.json");
        assertTrue(outputFile.exists(), "El fitxer de sortida llibres_output_jakarta.json hauria d'existir.");

        try (JsonReader reader = Json.createReader(Files.newBufferedReader(outputFile.toPath(), StandardCharsets.UTF_8))) {
            JsonArray jsonArray = reader.readArray();
            assertEquals(3, jsonArray.size(), "Hauria d'haver-hi tres llibres al fitxer de sortida.");

            JsonObject llibre1 = jsonArray.getJsonObject(0);
            assertEquals(1995, llibre1.getInt("any"), "L'any del llibre amb id 1 hauria de ser 1995 al fitxer de sortida.");

            JsonObject llibre3 = jsonArray.getJsonObject(2);
            assertEquals("Històries de la ciutat", llibre3.getString("titol"), "El títol del tercer llibre hauria de ser 'Històries de la ciutat' al fitxer de sortida.");
            assertEquals("Miquel Soler", llibre3.getString("autor"), "L'autor del tercer llibre hauria de ser 'Miquel Soler' al fitxer de sortida.");
        }
    }

    @Test
    void testProcessarFitxerNoModificaEntrada() throws IOException {
        app.processarFitxer();

        // El fitxer d'entrada no s'ha de modificar: els canvis es guarden en un fitxer nou
        String contingutEntrada = Files.readString(tempFile.toPath(), StandardCharsets.UTF_8);
        assertEquals(JSON_CONTENT, contingutEntrada, "El fitxer d'entrada no s'ha de modificar.");
        assertTrue(new File(tempDir, "llibres_output_jakarta.json").exists(), "processarFitxer() hauria de generar el fitxer de sortida.");
    }
}
