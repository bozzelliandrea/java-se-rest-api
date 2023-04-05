package database.repository;

import database.MemoryDataLayer;
import database.model.Car;

import java.util.Collection;
import java.util.LinkedList;

public class CarRepository implements Repository<Integer, Car> {

    @Override
    public int count() {
        return MemoryDataLayer.car.size();
    }

    @Override
    public Car findById(Integer id) {
        return MemoryDataLayer.car.get(id);
    }

    @Override
    public Collection<Car> findAll() {
        return MemoryDataLayer.car.values();
    }

    @Override
    public Car save(Car car) {
        car.setId(MemoryDataLayer.carSequence.incrementAndGet());
        MemoryDataLayer.car.put(car.getId(), car);
        return car;
    }

    @Override
    public Collection<Car> saveAll(Collection<Car> cars) {
        Collection<Car> res = new LinkedList<>();
        for (Car c : cars) {
            res.add(save(c));
        }
        return res;
    }

    @Override
    public int delete(Integer id) {
        return MemoryDataLayer.car.remove(id) != null ? 1 : 0;
    }

    @Override
    public int deleteAll() {
        int size = count();
        MemoryDataLayer.car.clear();
        return size;
    }
}
