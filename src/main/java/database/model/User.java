package database.model;

public class User implements Serializable {
    private final String name;
    private final String email;
    private final String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    @Override
    public String serialize() {
        return "{" +
                ",\"name\": \"" + name + '\"' +
                ",\"email\": \"" + email + '\"' +
                '}';
    }
}
