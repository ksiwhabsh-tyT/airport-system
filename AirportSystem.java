import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AirportSystem {
    private List<Airport> airports;
    private List<Airplane> crashedAirplanes;
    private int totalCrashes;
    private int passengersKilled;
    private double cargoLostKg;
    private Random random;
    private int crashedPassenger = 0;
    private int crashedCargo = 0;
    private int crashedMilitary = 0;

    public AirportSystem() {
        airports = new ArrayList<>();
        crashedAirplanes = new ArrayList<>();
        totalCrashes = 0;
        passengersKilled = 0;
        cargoLostKg = 0.0;
        random = new Random();
    }

    public void addAirport(Airport airport) {
        airports.add(airport);
    }
    // выбираем случайный аэропорт для вылета (не текущий)
    private Airport getRandomDestination(Airplane airplane, Airport currentAirport) {
        List<Airport> suitable = new ArrayList<>();
        // перебираем все аэропорты
        for (int i = 0; i < airports.size(); i++) {
            Airport airport = airports.get(i);
            if (airport != currentAirport) {
                if (airplane.canLand(airport) == true) {
                    suitable.add(airport);
                }
            }
        }
        // если нет подходящих - возвращаем первый попавшийся
        if (suitable.size() == 0) {
            return airports.get(0);
        }
        // выбираем случайный аэропорт из подходящих
        int index = random.nextInt(suitable.size());
        return suitable.get(index);
    }

    public void runSimulation(int steps) {
        for (int step = 0; step < steps; step++) {
            System.out.println("Шаг " + step + ":");

            for (int i = 0; i < airports.size(); i++) {
                Airport airport = airports.get(i);
                List<Airplane> result = airport.step();

                for (int j = 0; j < result.size(); j++) {
                    Airplane airplane = result.get(j);

                    if (airplane.getRemainingTime() <= 0) {
                        crashedAirplanes.add(airplane);
                        totalCrashes = totalCrashes + 1;

                        String airplaneType = airplane.getType();
                        if (airplaneType.equals("PASSENGER")) {
                            passengersKilled = passengersKilled + ((PassengerAirplane) airplane).getOccupiedSeats();
                            crashedPassenger = crashedPassenger + 1;
                        }
                        else if (airplaneType.equals("CARGO")) {
                            cargoLostKg = cargoLostKg + ((CargoAirplane) airplane).getCargoWeight();
                            crashedCargo = crashedCargo + 1;
                        }
                        else if (airplaneType.equals("MILITARY")) {
                            crashedMilitary = crashedMilitary + 1;
                        }
                    }
                    else if (airplane.isInAir() == false) {
                        Airport newDestination = getRandomDestination(airplane, airport);
                        airplane.setDestination(newDestination);
                        airplane.setInAir(true);
                        airplane.setRemainingTime(airplane.getMaxFlightTime());
                        airplane.load();
                        newDestination.addToWaiting(airplane);
                        System.out.println("Самолёт " + airplane.getId() + " вылетел из " + airport.getName() + " в " + newDestination.getName());
                    }
                }
            }
        }
    }
    // выводит список всех самолётов в воздухе
    public void listAllAirborne() {
        System.out.println("САМОЛЕТЫ В ВОЗДУХЕ:");
        boolean found = false; //флаг, есть ли хоть один самолет
        // проверяем каждый аэропорт
        for (int i = 0; i < airports.size(); i++) {
            Airport airport = airports.get(i);
            List<Airplane> waiting = airport.getWaitingAirplanes(); // самолёты, летящие сюда
            if (waiting.size() != 0) {
                found = true;
                System.out.println("летят в " + airport.getName() + ":");
                for (int j = 0; j < waiting.size(); j++) {
                    Airplane a = waiting.get(j);
                    System.out.println("    " + a);
                }
            }
        }
        if (found == false) {
            System.out.println("нет самолётов в воздухе");
        }
    }
    // выводит список всех самолётов на стоянке
    public void listAllParked() {
        System.out.println("САМОЛЕТЫ НА СТОЯНКЕ:");
        boolean found = false;
        for (int i = 0; i < airports.size(); i++) {
            Airport airport = airports.get(i);
            List<Airplane> parked = airport.getParkedAirplanes(); // самолёты на стоянке
            if (parked.size() != 0) {
                found = true;
                System.out.println("  " + airport.getName() + ":");
                for (int j = 0; j < parked.size(); j++) {
                    Airplane a = parked.get(j);
                    System.out.println("    " + a);
                }
            }
        }
        if (found == false) {
            System.out.println("нет самолётов на стоянке");
        }
    }

    public void printPassengersInFlight() {
        int total = 0;
        for (int i = 0; i < airports.size(); i++) {
            Airport airport = airports.get(i);
            List<Airplane> waiting = airport.getWaitingAirplanes();
            for (int j = 0; j < waiting.size(); j++) {
                Airplane a = waiting.get(j);
                if (a.getType().equals("PASSENGER")) {
                    total = total + ((PassengerAirplane) a).getOccupiedSeats();
                }
            }
        }
        System.out.println("пассажиры в полете: " + total);
    }

    public void printCrashStatistics() {
        System.out.println("СТАТИСТИКА КРУШЕНИЙ:");
        System.out.println("всего крушений: " + totalCrashes);
        System.out.println("погибло пассажиров: " + passengersKilled);
        System.out.println("потеряно груза: " + cargoLostKg + " кг");
    }

    public void printAllReports() {
        listAllAirborne();
        listAllParked();
        printPassengersInFlight();
        printCrashStatistics();
    }
    public List<Airport> getAirports() {
        return airports;
    }

    public int getTotalCrashes() {
        return totalCrashes;
    }

    public int getPassengersKilled() {
        return passengersKilled;
    }

    public double getCargoLostKg() {
        return cargoLostKg;
    }
    public int getCrashedPassenger() { return crashedPassenger; }
    public int getCrashedCargo() { return crashedCargo; }
    public int getCrashedMilitary() { return crashedMilitary; }

}