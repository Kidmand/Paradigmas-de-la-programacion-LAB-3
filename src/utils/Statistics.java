package utils;

import java.util.ArrayList;
import java.util.List;

import namedEntities.NamedEntityStorage;

/**
 * Esta clase se encarga de imprimir las estadísticas de las entidades
 * nombradas.
 */
public class Statistics {

    private NamedEntityStorage namedEntityStorage;

    /**
     * Constructor de la clase.
     * 
     * @param namedEntityStorage Almacén de entidades nombradas que se van a
     *                           analizar.
     */
    public Statistics(NamedEntityStorage namedEntityStorage) {
        this.namedEntityStorage = namedEntityStorage;
    }

    /**
     * Imprime una estadística relacionada con las categorías de las entidades.
     */
    public void printAnalysisCategories() {
        for (String category : namedEntities.definitions.Categories.getCategories()) {
            List<String> labels = namedEntityStorage.getCategoryLabels(category);
            if (labels.size() == 0) {
                continue;
            }
            System.out.println("Category: " + category);
            for (String label : labels) {
                System.out.println("          " + label + " (" + namedEntityStorage.getEntityCountByLabel(label) + ")");
            }
        }
    }

    /**
     * Imprime una estadística relacionada con los tópicos de las entidades.
     */
    public void printAnalysisTopics() {
        for (String topic : namedEntityStorage.getAllTopics()) {
            List<String> labels = namedEntityStorage.getTopicLabels(topic);
            if (labels.size() == 0) {
                continue;
            }
            System.out.println("Topic: " + topic);
            for (String label : labels) {
                System.out.println("          " + label + " (" + namedEntityStorage.getEntityCountByLabel(label) + ")");
            }
        }
    }

    /*
     * Imprime una estadística relacionada con las entidades nombradas que se
     * repiten. Mostrándolas en orden de menos a más.
     */
    public void printAnalysisMoreRepeatedInOrder() {
        List<String> labels = new ArrayList<>(namedEntityStorage.getAllLabels());

        labels.sort(this::compareByEntityCount);

        System.out.println("More repeated named entities:");
        for (String label : labels) {
            System.out.println("          " + label + " (" + namedEntityStorage.getEntityCountByLabel(label) + ")");
        }
    }

    private int compareByEntityCount(String labelA, String labelB) {
        int countA = namedEntityStorage.getEntityCountByLabel(labelA);
        int countB = namedEntityStorage.getEntityCountByLabel(labelB);
        return Integer.compare(countA, countB);
    }

}
