package com.example.currency.repository;

import com.example.currency.model.Currency;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FakeCurrencyRepository implements CurrencyRepository {

    private static final String DB_URL = "jdbc:sqlite:D:\\myLabs\\III course\\SQLite\\SpringLab.db";

    // Get all currencies
    @Override
    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        String query = "SELECT * FROM Currency";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                currencies.add(new Currency(
                        resultSet.getInt("Id"),
                        resultSet.getString("Name"),
                        resultSet.getString("Country")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch currencies", e);
        }

        return currencies;
    }


    @Override
    public Currency getByCountry(String country) {
        String query = "SELECT * FROM Currency WHERE Country = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, country);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Currency(
                            resultSet.getInt("Id"),
                            resultSet.getString("Name"),
                            resultSet.getString("Country")
                    );
                } else {
                    throw new IllegalArgumentException("Currency doesn't exist");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch currency by ID", e);
        }
    }

    // Get currency by ID
    @Override
    public Currency getById(Integer id) {
        String query = "SELECT * FROM Currency WHERE Id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Currency(
                            resultSet.getInt("Id"),
                            resultSet.getString("Name"),
                            resultSet.getString("Country")
                    );
                } else {
                    throw new IllegalArgumentException("Currency doesn't exist");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch currency by ID", e);
        }
    }

    // Get currency by name
    @Override
    public Currency getByName(String name) {
        String query = "SELECT * FROM Currency WHERE Name = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Currency(
                            resultSet.getInt("Id"),
                            resultSet.getString("Name"),
                            resultSet.getString("Country")
                    );
                } else {
                    throw new IllegalArgumentException("Currency doesn't exist");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch currency by name", e);
        }
    }

    // Add a new currency
    @Override
    public int add(String name, String country) {
        String query = "INSERT INTO Currency (Name, Country) VALUES (?, ?)";
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

    // Update currency by ID
    @Override
    public void updateById(int id, String name, String country) {
        String query = "UPDATE Currency SET Name = ?, Country = ? WHERE Id = ?";
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
    public void deleteById(int id) {
        String query = "DELETE FROM Currency WHERE Id = ?";
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

}
