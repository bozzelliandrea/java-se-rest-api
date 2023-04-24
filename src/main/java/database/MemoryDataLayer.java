package database;

import database.model.Car;
import database.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryDataLayer {

    public static final Map<Integer, Car> car = new HashMap<>();
    public static final AtomicInteger carSequence = new AtomicInteger();
    public static final Map<String, User> user = new HashMap<>();
    public static final Set<String> jwtBlacklist = new HashSet<>();

    static {
        Car c = new Car(carSequence.incrementAndGet(),
                "a nice card",
                "ford",
                "blue");
        car.put(c.getId(), c);
        c = new Car(carSequence.incrementAndGet(),
                "very expensive car",
                "ferrari",
                "yellow");
        car.put(c.getId(), c);
        c = new Car(carSequence.incrementAndGet(),
                "kind of a big deal",
                "volkswagen",
                "rust");
        car.put(c.getId(), c);

        User admin = new User("admin",
                "admin@server.com",
                "admin");
        user.put(admin.getName(), admin);
    }
}
