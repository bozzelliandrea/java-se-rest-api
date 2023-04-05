package database;

import database.model.Car;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryDataLayer {

    public static final Map<Integer, Car> car = new HashMap<>();
    public static final AtomicInteger carSequence = new AtomicInteger();
}
