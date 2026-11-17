import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ATMGUI extends JFrame {

    private double balance = 5000.00;
    private final int PIN = 1234;
    private int attempts = 0;

    private final ArrayList<String> transactions = new ArrayList<>();

    private final Color navy = new Color(17, 29, 53);
    private final Color blue = new Color(57, 120, 237);
    private final Color green = new Color(33, 124, 103);
    private final Color background = new Color(243, 246, 252);
    private final Color textColor = new Color(35, 48, 70);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    private JLabel balanceLabel;
    private JTextArea historyArea;

    public ATMGUI() {
        setTitle("SRM BANK | ATM Simulation");
        setSize(850, 650);
        setMinimumSize(new Dimension(750, 580));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel.add(createLoginScreen(), "LOGIN");
        mainPanel.add(createDashboard(), "DASHBOARD");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    // ---------- LOGIN SCREEN ----------

    private JPanel createLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(navy);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(35, 40, 35, 40));

        JLabel logo = new JLabel("SRM BANK");
        logo.setFont(new Font("SansSerif", Font.BOLD, 30));
        logo.setForeground(blue);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Welcome Back!");
        title.setFont(new Font("SansSerif", Font.BOLD, 23));
        title.setForeground(navy);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("ATM Simulation System");
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField pinField = new JPasswordField();
        pinField.setFont(new Font("SansSerif", Font.PLAIN, 22));
        pinField.setHorizontalAlignment(JTextField.CENTER);
        pinField.setMaximumSize(new Dimension(290, 45));
        pinField.setBorder(BorderFactory.createTitledBorder("Enter 4-digit PIN"));

        JButton loginButton = button("LOGIN SECURELY", blue, Color.WHITE);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Demo PIN: 1234");
        hint.setForeground(Color.GRAY);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        Runnable login = () -> {
            String entered = new String(pinField.getPassword());

            if (entered.equals(String.valueOf(PIN))) {
                attempts = 0;
                pinField.setText("");
                updateDashboard();
                cardLayout.show(mainPanel, "DASHBOARD");
            } else {
                attempts++;
                pinField.setText("");

                if (attempts >= 3) {
                    JOptionPane.showMessageDialog(this,
                            "Too many incorrect attempts.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Incorrect PIN. Attempts remaining: "
                                    + (3 - attempts));
                }
            }
        };

        loginButton.addActionListener(e -> login.run());
        pinField.addActionListener(e -> login.run());

        card.add(logo);
        card.add(Box.createVerticalStrut(22));
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));
        card.add(pinField);
        card.add(Box.createVerticalStrut(18));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(20));
        card.add(hint);

        panel.add(card);
        return panel;
    }

    // ---------- MAIN DASHBOARD ----------

    private JPanel createDashboard() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(25, 28, 25, 28));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("SRM BANK");
        brand.setFont(new Font("SansSerif", Font.BOLD, 26));
        brand.setForeground(navy);

        JLabel subtitle = new JLabel("Your banking dashboard");
        subtitle.setForeground(Color.GRAY);

        titles.add(brand);
        titles.add(Box.createVerticalStrut(4));
        titles.add(subtitle);

        JButton logout = button("LOG OUT", navy, Color.WHITE);
        logout.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        header.add(titles, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        // Balance card
        JPanel balanceCard = new JPanel();
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBackground(navy);
        balanceCard.setBorder(new EmptyBorder(22, 25, 22, 25));

        JLabel balanceTitle = new JLabel("TOTAL AVAILABLE BALANCE");
        balanceTitle.setForeground(new Color(190, 205, 229));
        balanceTitle.setFont(new Font("SansSerif", Font.BOLD, 12));

        balanceLabel = new JLabel("₹ 5,000.00");
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 34));

        JLabel account = new JLabel("DEMO ACCOUNT  •••• 1234");
        account.setForeground(new Color(190, 205, 229));

        balanceCard.add(balanceTitle);
        balanceCard.add(Box.createVerticalStrut(10));
        balanceCard.add(balanceLabel);
        balanceCard.add(Box.createVerticalStrut(8));
        balanceCard.add(account);

        // Action buttons
        JPanel actions = new JPanel(new GridLayout(2, 2, 14, 14));
        actions.setOpaque(false);

        JButton deposit = button(
                "+  DEPOSIT MONEY", green, Color.WHITE);

        JButton withdraw = button(
                "-  WITHDRAW MONEY", blue, Color.WHITE);

        JButton enquiry = button(
                "VIEW BALANCE", Color.WHITE, navy);

        JButton history = button(
                "TRANSACTION HISTORY", Color.WHITE, navy);

        deposit.addActionListener(e -> depositMoney());
        withdraw.addActionListener(e -> withdrawMoney());

        enquiry.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        String.format("Available balance: ₹ %.2f", balance),
                        "Balance Enquiry",
                        JOptionPane.INFORMATION_MESSAGE));

        history.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        new JScrollPane(createHistoryView()),
                        "Transaction History",
                        JOptionPane.INFORMATION_MESSAGE));

        actions.add(deposit);
        actions.add(withdraw);
        actions.add(enquiry);
        actions.add(history);

        // Recent transactions section
        JPanel recent = new JPanel(new BorderLayout(0, 8));
        recent.setBackground(Color.WHITE);
        recent.setBorder(new EmptyBorder(15, 16, 15, 16));

        JLabel recentTitle = new JLabel("Recent Transactions");
        recentTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        recentTitle.setForeground(textColor);

        historyArea = new JTextArea(4, 30);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        recent.add(recentTitle, BorderLayout.NORTH);
        recent.add(new JScrollPane(historyArea), BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setOpaque(false);
        center.add(balanceCard, BorderLayout.NORTH);
        center.add(actions, BorderLayout.CENTER);

        panel.add(header, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(recent, BorderLayout.SOUTH);

        recent.setPreferredSize(new Dimension(500, 160));

        return panel;
    }

    // ---------- BUTTON DESIGN ----------

    private JButton button(String name, Color bg, Color fg) {
        JButton b = new JButton(name);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(14, 12, 14, 12));
        return b;
    }

    // ---------- ATM OPERATIONS ----------

    private void depositMoney() {
        String input = JOptionPane.showInputDialog(
                this, "Enter deposit amount (₹):");

        if (input == null) return;

        try {
            double amount = Double.parseDouble(input.trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                showError("Enter a valid positive amount.");
                return;
            }

            balance += amount;
            addTransaction("DEPOSIT", amount);
            updateDashboard();

            JOptionPane.showMessageDialog(this,
                    String.format("Deposit successful!%nBalance: ₹ %.2f",
                            balance));

        } catch (NumberFormatException e) {
            showError("Please enter a numeric amount.");
        }
    }

    private void withdrawMoney() {
        String input = JOptionPane.showInputDialog(
                this, "Enter withdrawal amount (₹):");

        if (input == null) return;

        try {
            double amount = Double.parseDouble(input.trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                showError("Enter a valid positive amount.");
                return;
            }

            if (amount > balance) {
                showError("Insufficient balance!");
                return;
            }

            balance -= amount;
            addTransaction("WITHDRAWAL", amount);
            updateDashboard();

            JOptionPane.showMessageDialog(this,
                    String.format("Withdrawal successful!%nBalance: ₹ %.2f",
                            balance));

        } catch (NumberFormatException e) {
            showError("Please enter a numeric amount.");
        }
    }

    private void addTransaction(String type, double amount) {
        String time = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        transactions.add(0, time + " | " + type
                + " | ₹ " + String.format("%.2f", amount));
    }

    private JTextArea createHistoryView() {
        JTextArea area = new JTextArea(12, 38);
        area.setEditable(false);
        area.setText(transactions.isEmpty()
                ? "No transactions available."
                : String.join("\n", transactions));
        return area;
    }

    private void updateDashboard() {
        if (balanceLabel != null) {
            balanceLabel.setText(
                    String.format("₹ %.2f", balance));
        }

        if (historyArea != null) {
            historyArea.setText(transactions.isEmpty()
                    ? "No transactions yet."
                    : String.join("\n", transactions));
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "ATM Message", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new ATMGUI().setVisible(true));
    }
}
