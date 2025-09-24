import javax.swing.*;
import java.awt.*;

public class FinanceCalculator extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;

    public FinanceCalculator() {
        setTitle("Finance Calculator");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Home Page
        JPanel homePanel = new JPanel(new GridLayout(5, 1, 15, 15));
        homePanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

        JLabel title = new JLabel("Finance Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        homePanel.add(title);

        JButton ciButton = new JButton("Compound Interest Calculator");
        JButton ppfButton = new JButton("PPF Calculator");
        JButton fdButton = new JButton("FD Calculator");
        JButton sipButton = new JButton("SIP Calculator");

        ciButton.setFont(new Font("Arial", Font.BOLD, 18));
        ppfButton.setFont(new Font("Arial", Font.BOLD, 18));
        fdButton.setFont(new Font("Arial", Font.BOLD, 18));
        sipButton.setFont(new Font("Arial", Font.BOLD, 18));

        homePanel.add(ciButton);
        homePanel.add(ppfButton);
        homePanel.add(fdButton);
        homePanel.add(sipButton);

        // Add all calculator panels
        mainPanel.add(homePanel, "Home");
        mainPanel.add(new CalculatorPanel("Compound Interest", this), "CI");
        mainPanel.add(new CalculatorPanel("PPF", this), "PPF");
        mainPanel.add(new CalculatorPanel("FD", this), "FD");
        mainPanel.add(new CalculatorPanel("SIP", this), "SIP");

        // Button Actions
        ciButton.addActionListener(e -> cardLayout.show(mainPanel, "CI"));
        ppfButton.addActionListener(e -> cardLayout.show(mainPanel, "PPF"));
        fdButton.addActionListener(e -> cardLayout.show(mainPanel, "FD"));
        sipButton.addActionListener(e -> cardLayout.show(mainPanel, "SIP"));

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinanceCalculator::new);
    }
}

class CalculatorPanel extends JPanel {
    private JTextField investField, rateField, timeField;
    private JLabel investedLabel, returnsLabel, totalLabel;
    private PieChartPanel pieChart;
    private FinanceCalculator parent;

    public CalculatorPanel(String type, FinanceCalculator parent) {
        this.parent = parent;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel(type + " Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Input Panel (left side)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Details"));

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Dimension fieldSize = new Dimension(150, 30);

        // Row 1: Investment
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel investLabel = new JLabel("Investment Amount:");
        investLabel.setFont(labelFont);
        investField = new JTextField();
        investField.setPreferredSize(fieldSize);
        row1.add(investLabel);
        row1.add(investField);

        // Row 2: Rate
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel rateLabel = new JLabel("Rate (%):");
        rateLabel.setFont(labelFont);
        rateField = new JTextField();
        rateField.setPreferredSize(fieldSize);
        row2.add(rateLabel);
        row2.add(rateField);

        // Row 3: Time
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel timeLabel = new JLabel("Time (Years):");
        timeLabel.setFont(labelFont);
        timeField = new JTextField();
        timeField.setPreferredSize(fieldSize);
        row3.add(timeLabel);
        row3.add(timeField);

        // Calculate Button
        JButton calcButton = new JButton("Calculate");
        calcButton.setFont(new Font("Arial", Font.BOLD, 18));

        calcButton.addActionListener(e -> calculate(type));

        inputPanel.add(row1);
        inputPanel.add(row2);
        inputPanel.add(row3);
        inputPanel.add(calcButton);

        // Right side (Pie chart + Summary Box)
        JPanel rightPanel = new JPanel(new BorderLayout());

        pieChart = new PieChartPanel(0, 0);
        pieChart.setPreferredSize(new Dimension(350, 250));

        // Summary Box
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));

        Font sumFont = new Font("Arial", Font.BOLD, 18);
        investedLabel = new JLabel("Invested Amount: 0");
        investedLabel.setFont(sumFont);
        returnsLabel = new JLabel("Estimated Returns: 0");
        returnsLabel.setFont(sumFont);
        totalLabel = new JLabel("Total Value: 0");
        totalLabel.setFont(sumFont);

        summaryPanel.add(investedLabel);
        summaryPanel.add(returnsLabel);
        summaryPanel.add(totalLabel);

        rightPanel.add(pieChart, BorderLayout.CENTER);
        rightPanel.add(summaryPanel, BorderLayout.SOUTH);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.addActionListener(e -> parent.cardLayout.show(parent.mainPanel, "Home"));

        add(inputPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    private void calculate(String type) {
        try {
            double principal = Double.parseDouble(investField.getText());
            double rate = Double.parseDouble(rateField.getText()) / 100;
            int time = Integer.parseInt(timeField.getText());
            double total = 0;
            double invested = 0;

            switch (type) {
                case "Compound Interest":
                case "FD":
                    // Both are lump-sum investments with compounding.
                    invested = principal;
                    total = principal * Math.pow((1 + rate), time);
                    break;
                case "PPF":
                    // PPF is a recurring annual investment with annual compounding.
                    // Assuming 'principal' is the annual deposit and compounding is yearly.
                    invested = principal * time;
                    double futureValuePPF = 0;
                    for (int i = 0; i < time; i++) {
                        futureValuePPF = (futureValuePPF + principal) * (1 + rate);
                    }
                    total = futureValuePPF;
                    break;
                case "SIP":
                    // SIP is a recurring monthly investment with monthly compounding.
                    // Assuming 'principal' is the monthly deposit.
                    double monthlyRate = rate / 12;
                    int totalMonths = time * 12;

                    invested = principal * totalMonths;
                    
                    // Future Value of an Annuity (SIP) Formula
                    total = principal * ((Math.pow(1 + monthlyRate, totalMonths) - 1) / monthlyRate) * (1 + monthlyRate);
                    break;
            }

            double returns = total - invested;

            investedLabel.setText("Invested Amount: " + String.format("%.2f", invested));
            returnsLabel.setText("Estimated Returns: " + String.format("%.2f", returns));
            totalLabel.setText("Total Value: " + String.format("%.2f", total));

            pieChart.setValues(invested, returns);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage());
        }
    }
} // This is the missing brace for the CalculatorPanel class

// Simple Pie Chart without JFreeChart
class PieChartPanel extends JPanel {
    private double invested, returns;

    public PieChartPanel(double invested, double returns) {
        this.invested = invested;
        this.returns = returns;
    }

    public void setValues(double invested, double returns) {
        this.invested = invested;
        this.returns = returns;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (invested + returns == 0) return;

        Graphics2D g2 = (Graphics2D) g;
        int diameter = Math.min(getWidth(), getHeight()) - 20;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        double total = invested + returns;
        int angleInvested = (int) Math.round(360 * invested / total);
        int angleReturns = 360 - angleInvested;

        g2.setColor(Color.BLUE);
        g2.fillArc(x, y, diameter, diameter, 0, angleInvested);

        g2.setColor(Color.GREEN);
        g2.fillArc(x, y, diameter, diameter, angleInvested, angleReturns);

        g2.setColor(Color.BLACK);
        g2.drawString("Invested", x + 10, y + 20);
        g2.drawString("Returns", x + 10, y + 40);
    }
}





