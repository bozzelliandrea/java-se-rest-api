package database.repository;

import java.util.Collection;

public interface Repository<K, E> {

    int count();

    E findById(K id);

    Collection<E> findAll();

    E save(E e);

    Collection<E> saveAll(Collection<E> es);

    int delete(K id);

    int deleteAll();
}
