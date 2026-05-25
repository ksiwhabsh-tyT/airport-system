public class Main {
    public static void main(String[] args) {
        // создаём аэропорты
        Airport sheremetyevo = new Airport("Шереметьево", 1, "CIVIL");
        Airport domodedovo = new Airport("Домодедово", 1, "CIVIL");
        Airport chkalovsky = new Airport("Чкаловский", 1, "MILITARY_ONLY");

        AirportSystem system = new AirportSystem();
        system.addAirport(sheremetyevo);
        system.addAirport(domodedovo);
        system.addAirport(chkalovsky);

        // пассажирские самолёты
        PassengerAirplane p1 = new PassengerAirplane(50000, 850, 6, 5, sheremetyevo, 180);
        PassengerAirplane p2 = new PassengerAirplane(55000, 830, 10, 6, domodedovo, 200);
        PassengerAirplane p3 = new PassengerAirplane(48000, 860, 14, 4, sheremetyevo, 150);
        PassengerAirplane p4 = new PassengerAirplane(60000, 800, 9, 7, domodedovo, 250);

        // грузовые самолёты
        CargoAirplane c1 = new CargoAirplane(120000, 750, 25, 6, domodedovo, 30000);
        CargoAirplane c2 = new CargoAirplane(110000, 730, 22, 5, sheremetyevo, 25000);
        CargoAirplane c3 = new CargoAirplane(130000, 770, 28, 7, domodedovo, 35000);
        CargoAirplane c4 = new CargoAirplane(100000, 720, 20, 5, domodedovo, 20000);

        // военные самолёты
        MilitaryAirplane m1 = new MilitaryAirplane(15000, 900, 10, 4, chkalovsky);
        MilitaryAirplane m2 = new MilitaryAirplane(16000, 920, 13, 4, chkalovsky);
        MilitaryAirplane m3 = new MilitaryAirplane(14000, 880, 15, 5, sheremetyevo);
        MilitaryAirplane m4 = new MilitaryAirplane(17000, 950, 11, 4, chkalovsky);

        // загружаем
        p1.load();
        p2.load();
        p3.load();
        p4.load();
        c1.load();
        c2.load();
        c3.load();
        c4.load();

        // добавляем в очередь
        sheremetyevo.addToWaiting(p1);
        domodedovo.addToWaiting(p2);
        sheremetyevo.addToWaiting(p3);
        domodedovo.addToWaiting(p4);

        domodedovo.addToWaiting(c1);
        sheremetyevo.addToWaiting(c2);
        domodedovo.addToWaiting(c3);
        domodedovo.addToWaiting(c4);

        chkalovsky.addToWaiting(m1);
        chkalovsky.addToWaiting(m2);
        sheremetyevo.addToWaiting(m3);
        chkalovsky.addToWaiting(m4);

        // запускаем графический интерфейс
        new AirportGUI(system);
    }
}