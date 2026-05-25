public class MilitaryAirplane extends Airplane {

    public MilitaryAirplane(double fuelWeight, double speed, int maxFlightTime,
                            int serviceTime, Airport destination) {
        super("MILITARY", fuelWeight, speed,
                maxFlightTime, serviceTime, destination);
    }

    public void load() {
    }

    public void unload() {
    }

    public String toString() {
        return super.toString();
    }
}