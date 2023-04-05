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

    public String getName() {
        return this.name;
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
                "\"description\": \"" + description + '\"' +
                ",\"name\": \"" + name + '\"' +
                ",\"color\": \"" + color + '\"' +
                '}';
    }
}
