import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.border.TitledBorder;

public class AirportGUI {
    private AirportSystem system;
    private Timer timer;
    private int step = 0;
    private int crashedPassenger = 0;
    private int crashedCargo = 0;

    private JFrame frame;
    private JTextArea airborneArea;
    private JTextArea parkedArea;
    private JLabel stepLabel;


    // окно статистики
    private JFrame statsFrame;
    private JLabel statsStep;
    private JLabel statsCrashTotal;
    private JLabel statsCrashPassenger;
    private JLabel statsCrashCargo;
    private JLabel statsCrashMilitary;
    private JLabel statsCargoLost;
    private JLabel statsPassengersKilled;

    public AirportGUI(AirportSystem system) {
        this.system = system;
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Симуляция аэропортов");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // верхняя панель (шаг)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stepLabel = new JLabel("Шаг: 0");
        stepLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(stepLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // центр (две колонки)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // левая колонка - самолёты в воздухе
        JPanel leftPanel = new JPanel(new BorderLayout());
        TitledBorder borderLeft = BorderFactory.createTitledBorder("САМОЛЕТЫ В ВОЗДУХЕ");
        borderLeft.setTitleFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.setBorder(borderLeft);
        airborneArea = new JTextArea();
        airborneArea.setEditable(false);
        airborneArea.setFont(new Font("Consolas", Font.BOLD, 16));
        leftPanel.add(new JScrollPane(airborneArea), BorderLayout.CENTER);
        centerPanel.add(leftPanel);

        // правая колонка - самолёты на стоянке
        JPanel rightPanel = new JPanel(new BorderLayout());
        TitledBorder borderRight = BorderFactory.createTitledBorder("САМОЛЕТЫ НА СТОЯНКЕ");
        borderRight.setTitleFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.setBorder(borderRight);
        parkedArea = new JTextArea();
        parkedArea.setEditable(false);
        parkedArea.setFont(new Font("Consolas", Font.BOLD, 16));
        rightPanel.add(new JScrollPane(parkedArea), BorderLayout.CENTER);
        centerPanel.add(rightPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // нижняя панель (кнопки)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createTitledBorder("УПРАВЛЕНИЕ"));

        JButton startButton = new JButton("ПУСК");
        JButton stopButton = new JButton("СТОП");
        JButton stepButton = new JButton("ШАГ");
        JButton statsButton = new JButton("СТАТИСТИКА");

        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        stopButton.setFont(new Font("Arial", Font.BOLD, 16));
        stepButton.setFont(new Font("Arial", Font.BOLD, 16));
        statsButton.setFont(new Font("Arial", Font.BOLD, 16));

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(statsButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        // таймер
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeStep();
            }
        });

        // действия кнопок
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
            }
        });

        stepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeStep();
            }
        });

        statsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showStatsWindow();
            }
        });

        // ЦВЕТА
        Color bgColor = new Color(94, 131, 174);
        Color textColor = new Color(255, 247, 208);

        frame.getContentPane().setBackground(bgColor);
        mainPanel.setBackground(bgColor);
        topPanel.setBackground(bgColor);
        centerPanel.setBackground(bgColor);
        leftPanel.setBackground(bgColor);
        rightPanel.setBackground(bgColor);
        buttonPanel.setBackground(bgColor);

        airborneArea.setBackground(bgColor);
        parkedArea.setBackground(bgColor);

        airborneArea.setForeground(textColor);
        parkedArea.setForeground(textColor);
        stepLabel.setForeground(textColor);
        borderLeft.setTitleColor(textColor);
        borderRight.setTitleColor(textColor);

        startButton.setForeground(textColor);
        stopButton.setForeground(textColor);
        stepButton.setForeground(textColor);
        statsButton.setForeground(textColor);

        startButton.setBackground(new Color(0, 100, 0));
        stopButton.setBackground(new Color(100, 0, 0));
        stepButton.setBackground(new Color(0, 0, 100));
        statsButton.setBackground(new Color(80, 50, 120));

        frame.setVisible(true);
        updateDisplay();
    }

    private void showStatsWindow() {
        if (statsFrame == null) {
            statsFrame = new JFrame("СТАТИСТИКА");
            statsFrame.setSize(400, 300);
            statsFrame.setLocation(100, 100);
            statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            statsStep = new JLabel("Шаг: 0");
            statsCrashTotal = new JLabel("Всего крушений: 0");
            statsCrashPassenger = new JLabel("Пассажирских крушений: 0");
            statsCrashCargo = new JLabel("Грузовых крушений: 0");
            statsCargoLost = new JLabel("Потеряно груза: 0 кг");
            statsPassengersKilled = new JLabel("Погибло пассажиров: 0");
            statsCrashMilitary = new JLabel("Военных крушений: 0");

            statsStep.setFont(new Font("Arial", Font.BOLD, 14));
            statsCrashTotal.setFont(new Font("Arial", Font.PLAIN, 14));
            statsCrashPassenger.setFont(new Font("Arial", Font.PLAIN, 14));
            statsCrashCargo.setFont(new Font("Arial", Font.PLAIN, 14));
            statsCargoLost.setFont(new Font("Arial", Font.PLAIN, 14));
            statsPassengersKilled.setFont(new Font("Arial", Font.PLAIN, 14));
            statsCrashMilitary.setFont(new Font("Arial", Font.PLAIN, 14));


            panel.add(statsStep);
            panel.add(statsCrashTotal);
            panel.add(statsCrashPassenger);
            panel.add(statsCrashCargo);
            panel.add(statsCargoLost);
            panel.add(statsPassengersKilled);
            panel.add(statsCrashMilitary);

            statsFrame.add(panel);
        }

        statsFrame.setVisible(true);
        updateStatsWindow();
    }

    private void updateStatsWindow() {
        if (statsFrame != null && statsFrame.isVisible()) {
            statsStep.setText("Шаг: " + step);
            statsCrashTotal.setText("Всего крушений: " + system.getTotalCrashes());
            statsCrashPassenger.setText("Пассажирских крушений: " + crashedPassenger);
            statsCrashCargo.setText("Грузовых крушений: " + crashedCargo);
            statsCrashMilitary.setText("Военных крушений: " + system.getCrashedMilitary());
            statsCargoLost.setText("Потеряно груза: " + (int) system.getCargoLostKg() + " кг");
            statsPassengersKilled.setText("Погибло пассажиров: " + system.getPassengersKilled());
        }
    }

    private void makeStep() {
        system.runSimulation(1);
        step = step + 1;
        stepLabel.setText("Шаг: " + step);

        // обновляем счётчики крушений по типам из системы
        crashedPassenger = system.getCrashedPassenger();
        crashedCargo = system.getCrashedCargo();

        updateDisplay();
        updateStatsWindow();
    }

    private void updateDisplay() {
        // БЛОК 1: САМОЛЕТЫ В ВОЗДУХЕ
        StringBuilder airborneText = new StringBuilder();
        int passengerCount = 0;
        int cargoCount = 0;
        int militaryCount = 0;
        int totalPassengers = 0;
        double totalCargo = 0;

        List<Airport> airports = system.getAirports();
        for (int i = 0; i < airports.size(); i++) {
            Airport airport = airports.get(i);
            List<Airplane> waiting = airport.getWaitingAirplanes();
            for (int j = 0; j < waiting.size(); j++) {
                Airplane a = waiting.get(j);
                if (a.getType().equals("PASSENGER")) {
                    passengerCount++;
                    totalPassengers = totalPassengers + ((PassengerAirplane) a).getOccupiedSeats();
                } else if (a.getType().equals("CARGO")) {
                    cargoCount++;
                    totalCargo = totalCargo + ((CargoAirplane) a).getCargoWeight();
                } else {
                    militaryCount++;
                }
                airborneText.append(getPlaneInfo(a)).append("\n");
            }
        }

        airborneText.insert(0, "Всего: " + (passengerCount + cargoCount + militaryCount) +
                "  |  Пассажирских: " + passengerCount + " (" + totalPassengers + " пасс.)" +
                "  |  Грузовых: " + cargoCount + " (" + (int) totalCargo + " кг)" +
                "  |  Военных: " + militaryCount + "\n\n");

        airborneArea.setText(airborneText.toString());

        // БЛОК 2: САМОЛЕТЫ НА СТОЯНКЕ
        StringBuilder parkedText = new StringBuilder();
        int parkedPassenger = 0;
        int parkedCargo = 0;
        int parkedMilitary = 0;
        int parkedPassengersTotal = 0;
        double parkedCargoTotal = 0;

        for (int i = 0; i < airports.size(); i++) {
            Airport airport = airports.get(i);
            List<Airplane> parked = airport.getParkedAirplanes();
            for (int j = 0; j < parked.size(); j++) {
                Airplane a = parked.get(j);
                if (a.getType().equals("PASSENGER")) {
                    parkedPassenger++;
                    parkedPassengersTotal = parkedPassengersTotal + ((PassengerAirplane) a).getOccupiedSeats();
                } else if (a.getType().equals("CARGO")) {
                    parkedCargo++;
                    parkedCargoTotal = parkedCargoTotal + ((CargoAirplane) a).getCargoWeight();
                } else {
                    parkedMilitary++;
                }
                parkedText.append(getParkedPlaneInfo(a)).append("\n");
            }
        }

        parkedText.insert(0, "Всего: " + (parkedPassenger + parkedCargo + parkedMilitary) +
                "  |  Пассажирских: " + parkedPassenger + " (" + parkedPassengersTotal + " пасс.)" +
                "  |  Грузовых: " + parkedCargo + " (" + (int) parkedCargoTotal + " кг)" +
                "  |  Военных: " + parkedMilitary + "\n\n");

        parkedArea.setText(parkedText.toString());

        // прокрутка в начало
        airborneArea.setCaretPosition(0);
        parkedArea.setCaretPosition(0);
    }

    private String getPlaneInfo(Airplane a) {
        String type = "";
        if (a.getType().equals("PASSENGER")) {
            type = "Пассажирский";
        } else if (a.getType().equals("CARGO")) {
            type = "Грузовой";
        } else {
            type = "Военный";
        }

        String dest = "?";
        if (a.getDestination() != null) {
            dest = a.getDestination().getName();
        }

        String info = type + " " + a.getId() + " → " + dest + " | время: " + a.getRemainingTime();

        if (a.getType().equals("PASSENGER")) {
            PassengerAirplane p = (PassengerAirplane) a;
            info = info + " | пассажиры: " + p.getOccupiedSeats() + "/" + p.getSeatCount();
        } else if (a.getType().equals("CARGO")) {
            CargoAirplane c = (CargoAirplane) a;
            info = info + " | груз: " + (int) c.getCargoWeight() + "/" + (int) c.getMaxCargoWeight() + " кг";
        }

        return info;
    }

    private String getParkedPlaneInfo(Airplane a) {
        String type = "";
        if (a.getType().equals("PASSENGER")) {
            type = "Пассажирский";
        } else if (a.getType().equals("CARGO")) {
            type = "Грузовой";
        } else {
            type = "Военный";
        }

        String info = type + " " + a.getId() + " | обслуживание: " + a.getRemainingService();

        if (a.getType().equals("PASSENGER")) {
            PassengerAirplane p = (PassengerAirplane) a;
            info = info + " | пассажиры: " + p.getOccupiedSeats() + "/" + p.getSeatCount();
        } else if (a.getType().equals("CARGO")) {
            CargoAirplane c = (CargoAirplane) a;
            info = info + " | груз: " + (int) c.getCargoWeight() + "/" + (int) c.getMaxCargoWeight() + " кг";
        }

        return info;
    }
}