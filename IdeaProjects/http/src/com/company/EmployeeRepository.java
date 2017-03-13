package com.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EmployeeRepository {
    private static final String ID_ = "id";
    private static final String FIRST_NAME_ = "first_name";
    private static final String LAST_NAME_ = "last_name";
    private static final String EMAIL_ = "email";
    Connection connection;

    public EmployeeRepository(Connection connection) {
        this.connection = connection;
    }

    public String addEmployee(Employee employee) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("INSERT INTO Employee (id, first_name, last_name, email) VALUES (?, ?, ?, ?); ");
        String uuid = UUID.randomUUID().toString();
        preparedStatement.setString(1, uuid);
        preparedStatement.setString(2, employee.getFirstName());
        preparedStatement.setString(3, employee.getLastName());
        preparedStatement.setString(4, employee.getEmail());
        preparedStatement.execute();
        return uuid;
    }

    public Employee getEmployee(String email) throws Exception {
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("SELECT * FROM Employee WHERE email = (?); ");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Employee employee = new Employee();
            employee.setId(UUID.fromString(resultSet.getString(ID_)));
            employee.setFirstName(resultSet.getString(FIRST_NAME_));
            employee.setLastName(resultSet.getString(LAST_NAME_));
            employee.setEmail(resultSet.getString(EMAIL_));
            return employee;
        } else {
            return null;
        }
    }

    public void putEmployee (Employee employee) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("UPDATE Employee SET first_name = ?, last_name = ?, email = ? WHERE id = ?");
        preparedStatement.setString(4, employee.getId().toString());
        preparedStatement.setString(1, employee.getFirstName());
        preparedStatement.setString(2, employee.getLastName());
        preparedStatement.setString(3, employee.getEmail());
        preparedStatement.execute();
    }

    public void deleteEmployee (String email) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("DELETE FROM Employee WHERE email = ?");
        preparedStatement.setString(1, email);
        preparedStatement.execute();
    }

}
