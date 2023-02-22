package cs2030.simulator;

class Statistics {
    private final double waitingTime;
    private final int totalServed;
    private final int totalLeft;

    Statistics() {
        this.waitingTime = 0.0;
        this.totalServed = 0;
        this.totalLeft = 0; 
    }

    Statistics(double waitingTime, int served, int left) {
        this.waitingTime = waitingTime;
        this.totalServed = served;
        this.totalLeft = left;
    }

    double getWaitingTime() {
        return waitingTime;
    }

    int getServed() {
        return totalServed;
    }

    int getLeft() {
        return totalLeft;
    }

    public Statistics addServed() {
        return new Statistics(waitingTime, totalServed + 1, totalLeft);
    }

    public Statistics addWaitingTime(double time) {
        return new Statistics(waitingTime + time, totalServed, totalLeft);
    }

    public Statistics addLeft() {
        return new Statistics(waitingTime, totalServed, totalLeft + 1);
    }

    @Override
    public String toString() {
        return String.format("[%.3f %d %d]", waitingTime / (float)totalServed, 
                totalServed, totalLeft);
    }
}
