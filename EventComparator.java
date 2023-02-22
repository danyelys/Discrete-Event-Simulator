package cs2030.simulator;

import java.util.Comparator;

class EventComparator implements Comparator<Event> {
    
    EventComparator() {
    }

    @Override
    public int compare(Event event1, Event event2) {
        double comparison = event1.getTime() - event2.getTime();
        if (comparison > 0) {
            return 1;
        } else if (comparison < 0) {
            return -1;
        } else {
            int comparison1 = event1.getCustomer().getCustomerID() - 
                event2.getCustomer().getCustomerID();
            if (comparison1 > 0) {
                return 1;
            } else if (comparison1 < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}


