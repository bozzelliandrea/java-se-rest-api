package database.model;

import java.util.Collection;

public interface Serializable {

    String serialize();

    static <T extends Serializable> String forArray(Collection<T> arr) {

        if (arr.isEmpty())
            return "[]";

        String json = "[ ";
        int counter = arr.size();
        for (T obj : arr) {
            counter--;
            json = json + obj.serialize() + (counter != 0 ? ',' : "");
        }
        json = json + " ]";

        return json;
    }
}
