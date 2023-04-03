package server.model;

public class Car implements Serializable {

    private String description;
    private String name;
    private String color;

    public String getName() {
        return this.name;
    }

    @Override
    public String serialize() {
        return "{" +
                "\"description\": \"" + description + '\"' +
                ",\"name\": \"" + name + '\"' +
                ",\"color\": \"" + color + '\"' +
                '}';
    }
}
