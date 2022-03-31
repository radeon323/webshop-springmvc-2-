package com.luxoft.olshevchenko.webshop.dao.jdbc;

import com.luxoft.olshevchenko.webshop.dao.UserDao;
import com.luxoft.olshevchenko.webshop.entity.Role;
import com.luxoft.olshevchenko.webshop.entity.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;

/**
 * @author Oleksandr Shevchenko
 */
@Repository
public class JdbcUserDao implements UserDao {
    private final DataSource dataSource;
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final String ADD_SQL = "INSERT INTO users (email, password, gender, firstName, lastName, about, age, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?::user_role);";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String UPDATE_BY_ID_PASS_SQL = "UPDATE users SET email = ?, password = ? WHERE id = ?;";
    private static final String UPDATE_BY_ID_DATA_SQL = "UPDATE users SET gender = ?, firstName = ?, lastName = ?, about = ?, age = ? WHERE id = ?;";
    private static final String FIND_BY_ID_SQL = "SELECT id, email, password, gender, firstName, lastName, about, age, role FROM users WHERE id = ?";
    private static final String FIND_BY_EMAIL_SQL = "SELECT id, email, password, gender, firstName, lastName, about, age, role FROM users WHERE email = ?";

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void remove(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void edit(User user) {
        editEmailAndPassword(user);
        editInfo(user);
    }

    public void editEmailAndPassword(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_ID_PASS_SQL)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editInfo(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_ID_DATA_SQL)) {
            preparedStatement.setString(1, user.getGender());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getAbout());
            preparedStatement.setInt(5, user.getAge());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getGender());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getAbout());
            preparedStatement.setInt(7, user.getAge());
            preparedStatement.setString(8, String.valueOf(user.getRole()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User findById(int usrId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
                preparedStatement.setInt(1, usrId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return USER_ROW_MAPPER.mapRow(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public User findByEmail(String usrEmail) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
                preparedStatement.setString(1, usrEmail);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return USER_ROW_MAPPER.mapRow(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean isUserExist(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                if(Objects.equals(email, resultSet.getString("email"))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


}
