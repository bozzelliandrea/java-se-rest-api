package database.model;

public class User implements Serializable {
    private final Integer id;
    private final String name;
    private final String email;
    private final String password;

    public User(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String serialize() {
        return "{" +
                "\"id\": \"" + id + '\"' +
                ",\"name\": \"" + name + '\"' +
                ",\"email\": \"" + email + '\"' +
                '}';
    }
}
