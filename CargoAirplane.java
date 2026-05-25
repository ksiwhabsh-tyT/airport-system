import java.util.Random;

public class CargoAirplane extends Airplane {
    private double maxCargoWeight;
    private double cargoWeight;

    public CargoAirplane(double fuelWeight, double speed, int maxFlightTime,
                         int serviceTime, Airport destination, double maxCargoWeight) {
        super("CARGO", fuelWeight, speed,
                maxFlightTime, serviceTime, destination);
        this.maxCargoWeight = maxCargoWeight;
        this.cargoWeight = 0.0;
    }

    public void load() {
        Random r = new Random();
        this.cargoWeight = r.nextDouble() * maxCargoWeight;
        if (this.cargoWeight < 0.1) {
            this.cargoWeight = 0.1;
        }
    }

    public void unload() {
        this.cargoWeight = 0.0;
    }

    public double getCargoWeight() {
        return cargoWeight;
    }

    protected double getCurrentWeight() {
        return getFuelWeight() + cargoWeight;
    }

    public String toString() {
        int rounded = (int)(cargoWeight + 0.5);
        return super.toString() + ", груз: " + rounded + "/" + maxCargoWeight + " кг";
    }
    public double getMaxCargoWeight() {
        return maxCargoWeight;
    }
}