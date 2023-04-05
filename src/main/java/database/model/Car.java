package database.model;

import database.repository.AutoGenerateId;
import database.repository.Id;

@AutoGenerateId
public class Car implements Serializable {

    @Id
    private Integer id;
    private String description;
    private String name;
    private String color;

    public Car() {
    }

    public Car(Integer id, String description, String name, String color) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.color = color;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String serialize() {
        return "{" +
                "\"id\": \"" + id + '\"' +
                ",\"description\": \"" + description + '\"' +
                ",\"name\": \"" + name + '\"' +
                ",\"color\": \"" + color + '\"' +
                '}';
    }
}
