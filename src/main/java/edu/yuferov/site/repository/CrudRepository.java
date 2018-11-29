package edu.yuferov.site.repository;

import java.util.Optional;

public interface CrudRepository<Entity, PrimaryKey> {
    void save(Entity user);
    Optional<Entity> findById(PrimaryKey primaryKey);
    Iterable<Entity> findAll();
    long count();
    void delete(Entity user);
    void deleteById(PrimaryKey primaryKey);
    boolean existsById(PrimaryKey primaryKey);
}
