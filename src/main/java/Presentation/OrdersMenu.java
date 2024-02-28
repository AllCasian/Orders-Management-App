package Presentation;

import BusinessLogic.Bill;
import DataAccess.ClientDAO;
import DataAccess.OrdersDAO;
import DataAccess.ProductsDAO;
import BusinessLogic.Model.Client;
import BusinessLogic.Model.Orders;
import BusinessLogic.Model.Products;

import java.time.LocalDateTime;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Aceasta clasa se ocupa cu crearea de comenzi
 * in tabela "orders" in baza de date. In aceasta
 * clasa se defineste doar o metoda, aceea de a
 * crea o comanda. In interfata grafica corespunzatoare
 * putem vedea tabela respectiva din baza de date
 * si prin butonul "create" crearea unei noi comenzi.
 * Butonul "back" aduce utilizatorul la fereastra
 * principala
 */
public class OrdersMenu extends JFrame {
    JLabel ordersText = new JLabel("Orders");
    JButton create = new JButton("Create");
    JButton modify = new JButton("Modify");
    JButton delete = new JButton("Delete");
    JButton back = new JButton("Back");
    JTable table;
    DefaultTableModel tableModel;
    public OrdersMenu(){
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ordersText.setFont(new Font("Arial", Font.BOLD, 30));
        ordersText.setHorizontalAlignment(SwingConstants.LEFT);
        ordersText.setVerticalAlignment(SwingConstants.TOP);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("idOrder");
        tableModel.addColumn("IdClient");
        tableModel.addColumn("IdProduct");
        tableModel.addColumn("totalAmount");
        tableModel.addColumn("price");
        table = new JTable(tableModel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(ordersText, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(create);
        create.setFocusable(false);
        buttonsPanel.add(back);
        back.setFocusable(false);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.setVisible(true);

        populateTableWithOrders();

        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createButtonClicked();
                populateTableWithOrders();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonClicked();
            }
        });
    }

    /**
     * Populeaza tabela cu datele comenzilor.
     */
    public void populateTableWithOrders() {
        OrdersDAO ordersDAO = new OrdersDAO();
        List<Orders> orders = ordersDAO.findAll();

        tableModel = OrdersDAO.populateTableWithObjects(table, orders);

        int rowCount = table.getRowCount();
        tableModel.setRowCount(rowCount);
    }

    /**
     * Actiunea executata la apasarea butonului "Create".
     */
    private void createButtonClicked() {
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = clientDAO.findAll();

        ProductsDAO productDAO = new ProductsDAO();
        List<Products> products = productDAO.findAll();

        JComboBox<Client> clientComboBox = new JComboBox<>(clients.toArray(new Client[0]));
        JComboBox<Products> productComboBox = new JComboBox<>(products.toArray(new Products[0]));

        JTextField quantityField = new JTextField();

        Object[] message = {
                "Client:", clientComboBox,
                "Product:", productComboBox,
                "Quantity:", quantityField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create Order", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Client selectedClient = (Client) clientComboBox.getSelectedItem();
            Products selectedProduct = (Products) productComboBox.getSelectedItem();
            String quantityString = quantityField.getText();

            int quantity;
            try {
                quantity = Integer.parseInt(quantityString);

                if (selectedProduct.getQuantity() >= quantity) {
                    selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity);
                    productDAO.update(selectedProduct, "idProduct");

                    OrdersDAO orderDAO = new OrdersDAO();
                    Orders order = new Orders(selectedClient.getIdClient(), selectedProduct.getIdProduct(), quantity, selectedProduct.getPrice() * quantity);
                    orderDAO.insert(order);

                    int idOrder = orderDAO.getCurrentIdOrder();

                    LocalDateTime now = LocalDateTime.now();
                    Bill bill = new Bill(idOrder, order.getPrice(), now);
                    orderDAO.insertBill(bill);

                    JOptionPane.showMessageDialog(this, "Order created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Actiunea executata la apasarea butonului "Back".
     */
    private void backButtonClicked(){
        new MainMenu();
        this.dispose();
    }

}
