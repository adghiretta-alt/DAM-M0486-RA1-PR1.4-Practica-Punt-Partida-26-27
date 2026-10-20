package com.project.pr14;

import com.project.objectes.Llibre;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class PR14GestioLlibreriaJacksonMain {

    private final File dataFile =
            new File(System.getProperty("user.dir"), "data/pr14/llibres_input.json");

    public List<Llibre> carregarLlibres() {
        // *************** CODI PRÀCTICA **********************/
        ObjectMapper mapper = JsonMapper.builder().build();

        try {
            return mapper.readValue(
                    dataFile,
                    new TypeReference<List<Llibre>>() {}
            );
        } catch (JacksonException e) {
            System.out.println("Error carregant els llibres: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error llegint el fitxer: " + e.getMessage());
            return null;
        }
    }

    public void modificarAnyPublicacio(List<Llibre> llibres, int id, int nouAny) {
        // *************** CODI PRÀCTICA **********************/
        if (llibres != null) {
            for (Llibre llibre : llibres) {
                if (llibre.getId() == id) {
                    llibre.setAny(nouAny);
                    return;
                }
            }
        }
    }

    public void afegirNouLlibre(List<Llibre> llibres, Llibre nouLlibre) {
        // *************** CODI PRÀCTICA **********************/
        if (llibres != null && nouLlibre != null) {
            llibres.add(nouLlibre);
        }
    }

    public void esborrarLlibre(List<Llibre> llibres, int id) {
        // *************** CODI PRÀCTICA **********************/
        if (llibres != null) {
            llibres.removeIf(llibre -> llibre.getId() == id);
        }
    }

    public void guardarLlibres(List<Llibre> llibres) {
        // *************** CODI PRÀCTICA **********************/
        if (llibres == null) {
            System.out.println("No es poden guardar els llibres: la llista és null.");
            return;
        }

        ObjectMapper mapper = JsonMapper.builder().build();
        File outputFile = new File(dataFile.getParentFile(),
                "llibres_output_jackson.json");

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputFile, llibres);
        } catch (Exception e) {
            System.out.println("Error guardant els llibres: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PR14GestioLlibreriaJacksonMain gestio =
                new PR14GestioLlibreriaJacksonMain();

        List<Llibre> llibres = gestio.carregarLlibres();

        if (llibres == null) {
            return;
        }

        gestio.modificarAnyPublicacio(llibres, 1, 1995);

        gestio.afegirNouLlibre(
                llibres,
                new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022)
        );

        gestio.esborrarLlibre(llibres, 2);
        gestio.guardarLlibres(llibres);
    }
}
