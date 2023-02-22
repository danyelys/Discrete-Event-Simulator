package cs2030.simulator;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import cs2030.util.PQ;
import cs2030.util.ImList;
import cs2030.util.Pair;

public class Simulate3 {
    private final int numServers;
    private final Shop shop;
    private final PQ<Event> events;
    private static final int doneEventID = 6;

    public Simulate3(int numServer, List<Double> list) {
        this.numServers = numServer;
        ImList<Server> serve = ImList.<Server>of();
        for (int i = 1; i <= numServer; i++) {
            Server newServer = new Server(i, 1);
            serve = serve.add(newServer);
        }
        Shop shop = new Shop(serve);
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 1; i <= list.size(); i++) {
            Customer c = new Customer(i, list.get(i - 1));
            Event e = new ArriveEvent(c, list.get(i - 1));
            pq = pq.add(e);
        }
        this.shop = shop;
        this.events = pq;
    }

    public String run() {
        String simulationString = "";
        PQ<Event> pq = new PQ<Event>(new EventComparator(), events);
        Shop currentShop = shop;
        while (!pq.isEmpty()) {
            Event current = pq.poll().first();
            if (current.getEventID() == doneEventID) {
                Server thisServer = current.getServer();
                Server upServer = currentShop.getServer(thisServer);
                if (!upServer.isQueueEmpty()) {
                    ServeEvent serveEvent = new ServeEvent(upServer
                            .serveCustomer().second(), current.getTime(),
                            upServer);
                    pq = pq.add(serveEvent);
                }
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
        simulationString += "-- End of Simulation --";
        return simulationString;
    }

    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s",
                events.toString(), shop.toString());
    }
}
