package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;
import java.lang.Comparable;

class ArriveEvent extends EventStub {
    private final Customer cust;
    private static final int eventID = 1;

    ArriveEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
        this.cust = customer;
    }

    @Override
    public int getEventID() {
        return eventID;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        if (shop.hasServer(this.getTime())) {
            Server server = shop.nextServe(this.getTime());
            ServeEvent serveEvent = new ServeEvent(cust, this.getTime(), 
                    server);
            return Pair.of(Optional.<Event>of(serveEvent), shop);
        } else if (shop.hasSpace()) {
            Server server = shop.wait(cust).second(); 
            WaitEvent waitEvent = new WaitEvent(cust, this.getTime(), 
                    server);
            return Pair.of(Optional.<Event>of(waitEvent), shop);
        } else {
            LeaveEvent leaveEvent = new LeaveEvent(cust, this.getTime());
            return Pair.of(Optional.<Event>of(leaveEvent), shop);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s arrives", super.toString(), cust.toString());
    }
}
