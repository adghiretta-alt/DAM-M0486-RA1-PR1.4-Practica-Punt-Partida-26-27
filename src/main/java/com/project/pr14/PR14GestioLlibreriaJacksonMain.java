package com.project.pr14;

import com.project.objectes.Llibre;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;

import java.io.File;
import java.util.List;

/**
 * Programa que gestiona una colección de libros
 * almacenados en un fichero JSON usando Jackson.
 */
public class PR14GestioLlibreriaJacksonMain {

    // Fichero JSON de entrada
    private final File dataFile;

    /**
     * Constructor por defecto.
     */
    public PR14GestioLlibreriaJacksonMain() {

        this.dataFile = new File(
                System.getProperty("user.dir"),
                "data/pr14/llibres_input.json"
        );
    }

    /**
     * Constructor para utilizar otro archivo.
     */
    public PR14GestioLlibreriaJacksonMain(
            File dataFile) {

        this.dataFile = dataFile;
    }

    /**
     * Lee el archivo JSON y lo convierte
     * en una lista de objetos Llibre.
     */
    public List<Llibre> carregarLlibres() {

        /*
         * ObjectMapper es el componente principal
         * de Jackson.
         *
         * Se utiliza para convertir:
         *
         * JSON -> Objetos Java
         * Objetos Java -> JSON
         */
        ObjectMapper mapper =
                JsonMapper.builder().build();

        try {

            /*
             * readValue()
             * lee el contenido del JSON.
             *
             * Ejemplo JSON:
             *
             * [
             *   {
             *      "id":1,
             *      "titol":"Java",
             *      "autor":"Joan",
             *      "any":2020
             *   }
             * ]
             *
             * TypeReference<List<Llibre>>
             * indica que queremos convertir
             * el JSON a una lista de libros.
             */
            return mapper.readValue(
                    dataFile,
                    new TypeReference<List<Llibre>>() {}
            );

        } catch (JacksonException e) {

            System.out.println(
                    "Error carregant els llibres: "
                    + e.getMessage()
            );

            return null;

        } catch (Exception e) {

            System.out.println(
                    "Error llegint el fitxer: "
                    + e.getMessage()
            );

            return null;
        }
    }

    /**
     * Modifica el año de publicación
     * de un libro concreto.
     */
    public void modificarAnyPublicacio(
            List<Llibre> llibres,
            int id,
            int nouAny) {

        if (llibres != null) {

            // Recorrer todos los libros
            for (Llibre llibre : llibres) {

                /*
                 * Buscar el libro cuyo id
                 * coincide con el indicado.
                 */
                if (llibre.getId() == id) {

                    // Cambiar el año
                    llibre.setAny(nouAny);

                    return;
                }
            }
        }
    }

    /**
     * Añade un nuevo libro a la lista.
     */
    public void afegirNouLlibre(
            List<Llibre> llibres,
            Llibre nouLlibre) {

        if (llibres != null
                && nouLlibre != null) {

            // Añadir libro a la colección
            llibres.add(nouLlibre);
        }
    }

    /**
     * Elimina un libro usando su ID.
     */
    public void esborrarLlibre(
            List<Llibre> llibres,
            int id) {

        if (llibres != null) {

            /*
             * removeIf()
             * elimina todos los elementos
             * que cumplen la condición.
             *
             * llibre -> es cada libro de la lista.
             */
            llibres.removeIf(
                    llibre ->
                            llibre.getId() == id
            );
        }
    }

    /**
     * Guarda la lista actualizada
     * en un nuevo archivo JSON.
     */
    public void guardarLlibres(
            List<Llibre> llibres) {

        if (llibres == null) {

            System.out.println(
                    "No es poden guardar els llibres: la llista és null."
            );

            return;
        }

        // Crear ObjectMapper
        ObjectMapper mapper =
                JsonMapper.builder().build();

        // Archivo de salida
        File outputFile =
                new File(
                        dataFile.getParentFile(),
                        "llibres_output_jackson.json"
                );

        try {

            /*
             * writerWithDefaultPrettyPrinter()
             * formatea el JSON para que
             * sea legible.
             *
             * Sin él:
             *
             * [{"id":1,"titol":"Java"}]
             *
             * Con él:
             *
             * [
             *   {
             *      "id":1,
             *      "titol":"Java"
             *   }
             * ]
             */
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            outputFile,
                            llibres
                    );

        } catch (Exception e) {

            System.out.println(
                    "Error guardant els llibres: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Ejecuta todo el proceso.
     */
    public void processarFitxer() {

        // Cargar libros desde JSON
        List<Llibre> llibres =
                carregarLlibres();

        if (llibres == null) {
            return;
        }

        /*
         * Modificar:
         *
         * libro id = 1
         * año = 1995
         */
        modificarAnyPublicacio(
                llibres,
                1,
                1995
        );

        /*
         * Añadir un nuevo libro.
         */
        afegirNouLlibre(
                llibres,
                new Llibre(
                        4,
                        "Històries de la ciutat",
                        "Miquel Soler",
                        2022
                )
        );

        /*
         * Eliminar el libro con id 2.
         */
        esborrarLlibre(
                llibres,
                2
        );

        // Guardar resultado final
        guardarLlibres(llibres);
    }

    /**
     * Punto de entrada.
     */
    public static void main(String[] args) {

        PR14GestioLlibreriaJacksonMain gestio =
                new PR14GestioLlibreriaJacksonMain();

        gestio.processarFitxer();
    }
}
