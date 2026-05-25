public abstract class Airplane {
    private static int nextId = 0;

    private int id;
    private String type;
    private double fuelWeight;
    private int maxFlightTime;
    private int serviceTime;
    private Airport destination;
    private int remainingTime;
    private boolean isInAir;
    private Airport parkedAt;
    private int remainingService;

    public Airplane(String type, double fuelWeight, double speed,
                    int maxFlightTime, int serviceTime, Airport destination) {
        this.id = nextId;
        nextId = nextId + 1;
        this.type = type;
        this.fuelWeight = fuelWeight;
        this.maxFlightTime = maxFlightTime;
        this.serviceTime = serviceTime;
        this.destination = destination;
        this.remainingTime = maxFlightTime;
        this.isInAir = true;
        this.parkedAt = null;
        this.remainingService = 0;
    }

    public void updateFlightTime(int delta) {
        if (isInAir == false) {
            return;
        }
        // военные: время уменьшается равномерно
        if (type.equals("MILITARY")) {
            remainingTime = remainingTime - delta;
        } else {
            // гражданские: время уменьшается с учётом веса
            double currentWeight = getCurrentWeight();
            double factor = currentWeight / fuelWeight; // насколько тяжелее пустого
            int decrease = (int)(delta * factor); // сколько вычесть
            remainingTime = remainingTime - decrease; // уменьшаем время
        }
    }
    // можно ли приземлиться в аэропорту
    public boolean canLand(Airport airport) {
        if (airport.getAirportType().equals("MILITARY_ONLY")) {
            if (type.equals("MILITARY")) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    protected double getCurrentWeight() {
        return fuelWeight;
    }

    public abstract void load();
    public abstract void unload();

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getFuelWeight() {
        return fuelWeight;
    }

    public int getMaxFlightTime() {
        return maxFlightTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public boolean isInAir() {
        return isInAir;
    }

    public Airport getDestination() {
        return destination;
    }

    public int getRemainingService() {
        return remainingService;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setInAir(boolean inAir) {
        isInAir = inAir;
    }

    public void setParkedAt(Airport parkedAt) {
        this.parkedAt = parkedAt;
    }

    public void setRemainingService(int remainingService) {
        this.remainingService = remainingService;
    }

    public String toString() {
        String typeStr;
        if (type.equals("PASSENGER")) {
            typeStr = "Пассажирский";
        } else if (type.equals("CARGO")) {
            typeStr = "Грузовой";
        } else {
            typeStr = "Военный";
        }

        String status;
        // определяем статус самолёта (в воздухе или на стоянке)
        if (isInAir == true) {
            //самолет в воздухе
            String destName = "?";
            if (destination != null) {
                destName = destination.getName(); //название аэропорта назнач
            }
            status = "В воздухе (назначение: " + destName + ")";
        } else {
            String parkingName = "?";
            if (parkedAt != null) {
                parkingName = parkedAt.getName(); // название аэропорта, где стоит
            }
            status = "На стоянке " + parkingName;
        }

        return typeStr + " " + id + ": " + status + ", осталось времени: " + remainingTime;
    }
}