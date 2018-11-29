package edu.yuferov.site.repository.impl;

import edu.yuferov.site.model.Role;
import edu.yuferov.site.repository.RoleRepository;
import edu.yuferov.site.util.Assert;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends RepositoryBase implements RoleRepository {

    @Override
    public void save(Role role) {
        exec(connection -> {
            if (role.getId() != null) {
                update(role, connection);
            } else {
                create(role, connection);
            }
        });
    }

    private void update(Role role, Connection connection) throws SQLException {
        final String sql = "UPDATE roles SET title = ?, description = ? WHERE role_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, role.getTitle());
        statement.setString(2, role.getDescription());
        statement.setLong(3, role.getId());
        statement.executeUpdate();
        int updateCount = statement.getUpdateCount();
        if (updateCount == 0) {
            throw new IllegalArgumentException("Role with such id not found");
        }
    }

    private void create(Role role, Connection connection) throws SQLException {
        final String sql = "INSERT INTO roles (title, description) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, role.getTitle());
        statement.setString(2, role.getDescription());
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();
        Assert.assertTrue(result.next());
        Long id = result.getLong("role_id");
        role.setId(id);
    }

    @Override
    public Optional<Role> findById(Long id) {
        final Optional<Role>[] foundRole = new Optional[]{Optional.empty()};
        exec(connection -> {
            final String sql = "SELECT * FROM roles WHERE role_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Role role = new Role();
                role.setId(id);
                role.setTitle(result.getString("title"));
                role.setDescription(result.getString("description"));
                foundRole[0] = Optional.ofNullable(role);
            }
        });
        return foundRole[0];
    }

    @Override
    public Iterable<Role> findAll() {
        List<Role> allRoles = new ArrayList<>();
        exec(connection -> {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM roles");
            while (result.next()) {
                Role role = new Role();
                role.setId(result.getLong("role_id"));
                role.setTitle(result.getString("title"));
                role.setDescription(result.getString("description"));
                allRoles.add(role);
            }
        });
        return allRoles;
    }

    @Override
    public long count() {
        final Long[] count = {0L};
        exec(connection -> {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT COUNT(*) AS total FROM roles");
            Assert.assertTrue(result.next());
            count[0] = result.getLong("total");
        });
        return count[0];
    }

    @Override
    public void delete(Role role) {
        deleteById(role.getId());
    }

    @Override
    public void deleteById(Long primaryKey) {
        if (primaryKey == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        exec(connection -> {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM roles WHERE role_id = ?");
            statement.setLong(1, primaryKey);
            statement.executeQuery();
            int updateCount = statement.getUpdateCount();
            if (updateCount == 0) {
                throw new IllegalArgumentException("Role with such id not found");
            }
        });
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
}
