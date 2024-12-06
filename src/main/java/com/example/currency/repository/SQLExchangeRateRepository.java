package com.example.currency.repository;

import com.example.currency.model.Currency;
import com.example.currency.model.ExchangeRate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SQLExchangeRateRepository implements ExchangeRateRepository {
    private static final String DB_URL = "jdbc:sqlite:D:\\SQLite\\currencyDB.db";
    private final CurrencyRepository currencyRepository;

    public SQLExchangeRateRepository(CurrencyRepository SQLCurrencyRepository) {
        this.currencyRepository = SQLCurrencyRepository;
    }

    @Override
    public int create(double value, LocalDate date, int currencyId) {
        String query = "INSERT INTO Rate (rate_date, value, currency_id) VALUES (?, ?, ?)";
        Currency currency = currencyRepository.read(currencyId);

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, date.toString());
            preparedStatement.setDouble(2, value);
            preparedStatement.setInt(3, currency.getId());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new RuntimeException("Failed to retrieve generated ID");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add exchange rate", e);
        }
    }

    @Override
    public ExchangeRate read(int id) {
        String query = "SELECT * FROM Rate WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new ExchangeRate(
                        resultSet.getInt("id"),
                        currencyRepository.read(resultSet.getInt("currency_id")),
                        resultSet.getObject("rate_date", LocalDate.class),
                        resultSet.getDouble("value")
                );
            } else {
                throw new IllegalArgumentException("Exchange rate ID does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch exchange rate", e);
        }
    }

    @Override
    public void update(int id, double value, LocalDate date) {
        String query = "UPDATE Rate SET value = ?, rate_date = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, value);
            preparedStatement.setString(2, date.toString());
            preparedStatement.setInt(3, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Exchange rate ID does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to edit exchange rate", e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Rate WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Exchange rate ID does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete exchange rate", e);
        }
    }

    @Override
    public List<ExchangeRate> getByCurrencyId(int currencyId) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = "SELECT * FROM Rate WHERE currency_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, currencyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                exchangeRates.add(new ExchangeRate(resultSet.getInt("id"),
                        currencyRepository.read(resultSet.getInt("currency_id")),
                        resultSet.getObject("rate_date", LocalDate.class),
                        resultSet.getDouble("value")));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch exchange rates", e);
        }
    }

    @Override
    public List<ExchangeRate> getByDate(LocalDate date) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = "SELECT * FROM Rate WHERE rate_date = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, date.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                exchangeRates.add(new ExchangeRate(resultSet.getInt("id"),
                        currencyRepository.read(resultSet.getInt("currency_id")),
                        resultSet.getObject("rate_date", LocalDate.class),
                        resultSet.getDouble("value")));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch exchange rates", e);
        }
    }

    @Override
    public void deleteByCurrencyId(int currencyId) {
        String query = "DELETE FROM Rate WHERE currency_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, currencyId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete exchange rates by currency ID", e);
        }
    }
}
