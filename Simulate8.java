package cs2030.simulator;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;
import cs2030.util.PQ;
import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.Lazy;

public class Simulate8 {
    private final int numServers;
    private final int numSelfServers;
    private final Shop shop;
    private final PQ<Event> events;
    private final Supplier<Double> restTimes;
    private static final int waitEventID = 2;
    private static final int leaveEventID = 4;
    private static final int serveEventID = 5;
    private static final int doneEventID = 6;
    private static final int restEndEventID = 8;

    public Simulate8(int numServer, int numOfSelfChecks, 
            List<Pair<Double, Supplier<Double>>> list, 
            int maxq, Supplier<Double> restTimes) {
        this.numServers = numServer;
        this.numSelfServers = numOfSelfChecks;
        this.restTimes = restTimes;
        ImList<Server> serve = ImList.<Server>of();
        for (int i = 1; i <= numServer; i++) {
            Server newServer = new Server(i, maxq);
            serve = serve.add(newServer);
        }
        for (int i = 1; i <= numOfSelfChecks; i++) {
            SelfServer newServer = new SelfServer(serve.size() + 1, maxq);
            serve = serve.add(newServer);
        }

        Shop shop = new Shop(serve, maxq);
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 1; i <= list.size(); i++) {
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
                RestEvent restEvent = new RestEvent(current.getCustomer(), 
                        current.getTime(), upServer, restTimes);
                double restTime = 0.0;
                if (!upServer.isSelfCheck()) {
                    restTime = restEvent.getSupplier().get();
                }
                if (!upServer.isQueueEmpty() && restTime == 0) {
                    ServeEvent serveEvent = new ServeEvent(upServer
                            .serveCustomer().second(), current.getTime() + 
                            restTime, upServer);
                    pq = pq.add(serveEvent);   
                }
                if (restTime > 0) {
                    currentShop = currentShop.restServer(restTime, upServer);
                    RestEndEvent restEnd = new RestEndEvent(current
                            .getCustomer(), current.getTime() + restTime, 
                            upServer);
                    pq = pq.add(restEnd);
                }
            } else if (current.getEventID() == serveEventID) {
                stat = stat.addServed();
            } else if (current.getEventID() == leaveEventID) {
                stat = stat.addLeft();
            }

            String nextString = current.toString();
            pq = pq.poll().second();
            if (current.getEventID() != restEndEventID) {
                simulationString += nextString + "\n";
            }
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
