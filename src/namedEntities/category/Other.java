package namedEntities.category;

import java.util.ArrayList;
import java.util.List;

import namedEntities.NameEntity;
import namedEntities.definitions.Categories;

public class Other extends NameEntity {

    // TODO: Add more attributes if needed.

    public Other(String label) {
        super(label, Categories.OTHER, createListTopicsOTHER());
    }

    private static List<String> createListTopicsOTHER() {
        List<String> res = new ArrayList<>();
        res.add(Categories.OTHER);
        return res;
    }
}
