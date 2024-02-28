package DataAccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import BusinessLogic.Bill;
import Connection.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Since: Apr 03, 2017
 * @Source http://www.java-blog.com/mapping-javaobjects-database-reflection-generics
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }
    public List<T> findAll() {
        List<T> resultList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + type.getSimpleName();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            resultList = createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return resultList;
    }

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
    private List<T> createObjects(ResultSet resultSet) {
        List<T> resultList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();

                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);

                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }

                resultList.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SQLException |
                 IntrospectionException | NoSuchMethodException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:createObjects " + e.getMessage());
        }

        return resultList;
    }
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();

            String query = generateInsertQuery();

            statement = connection.prepareStatement(query);

            setStatementParameters(statement, t);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting object failed, no rows affected.");
            }


        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
    public T update(T t, String id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();

            String query = generateUpdateQuery(id);

            statement = connection.prepareStatement(query);

            setStatementParameters(statement, t);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating object failed, no rows affected.");
            }


        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }
    public void delete(int id, String idColumnName) {
        Connection connection = null;
        PreparedStatement statement = null;

        String tableName = type.getSimpleName();
        String query = "DELETE FROM " + tableName + " WHERE " + idColumnName + " = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error deleting record from " + tableName + " table: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
    private String generateInsertQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO ");
        queryBuilder.append(type.getSimpleName());
        queryBuilder.append(" (");

        Field[] fields = type.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            queryBuilder.append(fields[i].getName());
            if (i < fields.length - 1) {
                queryBuilder.append(", ");
            }
        }

        queryBuilder.append(") VALUES (");

        for (int i = 0; i < fields.length; i++) {
            queryBuilder.append("?");
            if (i < fields.length - 1) {
                queryBuilder.append(", ");
            }
        }

        queryBuilder.append(")");

        return queryBuilder.toString();
    }

    private String generateUpdateQuery(String idColumnName) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE ");
        queryBuilder.append(type.getSimpleName());
        queryBuilder.append(" SET ");

        Field[] fields = type.getDeclaredFields();
        for (int i = 0; i < fields.length - 1; i++) {
            queryBuilder.append(fields[i].getName());
            queryBuilder.append(" = ?");
            if (i < fields.length - 2) {
                queryBuilder.append(", ");
            }
        }

        queryBuilder.append(" WHERE ");
        queryBuilder.append(idColumnName);
        queryBuilder.append(" = ?");

        return queryBuilder.toString();
    }
    private void setStatementParameters(PreparedStatement statement, T t) throws SQLException {
        Field[] fields = type.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Object value = null;

            try {
                value = field.get(t);
            } catch (IllegalAccessException e) {
                LOGGER.log(Level.WARNING, "Error getting field value: " + e.getMessage());
            }

            statement.setObject(i + 1, value);
        }
    }
    public static DefaultTableModel populateTableWithObjects(JTable table, List<?> objects) {
        if (objects.isEmpty()) {
            return null;
        }
        Class<?> objectClass = objects.get(0).getClass();
        Field[] fields = objectClass.getDeclaredFields();
        Object[] headerData = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            headerData[i] = fields[i].getName();
        }
        DefaultTableModel tableModel = new DefaultTableModel(headerData, 0);
        table.setModel(tableModel);

        for (Object object : objects) {
            Object[] rowData = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                try {
                    rowData[i] = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            tableModel.addRow(rowData);
        }
        return tableModel;
    }
    public void insertBill(Bill bill) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "constangeles1");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Log (orderId, amount, timestamp) VALUES (?, ?, ?)")) {
            statement.setInt(1, bill.getOrderId());
            statement.setDouble(2, bill.getAmount());
            statement.setTimestamp(3, Timestamp.valueOf(bill.getTimestamp()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
