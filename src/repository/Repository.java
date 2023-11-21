package repository;

import java.util.List;

public interface Repository<T>{
    T add(T entity);

    void delete(Integer id);

    void update(T entity);

    T findOne(Integer id);

    List<T> findAll();
}
