package cs2030.simulator;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import cs2030.util.PQ;
import cs2030.util.ImList;

public class Simulate2 {
    private final int numServers;
    private final Shop shop;
    private final PQ<Event> arrivalTimes;
    private static final Comparator<Event> cmp = new EventComparator();

    public Simulate2(int numServer, List<Double> list) {
        this.numServers = numServer;
        ImList<Server> serve = ImList.<Server>of();
        for (int i = 1; i <= numServer; i++) {
            Server newServer = new Server(i, 1);
            serve = serve.add(newServer);
        }
        this.shop = new Shop(serve);

        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 1; i < list.size() + 1; i++) {
            Customer c = new Customer(i, list.get(i - 1));
            Event e = new EventStub(c, list.get(i - 1));
            pq = pq.add(e);
        }
        this.arrivalTimes = pq;
    }

    public String run() {
        String simulationString = "";
        PQ<Event> pq = new PQ<Event>(cmp, arrivalTimes);
        while (!pq.isEmpty()) {
            String next = pq.poll().first().toString();
            pq = pq.poll().second();
            simulationString += next + "\n";
        }
        simulationString += "-- End of Simulation --";
        return simulationString;
    }
            
    @Override
    public String toString() {
        return String.format("Queue: %s; Shop: %s", 
                arrivalTimes.toString(), shop.toString());
    }
}
