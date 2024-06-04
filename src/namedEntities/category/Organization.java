package namedEntities.category;

import java.util.List;

import namedEntities.NameEntity;
import namedEntities.definitions.Categories;

public class Organization extends NameEntity {
    private int foundationYear;

    public Organization(String label, List<String> topics) {
        super(label, Categories.ORGANIZATION, topics);
        this.foundationYear = -1; // TODO: Check if this is the best way to represent an unknown value
        // OPTIONAL: Usar strings y ponerles undefined
    }

    public Organization(String label, List<String> topics, int foundationYear) {
        super(label, Categories.ORGANIZATION, topics);
        this.foundationYear = foundationYear;
    }

    public int getFoundationYear() {
        return foundationYear;
    }

    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }
}