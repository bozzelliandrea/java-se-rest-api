package database.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Deserializable {

    static <T> T of(InputStream is, T obj) throws Exception {

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (is, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

        String json = textBuilder.toString().replaceAll("[ \n{}\"]", "");

        for (String prop : json.split(",")) {
            Field field = obj.getClass().getDeclaredField(prop.substring(0, prop.indexOf(':')));
            field.setAccessible(true);
            String val = prop.substring(prop.indexOf(':') + 1);
            if (field.getType().equals(Integer.class))
                field.set(obj, Integer.valueOf(val));
            else
                field.set(obj, val);
        }

        return obj;
    }
}
