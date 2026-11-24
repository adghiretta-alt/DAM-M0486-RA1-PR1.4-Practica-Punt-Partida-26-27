package com.project.pr14;

import com.project.objectes.Llibre;
import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Programa que gestiona libros almacenados
 * en un fichero JSON utilizando Jakarta JSON-P.
 */
public class PR14GestioLlibreriaJakartaMain {

    // Archivo JSON de entrada
    private final File dataFile;

    /**
     * Constructor.
     */
    public PR14GestioLlibreriaJakartaMain(
            File dataFile) {

        this.dataFile = dataFile;
    }

    /**
     * Punto de entrada del programa.
     */
    public static void main(String[] args) {

        File dataFile = new File(
                System.getProperty("user.dir"),
                "data/pr14" + File.separator +
                        "llibres_input.json"
        );

        PR14GestioLlibreriaJakartaMain app =
                new PR14GestioLlibreriaJakartaMain(
                        dataFile
                );

        app.processarFitxer();
    }

    /**
     * Ejecuta todo el proceso:
     * cargar, modificar, añadir,
     * eliminar y guardar.
     */
    public void processarFitxer() {

        // Cargar libros desde el JSON
        List<Llibre> llibres =
                carregarLlibres();

        if (llibres != null) {

            // Modificar año del libro con id 1
            modificarAnyPublicacio(
                    llibres,
                    1,
                    1995
            );

            // Añadir un nuevo libro
            afegirNouLlibre(
                    llibres,
                    new Llibre(
                            4,
                            "Històries de la ciutat",
                            "Miquel Soler",
                            2022
                    )
            );

            // Eliminar libro con id 2
            esborrarLlibre(
                    llibres,
                    2
            );

            // Guardar resultado final
            guardarLlibres(llibres);
        }
    }

    /**
     * Lee el JSON y lo convierte
     * en una lista de libros.
     */
    public List<Llibre> carregarLlibres() {

        // Comprobar que exista el archivo
        if (dataFile == null
                || !dataFile.exists()) {

            System.err.println(
                    "Error: El fitxer no existeix."
            );

            return null;
        }

        try (

                /*
                 * Abrir flujo de lectura.
                 */
                InputStream inputStream =
                        Files.newInputStream(
                                dataFile.toPath()
                        );

                /*
                 * JsonReader permite leer
                 * contenido JSON.
                 */
                JsonReader jsonReader =
                        Json.createReader(
                                new InputStreamReader(
                                        inputStream,
                                        StandardCharsets.UTF_8
                                )
                        )

        ) {

            /*
             * Leer el JSON completo
             * como array.
             */
            JsonArray jsonArray =
                    jsonReader.readArray();

            List<Llibre> llibres =
                    new ArrayList<>();

            /*
             * Recorrer cada objeto JSON.
             */
            for (JsonObject jsonObject :
                    jsonArray.getValuesAs(
                            JsonObject.class
                    )) {

                /*
                 * JSON:
                 *
                 * {
                 *   "id": 1,
                 *   "titol": "Java",
                 *   "autor": "Joan",
                 *   "any": 2020
                 * }
                 */

                int id =
                        jsonObject.getInt("id");

                String titol =
                        jsonObject.getString("titol");

                String autor =
                        jsonObject.getString("autor");

                int any =
                        jsonObject.getInt("any");

                /*
                 * Crear objeto Llibre
                 * a partir de los datos.
                 */
                llibres.add(
                        new Llibre(
                                id,
                                titol,
                                autor,
                                any
                        )
                );
            }

            return llibres;

        } catch (IOException | JsonException e) {

            System.err.println(
                    "Error en llegir o processar el fitxer JSON: "
                            + e.getMessage()
            );

            return null;
        }
    }

    /**
     * Modifica el año de publicación
     * de un libro.
     */
    public void modificarAnyPublicacio(
            List<Llibre> llibres,
            int id,
            int nouAny) {

        if (llibres != null) {

            // Recorrer todos los libros
            for (Llibre llibre : llibres) {

                // Buscar el libro indicado
                if (llibre.getId() == id) {

                    // Cambiar año
                    llibre.setAny(nouAny);
                }
            }
        }
    }

    /**
     * Añade un nuevo libro.
     */
    public void afegirNouLlibre(
            List<Llibre> llibres,
            Llibre nouLlibre) {

        if (llibres != null
                && nouLlibre != null) {

            // Añadir libro a la lista
            llibres.add(nouLlibre);
        }
    }

    /**
     * Elimina un libro por ID.
     */
    public void esborrarLlibre(
            List<Llibre> llibres,
            int id) {

        if (llibres != null) {

            /*
             * removeIf elimina los elementos
             * que cumplan la condición.
             */
            llibres.removeIf(
                    llibre ->
                            llibre.getId() == id
            );
        }
    }

    /**
     * Guarda los libros en
     * llibres_output_jakarta.json
     */
    public void guardarLlibres(
            List<Llibre> llibres) {

        if (llibres == null
                || dataFile == null) {

            return;
        }

        /*
         * JsonArrayBuilder sirve para
         * construir un array JSON.
         */
        JsonArrayBuilder arrayBuilder =
                Json.createArrayBuilder();

        /*
         * Convertir cada libro
         * a objeto JSON.
         */
        for (Llibre llibre : llibres) {

            /*
             * JsonObjectBuilder crea:
             *
             * {
             *   "id":1,
             *   "titol":"Java"
             * }
             */
            JsonObject llibreJson =
                    Json.createObjectBuilder()

                            .add(
                                    "id",
                                    llibre.getId()
                            )

                            .add(
                                    "titol",
                                    llibre.getTitol()
                            )

                            .add(
                                    "autor",
                                    llibre.getAutor()
                            )

                            .add(
                                    "any",
                                    llibre.getAny()
                            )

                            .build();

            // Añadir objeto al array
            arrayBuilder.add(llibreJson);
        }

        /*
         * Crear el array JSON final.
         */
        JsonArray jsonArray =
                arrayBuilder.build();

        // Ruta del archivo de salida
        Path outputPath =
                Paths.get(
                        dataFile.getParent(),
                        "llibres_output_jakarta.json"
                );

        try (

                /*
                 * Abrir archivo para escritura.
                 */
                Writer writer =
                        Files.newBufferedWriter(
                                outputPath,
                                StandardCharsets.UTF_8
                        );

                /*
                 * JsonWriter escribe JSON.
                 */
                JsonWriter jsonWriter =
                        Json.createWriter(writer)

        ) {

            /*
             * Guardar array completo.
             */
            jsonWriter.writeArray(
                    jsonArray
            );

        } catch (IOException | JsonException e) {

            System.err.println(
                    "Error en guardar el fitxer JSON: "
                            + e.getMessage()
            );
        }
    }
}
