package Presentation;

import DataAccess.ClientDAO;
import BusinessLogic.Model.Client;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Aceasta clasa faciliteaza interactiunea cu tabela "client" din baza de date.
 * Sunt disponibile operatii de tip CRUD asupra tabelului, utilizand urmatoarele butoane:
 * create (pentru crearea unui nou client),
 * modify (pentru modificarea clientului cu ID-ul introdus),
 * delete (pentru stergerea clientului cu ID-ul introdus).
 * Butonul "back" permite revenirea la fereastra principala.
 */
public class ClientsMenu extends JFrame {

    private JLabel clientsText = new JLabel("Clients");
    private JButton create = new JButton("Create");
    private JButton modify = new JButton("Modify");
    private JButton delete = new JButton("Delete");
    private JButton back = new JButton("Back");
    private JTable table;

    private DefaultTableModel tableModel;

    public ClientsMenu() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        clientsText.setFont(new Font("Arial", Font.BOLD, 30));
        clientsText.setHorizontalAlignment(SwingConstants.LEFT);
        clientsText.setVerticalAlignment(SwingConstants.TOP);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("name");
        tableModel.addColumn("email");
        tableModel.addColumn("idClient");
        table = new JTable(tableModel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(clientsText, BorderLayout.NORTH);
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

        populateTableWithClients();

        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createButtonClicked();
                populateTableWithClients();
            }
        });

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteButtonClicked();
                populateTableWithClients();
            }
        });

        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyButtonClicked();
                populateTableWithClients();
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
     * @param idClient ID-ul clientului
     * @return Indexul randului
     */
    private int getRowIndexById(int idClient) {
        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object value = tableModel.getValueAt(i, 2);
            if (value instanceof Integer && (Integer) value == idClient) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Populeaza tabela cu datele clientilor.
     */
    public void populateTableWithClients() {
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = clientDAO.findAll();

        tableModel = ClientDAO.populateTableWithObjects(table, clients);

        int rowCount = table.getRowCount();
        tableModel.setRowCount(rowCount);
    }

    /**
     * Actiunea executata la apasarea butonului "Create".
     */
    private void createButtonClicked() {
        JDialog createDialog = new JDialog(this, "Create Client", true);
        createDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createDialog.setSize(400, 100);
        createDialog.setLocationRelativeTo(this);

        JPanel createPanel = new JPanel();
        createPanel.setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JButton saveButton = new JButton("Save");

        createPanel.add(nameLabel);
        createPanel.add(nameField);
        createPanel.add(emailLabel);
        createPanel.add(emailField);
        createPanel.add(saveButton);

        createDialog.add(createPanel);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();

                ClientDAO clientDAO = new ClientDAO();
                Client client = new Client();

                client.setName(name);
                client.setEmail(email);

                Client createdClient = clientDAO.insert(client);

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
            int idClient = Integer.parseInt(idString);
            ClientDAO clientDAO = new ClientDAO();
            clientDAO.delete(idClient, "idClient");
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
            int idClient = Integer.parseInt(idString);
            int rowIndex = getRowIndexById(idClient);
            if (rowIndex != -1) {
                String currentName = tableModel.getValueAt(rowIndex, 0).toString();
                String currentEmail = tableModel.getValueAt(rowIndex, 1).toString();

                JTextField nameField = new JTextField(currentName);
                JTextField emailField = new JTextField(currentEmail);
                Object[] message = {
                        "Name:", nameField,
                        "Email:", emailField
                };
                int option = JOptionPane.showConfirmDialog(this, message, "Modify Entry", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String newName = nameField.getText();
                    String newEmail = emailField.getText();

                    ClientDAO clientDAO = new ClientDAO();
                    Client client = new Client();
                    client.setName(newName);
                    client.setEmail(newEmail);
                    client.setIdClient(idClient);
                    Client createdClient = clientDAO.update(client, "idClient");

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
    private void backButtonClicked() {
        new MainMenu();
        this.dispose();
    }
}
