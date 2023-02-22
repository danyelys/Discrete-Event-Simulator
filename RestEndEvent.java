package cs2030.simulator;

import cs2030.util.Pair;
import cs2030.util.Lazy;
import java.util.function.Supplier;
import java.util.Optional;
import java.lang.Comparable;

class RestEndEvent extends EventStub {
    private final Customer cust;
    private final Server server;
    private static final int eventID = 8;

    RestEndEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime, server);
        this.cust = customer;
        this.server = server;
    }
    
    @Override
    public int getEventID() {
        return eventID;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Server upServer = shop.getServer(server);
        if (!upServer.isQueueEmpty()) {
            ServeEvent serveEvent = new ServeEvent(upServer.serveCustomer()
                    .second(), this.getTime(), upServer); 
            return Pair.of(Optional.<Event>of(serveEvent), shop);
        }
        return Pair.of(Optional.<Event>empty(), shop);
    }

    @Override
    public String toString() {
        return String.format("Server %s is back %f", server, this.getTime());
    }
}
