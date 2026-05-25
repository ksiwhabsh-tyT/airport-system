import java.util.Random;

public class PassengerAirplane extends Airplane {
    private int seatCount;
    private int occupiedSeats;

    public PassengerAirplane(double fuelWeight, double speed, int maxFlightTime,
                             int serviceTime, Airport destination, int seatCount) {
        super("PASSENGER", fuelWeight, speed,
                maxFlightTime, serviceTime, destination);
        this.seatCount = seatCount;
        this.occupiedSeats = 0;
    }

    public void load() {
        Random r = new Random();
        this.occupiedSeats = r.nextInt(seatCount) + 1;
    }

    public void unload() {
        this.occupiedSeats = 0;
    }

    protected double getCurrentWeight() {
        return getFuelWeight() + occupiedSeats * 100;
    }

    public int getOccupiedSeats() {
        return occupiedSeats;
    }

    public String toString() {
        return super.toString() + ", пассажиры: " + occupiedSeats + "/" + seatCount;
    }

    public int getSeatCount() {
        return seatCount;
    }
}