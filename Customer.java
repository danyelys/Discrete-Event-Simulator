package cs2030.simulator;

import java.util.function.Supplier;
import cs2030.util.Lazy;

class Customer {
    private final int customerID;
    private final double arrTime;
    private final Lazy<Double> serveTime;
    private final double waitTime;

    Customer(int customerID, double arrTime) {
        this.customerID = customerID;
        this.arrTime = arrTime;
        Supplier<Double> supplier = () -> 1.0;
        Lazy<Double> serve = Lazy.<Double>of(supplier);
        this.serveTime = serve;
        this.waitTime = 0.0;
    }

    Customer(int customerID, double arrTime, Supplier<Double> supplier) {
        this.customerID = customerID;
        this.arrTime = arrTime;
        this.serveTime = Lazy.<Double>of(supplier);
        this.waitTime = 0.0;
    }

    Customer(int customerID, double arrTime, Lazy<Double> supplier, 
            double waitTime) {
        this.customerID = customerID;
        this.arrTime = arrTime;
        this.serveTime = supplier;
        this.waitTime = waitTime;
    }

    Customer updateWaitTime(double time) {
        return new Customer(customerID, arrTime, serveTime, time - arrTime);
    }

    double waitingTime() {
        return waitTime;
    }

    Lazy<Double> getSupplier() {
        return serveTime;
    }

    int getCustomerID() {
        return customerID;
    }

    @Override
    public String toString() {
        return String.format("%d", customerID);
    }
}
