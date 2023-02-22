package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;

class SelfServer extends Server {
    private final int customerID;
    private final ImList<Customer> customerQueue;
    private final int maxQ;
    private static final boolean isSelfCheck = true;

    SelfServer(int customerID, int maxQ) {
        super(customerID, maxQ);
        this.customerID = customerID;
        this.maxQ = maxQ;
        this.customerQueue = ImList.<Customer>of();
    }

    SelfServer(int customerID, ImList<Customer> customerQueue, int maxQ) {
        super(customerID, customerQueue, maxQ);
        this.customerID = customerID;
        this.maxQ = maxQ;
        this.customerQueue = customerQueue;
    }

    @Override
    Server waitCustomer(Customer customer) {
        return new SelfServer(customerID, customerQueue.add(customer), maxQ);
    }

    @Override
    Pair<Server, Customer> serveCustomer() {
        return Pair.of(new SelfServer(customerID, customerQueue.remove(0).second(), maxQ), 
                customerQueue.remove(0).first());
    }

    @Override
    boolean isSelfCheck() {
        return isSelfCheck;
    }
 
    @Override
    public String toString() {
        return String.format("self-check %d", customerID);
    }
}
