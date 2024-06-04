package namedEntities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import namedEntities.category.*;
import namedEntities.definitions.Categories;
import namedEntities.dictionary.*;

/**
 * Esta clase funciona como un almacenamiento de entidades nombradas que se
 * pueden acceder a través de una etiqueta.
 */
public class NamedEntityStorage extends Storage<NameEntity> {
    private DictionaryStorage dictionary;

    // Almacenamiento de la cantidad de entidades nombradas por etiqueta.
    private Storage<Integer> labelEntityCount;

    /**
     * Constructor de la clase.
     * 
     * @param dictionary Diccionario de entidades nombradas.
     * @apiNote Notar que NamedEntityStorage usa uan referencia de dictionary.
     */
    public NamedEntityStorage(DictionaryStorage dictionary) {
        labelEntityCount = new Storage<Integer>();

        this.dictionary = dictionary;
    }

    /**
     * Agrega un elemento al almacenamiento de entidades nombradas.
     * 
     * @param word Palabra a agregar.
     * 
     * @apiNote Si la palabra no se encuentra en el diccionario, se agrega como una
     *          entidad nombrada de tipo `OTHER`. Es decir que su categoría es
     *          `OTHER` y tiene un único tópico `OTHER`.
     */
    public void addElement(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }

        String label = dictionary.findLabelForWord(word);

        if (dictionary.labelExists(label)) {
            DictNameEntity dictEntity = dictionary.accessValue(label);

            if (this.labelExists(label)) {
                // Update label for count
                labelEntityCount.remplaceValue(label, labelEntityCount.accessValue(label) + 1);
            } else {
                // Add new label for count
                labelEntityCount.addValue(label, 1);

                String category = dictEntity.getCategory();

                if (category.equals(Categories.PERSON)) {
                    addValue(label, new Person(label, dictEntity.getTopics()));
                } else if (category.equals(Categories.ORGANIZATION)) {
                    addValue(label, new Organization(label, dictEntity.getTopics()));
                } else if (category.equals(Categories.LOCATION)) {
                    addValue(label, new Location(label, dictEntity.getTopics()));
                } else {
                    addValue(label, new Other(label));
                }
            }
        } else {
            // NOTE: Se podrían normalizar esta palabras que no están en el diccionario.
            // Pasarlas a minúscula por ejemplo.
            //
            // word = word.toLowerCase();

            // Add new label for this word
            // NOTE: En esta parte revisamos si la palabra esta en el almacenamiento de
            // entidades nombradas.
            if (this.labelExists(word)) {
                // Update label for count
                labelEntityCount.remplaceValue(word, labelEntityCount.accessValue(word) + 1);
            } else {
                // Create new entity
                labelEntityCount.addValue(word, 1);
                addValue(word, new Other(word));
            }
        }

    }

    /**
     * Obtiene la cantidad de entidades nombradas por etiqueta.
     * 
     * @param label Etiqueta de la entidad.
     * @return Cantidad de entidades nombradas.
     * 
     * @apiNote Si la etiqueta no existe, se retorna 0.
     */
    public Integer getEntityCountByLabel(String label) {
        if (!labelEntityCount.labelExists(label)) {
            return 0;
        } else {
            return labelEntityCount.accessValue(label);
        }
    }

    /**
     * Obtiene las etiquetas de las entidades nombradas de una categoría dada.
     * 
     * @param category Categoría de la entidad.
     * 
     * @return Lista de etiquetas de las entidades nombradas de la categoría dada.
     */
    public List<String> getCategoryLabels(String category) {
        List<String> labels = new ArrayList<String>();

        for (String label : getAllLabels()) {
            if (accessValue(label).isCategory(category)) {
                labels.add(label);
            }
        }
        return labels;
    }

    /**
     * Obtiene las etiquetas de las entidades nombradas de un tópico dado.
     * 
     * @param topic Tópico de la entidad.
     * 
     * @return Lista de etiquetas de las entidades nombradas del tópico dado.
     */
    public List<String> getTopicLabels(String topic) {
        List<String> labels = new ArrayList<String>();

        for (String label : getAllLabels()) {
            if (accessValue(label).isTopic(topic)) {
                labels.add(label);
            }
        }

        return labels;
    }

    public List<String> getAllTopics() {
        Set<String> topics = new HashSet<>();

        for (NameEntity nameEntry : this.getAllValues()) {
            topics.addAll(nameEntry.getTopics());
        }

        return new ArrayList<>(topics);
    }

    /**
     * Imprime las entidades nombradas en el siguiente formato:
     * <code>
     *  Named Entities:
     *       [  (Marta - 2)  (Pepe - 1)  (Sofia - 1)  ]
     * </code>
     * 
     * @apiNote Imprime las etiquetas de las entidades nombradas y la cantidad de
     *          entidades nombradas por etiqueta.
     **/
    public void print() {
        System.out.println("Named Entities:");
        System.out.print("[  ");
        for (String label : getAllLabels()) {
            System.out.print("(" + label + " - " + getEntityCountByLabel(label) + ")  ");
        }
        System.out.println("]");
    }

}
