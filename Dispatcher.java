import java.util.ArrayList;
import java.util.List;

public class Dispatcher {
    private Airport airport;
    // Диспетчер управляет посадкой в одном аэропорту
    public Dispatcher(Airport airport) {
        this.airport = airport;
    }

    // проверка, можно ли посадить самолёт
    public boolean requestLanding(Airplane airplane) {
        if (airplane.canLand(airport) == false) {
            return false;       // не подходит по типу
        }
        if (airport.hasFreeParking() == false) {
            return false;       // нет мест
        }
        return true;            // можно садиться
    }
    // сортировка очереди: сначала с малым остатком времени, затем пассажирские
    public List<Airplane> scheduleLanding(List<Airplane> waiting) {
        List<Airplane> sorted = new ArrayList<>(waiting);

        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = i + 1; j < sorted.size(); j++) {
                Airplane a1 = sorted.get(i);
                Airplane a2 = sorted.get(j);
                // сравниваем по остатку времени
                if (a1.getRemainingTime() > a2.getRemainingTime()) {
                    sorted.set(i, a2);
                    sorted.set(j, a1);
                }
                // если время одинаковое, пассажирские выше
                else if (a1.getRemainingTime() == a2.getRemainingTime()) {
                    String a1Type = a1.getType();
                    String a2Type = a2.getType();
                    if (a1Type.equals("PASSENGER") == false) {
                        if (a2Type.equals("PASSENGER") == true) {
                            sorted.set(i, a2);
                            sorted.set(j, a1);
                        }
                    }
                }
            }
        }

        return sorted;
    }
}