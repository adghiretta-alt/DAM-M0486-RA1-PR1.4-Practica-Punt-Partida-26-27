package com.project.exemples;

import com.project.objectes.Persona;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Exemple que llegeix, modifica i guarda una llista de persones amb Jakarta JSON Processing
 * (model d'objectes: JsonArray, JsonObject...).
 */
public class GestioPersonesJakartaMain {

    private final File dataFile;

    /**
     * Constructor de la classe GestioPersonesJakartaMain.
     *
     * @param dataFile Fitxer on es troben les persones.
     */
    public GestioPersonesJakartaMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        // Defineix la ruta del fitxer de dades d'entrada (persones_input.json)
        File dataFile = new File(System.getProperty("user.dir"), "data/exemples" + File.separator + "persones_input.json");
        GestioPersonesJakartaMain app = new GestioPersonesJakartaMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades de les persones.
     */
    public void processarFitxer() {
        List<Persona> persones = carregarPersones();
        if (persones != null) {
            modificarEdat(persones, 1, 35);
            afegirNovaPersona(persones, new Persona(4, "Miquel Soler", 30));
            esborrarPersona(persones, 2);
            guardarPersones(persones);
        }
    }

    /**
     * Carrega les persones des del fitxer JSON utilitzant Jakarta JSON.
     *
     * @return Llista de persones o null si hi ha hagut un error en la lectura.
     */
    public List<Persona> carregarPersones() {
        try (JsonReader jsonReader = Json.createReader(new FileReader(dataFile, StandardCharsets.UTF_8))) {
            List<Persona> persones = new ArrayList<>();
            JsonArray jsonArray = jsonReader.readArray();
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                int id = jsonObject.getInt("id");
                String nom = jsonObject.getString("nom");
                int edat = jsonObject.getInt("edat");
                persones.add(new Persona(id, nom, edat));
            }
            return persones;
        } catch (IOException | JsonException e) {
            // IOException: el fitxer no existeix o no es pot llegir
            // JsonException (no comprovada): el contingut no és un JSON vàlid
            System.err.println("Error en llegir " + dataFile.getName() + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Modifica l'edat d'una persona amb un id específic.
     *
     * @param persones Llista de persones.
     * @param id Identificador de la persona a modificar.
     * @param novaEdat Nova edat.
     */
    public void modificarEdat(List<Persona> persones, int id, int novaEdat) {
        for (Persona persona : persones) {
            if (persona.getId() == id) {
                persona.setEdat(novaEdat);
            }
        }
    }

    /**
     * Afegeix una nova persona a la llista de persones.
     *
     * @param persones Llista de persones.
     * @param novaPersona Nova persona a afegir.
     */
    public void afegirNovaPersona(List<Persona> persones, Persona novaPersona) {
        persones.add(novaPersona);
    }

    /**
     * Esborra una persona amb un id específic de la llista de persones.
     *
     * @param persones Llista de persones.
     * @param id Identificador de la persona a esborrar.
     */
    public void esborrarPersona(List<Persona> persones, int id) {
        persones.removeIf(persona -> persona.getId() == id);
    }

    /**
     * Guarda la llista de persones en un fitxer nou utilitzant Jakarta JSON.
     *
     * @param persones Llista de persones a guardar.
     */
    public void guardarPersones(List<Persona> persones) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Persona persona : persones) {
            JsonObject personaJson = Json.createObjectBuilder()
                .add("id", persona.getId())
                .add("nom", persona.getNom())
                .add("edat", persona.getEdat())
                .build();
            arrayBuilder.add(personaJson);
        }

        JsonArray jsonArray = arrayBuilder.build();
        try (Writer writer = Files.newBufferedWriter(Paths.get(dataFile.getParent(), "persones_output_jakarta.json"), StandardCharsets.UTF_8);
             JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.writeArray(jsonArray);
        } catch (IOException | JsonException e) {
            System.err.println("Error en guardar les persones: " + e.getMessage());
        }
    }
}
