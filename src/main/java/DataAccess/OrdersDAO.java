package DataAccess;

import BusinessLogic.Model.Orders;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Connection.ConnectionFactory.getConnection;

/**
 * Extinde clasa abstracta AbstractDAO folosindu-se
 * de parametrul generic Orders. Responsabila de
 * reflection in cazul unei comenzi. Totodata, aici
 * avem metoda getCurrentIdOrder care returneaza
 * id-ul comenzii curente si folosit apoi la
 * crearea unei facturi.
 */
public class OrdersDAO extends AbstractDAO<Orders> {
    /**
     * Returneaza id-ul comenzii curente, adica
     * id-ul maxim din tabela Orders.
     *
     * @return Id-ul comenzii curente
     */
    public int getCurrentIdOrder() {
        String query = "SELECT MAX(idOrder) AS current_idOrder FROM Orders";
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    if (resultSet.next()) {
                        return resultSet.getInt("current_idOrder");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
