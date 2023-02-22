package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;
import java.lang.Comparable;

class LeaveEvent extends EventStub {
    private final Customer cust;
    private static final int eventID = 4;

    LeaveEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
        this.cust = customer;
    }

    @Override
    public int getEventID() {
        return eventID;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        return Pair.of(Optional.<Event>empty(), shop);
    }

    @Override
    public String toString() {
        return String.format("%s %s leaves", super.toString(), cust.toString());
    }
}
