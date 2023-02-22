package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;
import java.lang.Comparable;

class WaitEvent extends EventStub {
    private final Customer cust;
    private final Server server;
    private static final int eventID = 2;

    WaitEvent(Customer customer, double eventTime, Server server) {
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
        Shop newShop = shop.wait(cust).first();
        return Pair.of(Optional.<Event>empty(), newShop);
    }

    @Override
    public String toString() {
        return String.format("%s %s waits at %s", 
                super.toString(), cust.toString(), server.toString());
    }
}
