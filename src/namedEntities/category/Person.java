package namedEntities.category;

import java.util.List;

import namedEntities.NameEntity;
import namedEntities.definitions.Categories;

public class Person extends NameEntity {
    private String gender;
    private int age;

    public Person(String label, List<String> topics) {
        super(label, Categories.PERSON, topics);
        this.gender = "undefined";
        this.age = -1; // TODO: Check if this is the best way to represent an unknown value
    }

    public Person(String label, List<String> topics, String gender, int age) {
        super(label, Categories.PERSON, topics);
        this.gender = gender;
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}