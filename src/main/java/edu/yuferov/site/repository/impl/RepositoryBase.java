package edu.yuferov.site.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class RepositoryBase {
    protected Logger log = LogManager.getLogger(RepositoryBase.class);

    @Autowired
    protected DataSource dataSource;

    protected interface Command {
        void run(Connection connection) throws SQLException;
    }

    protected void exec(Command cmd) {
        try (Connection connection = dataSource.getConnection()) {
            cmd.run(connection);
        } catch (SQLException e) {
            log.error("an exception thrown", e);
            throw new RuntimeException();
        }
    }

}
