package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;

class Server {
    private final int serverID;
    private final ImList<Customer> customerQueue;
    private final int maxQ;
    private static final boolean isSelfCheck = false;

    Server(int serverID) {
        this.serverID = serverID;
        this.maxQ = 1;
        this.customerQueue = ImList.<Customer>of();
    }

    Server(int serverID, int maxQ) {
        this.serverID = serverID;
        this.maxQ = maxQ;
        this.customerQueue = ImList.<Customer>of();
    }

    Server(int serverID, ImList<Customer> customerQueue, int maxQ) {
        this.serverID = serverID;
        this.maxQ = maxQ;
        this.customerQueue = customerQueue;
    }

    boolean isSelfCheck() {
        return isSelfCheck;
    }

    Server waitCustomer(Customer customer) {
        return new Server(serverID, customerQueue.add(customer), maxQ);
    }

    Pair<Server, Customer> serveCustomer() {
        return Pair.of(new Server(serverID, customerQueue.remove(0).second(), maxQ), 
                customerQueue.remove(0).first());
    }

    String getQ() {
        return customerQueue.toString();
    }

    int getServerID() {
        return serverID;
    }

    boolean isQueueFull() {
        return customerQueue.size() == maxQ;
    }

    boolean isQueueEmpty() {
        return customerQueue.isEmpty();
      
    }

    @Override
    public String toString() {
        return String.format("%d", serverID);
    }
}
