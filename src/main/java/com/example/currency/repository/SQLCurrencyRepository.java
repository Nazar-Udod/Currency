package com.example.currency.repository;

import com.example.currency.model.Currency;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SQLCurrencyRepository implements CurrencyRepository {
    private static final String DB_URL = "jdbc:sqlite:D:\\SQLite\\currencyDB.db";

    @Override
    public int create(String name, String country) {
        String query = "INSERT INTO Currency (name, country) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, country);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new RuntimeException("Failed to retrieve generated ID");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add currency", e);
        }
    }

    @Override
    public Currency read(int id) {
        String query = "SELECT * FROM Currency WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Currency(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("country")
                    );
                } else {
                    throw new IllegalArgumentException("Currency doesn't exist");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch currency by ID", e);
        }
    }

    @Override
    public void update(int id, String name, String country) {
        String query = "UPDATE Currency SET name = ?, country = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, country);
            preparedStatement.setInt(3, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Currency ID does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update currency", e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Currency WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Currency ID does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete currency", e);
        }
    }

    @Override
    public List<Currency> getByCountry(String country) {
        List<Currency> currencies = new ArrayList<>();
        String query = "SELECT * FROM Currency WHERE country = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, country);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    currencies.add(new Currency(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("country")));
                }
                return currencies;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch currency by country", e);
        }
    }
}
