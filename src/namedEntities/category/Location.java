package namedEntities.category;

import java.util.List;

import namedEntities.NameEntity;
import namedEntities.definitions.Categories;

public class Location extends NameEntity {
    private int altitud;
    private int latitud;

    public Location(String label, List<String> topics) {
        super(label, Categories.LOCATION, topics);
        this.altitud = 0;
        this.latitud = 0;
    }

    public Location(String label, List<String> topics, int altitud, int latitud) {
        super(label, Categories.LOCATION, topics);
        this.altitud = altitud;
        this.latitud = latitud;
    }

    public int getAltitud() {
        return altitud;
    }

    public void setAltitud(int altitud) {
        this.altitud = altitud;
    }

    public int getLatitud() {
        return latitud;
    }

    public void setLatitud(int latitud) {
        this.latitud = latitud;
    }
}
