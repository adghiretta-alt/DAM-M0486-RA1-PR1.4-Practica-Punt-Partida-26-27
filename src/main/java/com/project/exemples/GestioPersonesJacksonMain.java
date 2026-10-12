package com.project.exemples;

import com.project.objectes.Persona;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.util.List;

/**
 * Exemple que llegeix, modifica i guarda una llista de persones amb Jackson 3
 * (vinculació de dades: JSON &lt;-&gt; objectes Java).
 */
public class GestioPersonesJacksonMain {

    private final File dataFile;

    // El mapper és costós de crear i és segur entre fils: es crea una vegada i es reutilitza
    private final JsonMapper mapper = JsonMapper.builder().build();

    /**
     * Constructor de la classe GestioPersonesJacksonMain.
     *
     * @param dataFile Fitxer on es troben les persones.
     */
    public GestioPersonesJacksonMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        // Defineix la ruta del fitxer de dades d'entrada (persones_input.json)
        File dataFile = new File(System.getProperty("user.dir"), "data/exemples" + File.separator + "persones_input.json");
        GestioPersonesJacksonMain app = new GestioPersonesJacksonMain(dataFile);
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
     * Carrega les persones des del fitxer JSON.
     *
     * @return Llista de persones o null si hi ha hagut un error en la lectura.
     */
    public List<Persona> carregarPersones() {
        try {
            // TypeReference indica el tipus genèric complet: una llista de Persona
            return mapper.readValue(dataFile, new TypeReference<List<Persona>>() {});
        } catch (JacksonException e) {
            System.err.println("Error en llegir " + dataFile.getName() + ": " + e.getOriginalMessage());
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
     * Guarda la llista de persones en un fitxer nou.
     *
     * @param persones Llista de persones a guardar.
     */
    public void guardarPersones(List<Persona> persones) {
        File outputFile = new File(dataFile.getParent(), "persones_output_jackson.json");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, persones);
        } catch (JacksonException e) {
            System.err.println("Error en guardar " + outputFile.getName() + ": " + e.getOriginalMessage());
        }
    }
}
