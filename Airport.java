import java.util.ArrayList;
import java.util.List;

public class Airport {
    private String name;
    private String airportType;
    private int parkingSpots;
    private List<Airplane> parkedAirplanes;
    private List<Airplane> waitingAirplanes;
    private Dispatcher dispatcher;
    private boolean isRunwayBusy;

    public Airport(String name, int parkingSpots, String airportType) {
        this.name = name;
        this.airportType = airportType;
        this.parkingSpots = parkingSpots;
        this.parkedAirplanes = new ArrayList<>();
        this.waitingAirplanes = new ArrayList<>();
        this.dispatcher = new Dispatcher(this);
        this.isRunwayBusy = false;
    }

    public String getName() {
        return name;
    }

    public int getParkingSpots() {
        return parkingSpots;
    }

    public String getAirportType() {
        return airportType;
    }

    public List<Airplane> getParkedAirplanes() {
        return parkedAirplanes;
    }

    public List<Airplane> getWaitingAirplanes() {
        return waitingAirplanes;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public boolean hasFreeParking() {
        return parkedAirplanes.size() < parkingSpots;
    }

    public boolean isRunwayFree() {
        if (isRunwayBusy == false) {
            return true;
        } else {
            return false;
        }
    }

    public void occupyRunway() {
        isRunwayBusy = true;
    }

    public void releaseRunway() {
        isRunwayBusy = false;
    }

    public boolean parkAirplane(Airplane airplane) {
        if (hasFreeParking() == true) {
            parkedAirplanes.add(airplane);
            airplane.setInAir(false);
            airplane.setParkedAt(this);
            airplane.setRemainingService(airplane.getServiceTime());
            airplane.unload();
            return true;
        }
        return false;
    }

    public boolean unparkAirplane(Airplane airplane) {
        if (parkedAirplanes.remove(airplane) == true) {
            airplane.setParkedAt(null);
            return true;
        }
        return false;
    }

    public void addToWaiting(Airplane airplane) {
        waitingAirplanes.add(airplane);
    }

    public boolean removeFromWaiting(Airplane airplane) {
        if (waitingAirplanes.remove(airplane) == true) {
            return true;
        }
        return false;
    }

    public List<Airplane> processLandings() {
        List<Airplane> crashed = new ArrayList<>();

        // обновляем время полёта и находим разбившиеся самолёты
        for (int i = 0; i < waitingAirplanes.size(); i++) {
            Airplane airplane = waitingAirplanes.get(i);
            airplane.updateFlightTime(1); //прошел 1 шаг времени
            //если время консилочь - самолет разбился
            if (airplane.getRemainingTime() <= 0) {
                crashed.add(airplane);
            }
        }

        for (int i = 0; i < crashed.size(); i++) {
            Airplane airplane = crashed.get(i);
            waitingAirplanes.remove(airplane);
            System.out.println("КРУШЕНИЕ! " + airplane + " не успел сесть в " + name);
        }

        // сортируем оставшиеся самолёты через диспетчера
        List<Airplane> sorted = dispatcher.scheduleLanding(waitingAirplanes);

        // сажаем самолёты, если есть свободные места
        for (int i = 0; i < sorted.size(); i++) {
            Airplane airplane = sorted.get(i);
            if (hasFreeParking() == false) {
                break;
            }
            else {
                if (dispatcher.requestLanding(airplane) == true) { //спрашивем диспетчера разрешение на посадку
                    occupyRunway();
                    parkAirplane(airplane);
                    waitingAirplanes.remove(airplane);
                    releaseRunway();
                }
            }
        }

        return crashed;
    }

    public List<Airplane> processParking() {
        List<Airplane> ready = new ArrayList<>();
        // уменьшаем время обслуживания у всех самолётов на стоянке
        for (int i = 0; i < parkedAirplanes.size(); i++) {
            Airplane airplane = parkedAirplanes.get(i);
            airplane.setRemainingService(airplane.getRemainingService() - 1);
            //время законч. - самолет готов лететь
            if (airplane.getRemainingService() <= 0) {
                ready.add(airplane);
            }
        }

        for (int i = 0; i < ready.size(); i++) {
            Airplane airplane = ready.get(i);
            unparkAirplane(airplane);
        }

        return ready;
    }
    // один шаг симуляции аэропорта
    public List<Airplane> step() {
        // обрабатываем посадки (крушения + посадки)
        List<Airplane> crashed = processLandings();
        //обрабатываем стоянки (уменьшаем время обслуживания)
        List<Airplane> ready = processParking();
        // объединяем оба списка в один
        List<Airplane> result = new ArrayList<>();
        for (int i = 0; i < crashed.size(); i++) {
            result.add(crashed.get(i));
        }
        for (int i = 0; i < ready.size(); i++) {
            result.add(ready.get(i));
        }
        return result;  // возвращаем разбившихся + готовых к вылету
    }
}