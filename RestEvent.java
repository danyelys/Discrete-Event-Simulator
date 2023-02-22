package cs2030.simulator;

import cs2030.util.Pair;
import cs2030.util.Lazy;
import java.util.function.Supplier;
import java.util.Optional;
import java.lang.Comparable;

class RestEvent extends EventStub {
    private final Customer cust;
    private final Server server;
    private final Lazy<Double> restTime;
    private static final int eventID = 7;

    RestEvent(Customer customer, double eventTime, Server server, 
            Supplier<Double> restTime) {
        super(customer, eventTime, server);
        this.cust = customer;
        this.server = server;
        this.restTime = Lazy.<Double>of(restTime);
    }
    
    @Override
    public int getEventID() {
        return eventID;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Shop newShop = shop.restServer(restTime.get(), server);
        return Pair.of(Optional.<Event>empty(), newShop);
    }

    Lazy<Double> getSupplier() {
        return restTime;
    }

    @Override
    public String toString() {
        return String.format("Server %s Rests", server);
    }
}
