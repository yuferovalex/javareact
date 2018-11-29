package edu.yuferov.site.repository.impl;

import edu.yuferov.site.model.Role;
import edu.yuferov.site.model.User;
import edu.yuferov.site.repository.RoleRepository;
import edu.yuferov.site.repository.UserRepository;
import edu.yuferov.site.util.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends RepositoryBase implements UserRepository {

    @Autowired
    private RoleRepository roleRepository;

    private Logger log = LogManager.getLogger(RoleRepository.class);

    @Override
    public void save(User user) {
        exec(connection -> {
            if (user.getId() != null) {
                update(user, connection);
            } else {
                create(user, connection);
            }
        });
    }

    private void update(User user, Connection connection) throws SQLException {
        final String sql = "UPDATE users SET name = ?, role_id = ? WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.setLong(2, user.getRole().getId());
        statement.setLong(3, user.getId());
        statement.executeUpdate();
        int updateCount = statement.getUpdateCount();
        if (updateCount == 0) {
            throw new IllegalArgumentException("User with such id not found");
        }
    }

    private void create(User user, Connection connection) throws SQLException {
        final String sql = "INSERT INTO users (name, role_id) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, user.getName());
        statement.setLong(2, user.getRole().getId());
        statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        Assert.assertTrue(generatedKeys.next());
        user.setId(generatedKeys.getLong("user_id"));
    }

    @Override
    public Optional<User> findById(Long id) {
        final Optional<User>[] foundUser = new Optional[]{ Optional.empty() };
        exec(connection -> {
            final String sql = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                foundUser[0] = Optional.ofNullable(createUserFromResultSet(resultSet));
            }
        });
        return foundUser[0];
    }

    @Override
    public Iterable<User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        exec(connection -> {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                users.add(createUserFromResultSet(resultSet));
            }
        });
        return users;
    }

    @Override
    public long count() {
        final Long[] total = {0L};
        exec(connection -> {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM users");
            Assert.assertTrue(resultSet.next());
            total[0] = resultSet.getLong("total");
        });
        return total[0];
    }

    @Override
    public void delete(User user) {
        deleteById(user.getId());
    }

    @Override
    public void deleteById(Long primaryKey) {
        if (primaryKey == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        exec(connection -> {
            final String sql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, primaryKey);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            if (updateCount == 0) {
                throw new IllegalArgumentException("User with such id not found");
            }
        });
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        user.setName(resultSet.getString("name"));
        Optional<Role> role = roleRepository.findById(resultSet.getLong("role_id"));
        user.setRole(role.get());
        return user;
    }
}
