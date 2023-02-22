package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;
import java.lang.Comparable;
import java.util.function.Supplier;

class ServeEvent extends EventStub {
    private final Customer cust;
    private final Server server;
    private static final int eventID = 5;

    ServeEvent(Customer customer, double eventTime, Server server) {
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
        double newTime = this.getTime() + cust.getSupplier().get();
        Shop newShop = shop;
        if (server.isQueueEmpty()) {
            newShop = shop.serve(this.getTime(), cust.getSupplier())
                .first();
        } else {
            newShop = shop.waitServe(this.getTime(), cust.getSupplier(), 
                    server).first();
        }
        Customer newCustomer = cust.updateWaitTime(this.getTime());
        DoneEvent done = new DoneEvent(newCustomer, newTime, server);
        return Pair.of(Optional.<Event>of(done), newShop);
    }

    @Override
    public String toString() {
        return String.format("%s %s serves by %s", 
                super.toString(), cust.toString(), server.toString());
    }
}
