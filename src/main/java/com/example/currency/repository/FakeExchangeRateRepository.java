package com.example.currency.repository;

import com.example.currency.model.Currency;
import com.example.currency.model.ExchangeRate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FakeExchangeRateRepository implements ExchangeRateRepository {

    private static final String DB_URL = "jdbc:sqlite:D:\\myLabs\\III course\\SQLite\\SpringLab.db";
    private final CurrencyRepository currencyRepository;

    public FakeExchangeRateRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    // Get all exchange rates
    @Override
    public List<ExchangeRate> getAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = "SELECT * FROM Rate_new";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                exchangeRates.add(mapToExchangeRate(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch exchange rates", e);
        }

        return exchangeRates;
    }

    // Get all exchange rates for a specific date
    @Override
    public List<ExchangeRate> getAllByDate(LocalDate date) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = "SELECT * FROM Rate_new WHERE Date = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, date.toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    exchangeRates.add(mapToExchangeRate(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch exchange rates by date", e);
        }

        return exchangeRates;
    }

    // Get exchange rate for a specific currency and date
    @Override
    public ExchangeRate getByCurrencyAndDate(Integer currencyId, LocalDate date) {
        String query = "SELECT * FROM Rate_new WHERE CurrencyId = ? AND Date = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, currencyId);
            preparedStatement.setString(2, date.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToExchangeRate(resultSet);
                } else {
                    throw new IllegalArgumentException("Exchange rate doesn't exist");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch exchange rate", e);
        }
    }

    // Get exchange rates for a specific currency in a date range
    @Override
    public List<ExchangeRate> getByCurrencyAndDateRange(Integer currencyId, LocalDate startDate, LocalDate endDate) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = "SELECT * FROM Rate_new WHERE CurrencyId = ? AND Date BETWEEN ? AND ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, currencyId);
            preparedStatement.setString(2, startDate.toString());
            preparedStatement.setString(3, endDate.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    exchangeRates.add(mapToExchangeRate(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch exchange rates by date range", e);
        }

        return exchangeRates;
    }

    // Add a new exchange rate
    @Override
    public int add(String currencyName, LocalDate date, double rate) {
        String query = "INSERT INTO Rate_new (CurrencyId, Date, RateValue) VALUES (?, ?, ?)";
        Currency currency = currencyRepository.getByName(currencyName);

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, currency.getId());
            preparedStatement.setString(2, date.toString());
            preparedStatement.setDouble(3, rate);

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

    // Edit an exchange rate by ID
    @Override
    public void editRateById(int id, LocalDate date, double rate) {
        String query = "UPDATE Rate_new SET Date = ?, RateValue = ? WHERE Id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, date.toString());
            preparedStatement.setDouble(2, rate);
            preparedStatement.setInt(3, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Exchange rate ID does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to edit exchange rate", e);
        }
    }

    // Delete exchange rate by ID
    @Override
    public void deleteById(int rateId) {
        String query = "DELETE FROM Rate_new WHERE Id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, rateId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Exchange rate ID does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete exchange rate", e);
        }
    }

    @Override
    public ExchangeRate getById(int rateId) {
        String query = "SELECT * FROM Rate_new WHERE Id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, rateId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new ExchangeRate(
                        resultSet.getInt("Id"),
                        currencyRepository.getById(resultSet.getInt("CurrencyId")),
                        resultSet.getDate("Date").toLocalDate(),
                        resultSet.getDouble("Rate")
                );
            } else {
                throw new IllegalArgumentException("Exchange rate ID does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch exchange rate", e);
        }
    }



    @Override
    public void deleteByCurrencyId(int currencyId) {
        String query = "DELETE FROM Rate_new WHERE CurrencyId = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, currencyId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete exchange rates by currency ID", e);
        }
    }


    // Helper method to map ResultSet to ExchangeRate
    private ExchangeRate mapToExchangeRate(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("Id");
        int currencyId = resultSet.getInt("CurrencyId");
        double rateValue = resultSet.getDouble("RateValue");
        LocalDate date = LocalDate.parse(resultSet.getString("Date"));
        Currency currency = currencyRepository.getById(currencyId);

        return new ExchangeRate(id, currency, date, rateValue);
    }
}
