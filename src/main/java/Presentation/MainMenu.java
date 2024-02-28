package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fereastra principala atunci cand se ruleaza programul.
 * Butoanele sunt responsabile de introducerea utilizatorului
 * in ferestrele respective.
 */
public class MainMenu {
    JFrame frame = new JFrame("Shop");
    JPanel panel = new JPanel(new GridBagLayout());
    JLabel mobileShopText = new JLabel("MOBILE SHOP");
    JLabel welcomeText = new JLabel("Welcome!");
    JButton clients = new JButton("Clients");
    JButton orders = new JButton("Orders");
    JButton products = new JButton("Products");

    public MainMenu() {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mobileShopText.setFont(new Font("Arial", Font.BOLD, 100));
        welcomeText.setFont(new Font("Arial", Font.ITALIC, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(100,50,100,50);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(mobileShopText, gbc);

        gbc.gridy = 1;
        panel.add(welcomeText, gbc);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        clients.setFocusable(false);
        clients.setSize(new Dimension(200, 200));
        panel.add(clients, gbc);


        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        orders.setFocusable(false);
        orders.setMinimumSize(new Dimension(500, 500));
        panel.add(orders, gbc);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        products.setFocusable(false);
        products.setMinimumSize(new Dimension(500, 500));
        panel.add(products, gbc);

        clients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClientsMenu();
                frame.dispose();
            }
        });

        orders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrdersMenu();
                frame.dispose();
            }
        });

        products.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProductsMenu();
                frame.dispose();
            }
        });

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
