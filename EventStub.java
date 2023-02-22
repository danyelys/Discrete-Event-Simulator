package cs2030.simulator;

import java.lang.Comparable;
import java.util.Optional;
import cs2030.util.Pair;

class EventStub implements Event, Comparable<EventStub> {
  
    private final Customer customer;
    private final double eventTime;
    private final Server server;
    private static final int eventID = 0;

    EventStub(Customer customer, double eventTime) {
        this.customer = customer;
        this.eventTime = eventTime;
        this.server = new Server(0, 0);
    }

    EventStub(Customer customer, double eventTime, Server server) {
        this.customer = customer;
        this.eventTime = eventTime;
        this.server = server;
    }

    @Override
    public int getEventID() {
        return eventID;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public double getTime() {
        return eventTime;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        return Pair.of(Optional.<Event>empty(), shop);
    }
    
    @Override
    public int compareTo(EventStub event) {
        double comparison = eventTime - event.eventTime;
        if (comparison > 0) {
            return 1;
        } else if (comparison < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("%.3f", eventTime);
    }
}
