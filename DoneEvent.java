package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;
import java.lang.Comparable;

class DoneEvent extends EventStub {
    private final Customer cust;
    private final Server server;
    private static final int eventID = 6;

    DoneEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime, server);
        this.cust = customer;
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
    public Pair<Optional<Event>, Shop> execute(Shop shop) {        
        return Pair.of(Optional.<Event>empty(), shop);
    }

    @Override
    public String toString() {
        return String.format("%s %s done serving by %s", 
                super.toString(), cust.toString(), server.toString());
    }
}
