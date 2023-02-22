package cs2030.simulator;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;
import cs2030.util.PQ;
import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.Lazy;

public class Simulate5 {
    private final int numServers;
    private final Shop shop;
    private final PQ<Event> events;
    private static final int waitEventID = 2;
    private static final int leaveEventID = 4;
    private static final int serveEventID = 5;
    private static final int doneEventID = 6;

    public Simulate5(int numServer, List<Pair<Double, Supplier<Double>>> list) {
        this.numServers = numServer;
        ImList<Server> serve = ImList.<Server>of();
        for (int i = 1; i <= numServer; i++) {
            Server newServer = new Server(i, 1);
            serve = serve.add(newServer);
        }
        Shop shop = new Shop(serve);
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 1; i <= list.size(); i++) {
            Supplier<Double> supplier = list.get(i - 1).second();
            Customer c = new Customer(i, list.get(i - 1).first(), 
                    list.get(i - 1).second());
            Event e = new ArriveEvent(c, list.get(i - 1).first());
            pq = pq.add(e);
        }
        this.shop = shop;
        this.events = pq;
    }

    public String run() {
        String simulationString = "";
        PQ<Event> pq = new PQ<Event>(new EventComparator(), events);
        Shop currentShop = shop;
        Statistics stat = new Statistics();
        while (!pq.isEmpty()) {
            Event current = pq.poll().first();
            if (current.getEventID() == doneEventID) {
                stat = stat.addWaitingTime(current.getCustomer().waitingTime());
                Server thisServer = current.getServer();
                Server upServer = currentShop.getServer(thisServer);
                if (!currentShop.getServer(thisServer).isQueueEmpty()) {
                    ServeEvent serveEvent = new ServeEvent(upServer
                            .serveCustomer().second(), current.getTime(),
                            upServer);
                    pq = pq.add(serveEvent);
                }
            } else if (current.getEventID() == serveEventID) {
                stat = stat.addServed();
            } else if (current.getEventID() == leaveEventID) {
                stat = stat.addLeft();
            }
            String nextString = pq.poll().first().toString();
            pq = pq.poll().second();
            simulationString += nextString + "\n";
            if (current.execute(currentShop).first() != 
                    Optional.<Event>empty()) { 
                pq = pq.add(current.execute(currentShop)
                        .first().orElse(current));
            }
            currentShop = current.execute(currentShop).second();
        }
        simulationString += stat.toString();
        return simulationString;
    }

    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s",
                events.toString(), shop.toString());
    }
}
