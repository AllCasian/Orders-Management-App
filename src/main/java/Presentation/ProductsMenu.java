package Presentation;

import DataAccess.ProductsDAO;
import BusinessLogic.Model.Products;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Aceasta clasa face posibila comunicarea cu tabela
 * "products" din baza de date. Aici se pot realiza operatii
 * de tip CRUD pe tabela respectiva, folosind butoanele:
 * create(crearea unui nou produs), modify(modificarea
 * produsului cu id-ul introdus), delete(stergerea
 * produsului cu id-ul introdus). Butonul back aduce
 * utilizatorul la fereastra principala.
 */
public class ProductsMenu extends JFrame {
    JLabel productsText = new JLabel("Products");
    JButton create = new JButton("Create");
    JButton modify = new JButton("Modify");
    JButton delete = new JButton("Delete");
    JButton back = new JButton("Back");
    JTable table;
    DefaultTableModel tableModel;
    public ProductsMenu(){
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        productsText.setFont(new Font("Arial", Font.BOLD, 30));
        productsText.setHorizontalAlignment(SwingConstants.LEFT);
        productsText.setVerticalAlignment(SwingConstants.TOP);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("name");
        tableModel.addColumn("price");
        tableModel.addColumn("quantity");
        tableModel.addColumn("idProduct");
        table = new JTable(tableModel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(productsText, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(create);
        create.setFocusable(false);
        buttonsPanel.add(modify);
        modify.setFocusable(false);
        buttonsPanel.add(delete);
        delete.setFocusable(false);
        buttonsPanel.add(back);
        back.setFocusable(false);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.setVisible(true);

        populateTableWithProducts();

        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createButtonClicked();
                populateTableWithProducts();
            }
        });

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteButtonClicked();
                populateTableWithProducts();
            }
        });

        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyButtonClicked();
                populateTableWithProducts();
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
     * Returneaza indexul randului corespunzator ID-ului specificat.
     *
     * @param idProduct ID-ul produsului
     * @return Indexul randului
     */
    private int getRowIndexById(int idProduct) {
        int rowCount = tableModel.getRowCount();
        System.out.println(tableModel.getColumnCount());
        for (int i = 0; i < rowCount; i++) {
            Object value = tableModel.getValueAt(i, 3);
            if (value instanceof Integer && (Integer) value == idProduct) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Populeaza tabela cu datele clientilor.
     */
    public void populateTableWithProducts() {
        ProductsDAO productsDAO = new ProductsDAO();
        List<Products> products = productsDAO.findAll();

        tableModel = ProductsDAO.populateTableWithObjects(table, products);

        int rowCount = table.getRowCount();
        tableModel.setRowCount(rowCount);
    }

    /**
     * Actiunea executata la apasarea butonului "Create".
     */
    private void createButtonClicked() {
        JDialog createDialog = new JDialog(this, "Create Product", true);
        createDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createDialog.setSize(400, 130);
        createDialog.setLocationRelativeTo(this);

        JPanel createPanel = new JPanel();
        createPanel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JButton saveButton = new JButton("Save");

        createPanel.add(nameLabel);
        createPanel.add(nameField);
        createPanel.add(priceLabel);
        createPanel.add(priceField);
        createPanel.add(quantityLabel);
        createPanel.add(quantityField);
        createPanel.add(saveButton);

        createDialog.add(createPanel);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String priceText = priceField.getText();
                String quantityText = quantityField.getText();

                ProductsDAO productsDAO = new ProductsDAO();
                Products products = new Products();

                products.setPrice(Integer.parseInt(priceText));
                products.setName(name);
                products.setQuantity(Integer.parseInt(quantityText));

                Products createdProduct = productsDAO.insert(products);

                createDialog.dispose();
            }
        });

        createDialog.setVisible(true);
    }

    /**
     * Actiunea executata la apasarea butonului "Delete".
     */
    private void deleteButtonClicked() {
        String idString = JOptionPane.showInputDialog(this, "Enter ID to delete:");
        if (idString == null || idString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idProduct = Integer.parseInt(idString);
            ProductsDAO productsDAO = new ProductsDAO();
            productsDAO.delete(idProduct, "idProduct");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actiunea executata la apasarea butonului "Modify".
     */
    private void modifyButtonClicked() {
        String idString = JOptionPane.showInputDialog(this, "Enter ID to modify:");
        if (idString == null || idString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idProduct = Integer.parseInt(idString);
            int rowIndex = getRowIndexById(idProduct);
            if (rowIndex != -1) {
                String currentName = tableModel.getValueAt(rowIndex, 0).toString();
                String currentPrice = tableModel.getValueAt(rowIndex, 1).toString();
                String currentQuantity = tableModel.getValueAt(rowIndex, 2).toString();

                JTextField nameField = new JTextField(currentName);
                JTextField priceField = new JTextField(currentPrice);
                JTextField quantityField = new JTextField(currentQuantity);
                Object[] message = {
                        "Name:", nameField,
                        "Price:", priceField,
                        "Quantity:", quantityField
                };
                int option = JOptionPane.showConfirmDialog(this, message, "Modify Entry", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String newName = nameField.getText();
                    String newPriceText = priceField.getText();
                    String newQuantityText = quantityField.getText();

                    ProductsDAO productsDAO = new ProductsDAO();
                    Products products = new Products();
                    products.setIdProduct(idProduct);
                    products.setPrice(Integer.parseInt(newPriceText));
                    products.setName(newName);
                    products.setQuantity(Integer.parseInt(newQuantityText));
                    Products createdProduct = productsDAO.update(products, "idProduct");

                    JOptionPane.showMessageDialog(this, "Row modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No matching row found with the specified ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
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
