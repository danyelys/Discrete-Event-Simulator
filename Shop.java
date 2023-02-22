package cs2030.simulator;

import java.util.List;
import java.util.function.Supplier;
import cs2030.util.ImList;
import cs2030.util.Pair;
import cs2030.util.Lazy;

class Shop {
    private final ImList<Server> servers;
    private final ImList<Double> serverAvailableTimes;   

    Shop(List<Server> servers) {
        this.servers = ImList.<Server>of(servers);
        ImList<Double> serve = ImList.<Double>of();
        for (int i = 0; i < servers.size(); i++) {
            serve = serve.add(0.0);
        }
        this.serverAvailableTimes = serve;
    }

    Shop(List<Server> servers, int maxQ) {
        this.servers = ImList.<Server>of(servers);
        ImList<Double> serve = ImList.<Double>of();
        for (int i = 0; i < servers.size(); i++) {
            serve = serve.add(0.0);
        }
        this.serverAvailableTimes = serve;
    }

    Shop(ImList<Server> servers) {
        this.servers = ImList.<Server>of(servers);
        ImList<Double> serve = ImList.<Double>of();
        for (int i = 0; i < servers.size(); i++) {
            serve = serve.add(0.0);
        }
        this.serverAvailableTimes = serve;
    }

    Shop(ImList<Server> servers, int maxQ) {
        this.servers = ImList.<Server>of(servers);
        ImList<Double> serve = ImList.<Double>of();
        for (int i = 0; i < servers.size(); i++) {
            serve = serve.add(0.0);
        }
        this.serverAvailableTimes = serve;
    }

    Shop(ImList<Server> servers, ImList<Double> newAvailTime) {
        this.servers = servers;
        this.serverAvailableTimes = newAvailTime;
    }

    boolean hasSpace() {
        for (int i = 0; i < servers.size(); i++) {   
            if (!servers.get(i).isQueueFull()) {
                return true;
            }
        }
        return false;
    }

    boolean hasServer(double time) {
        for (int i = 0; i < serverAvailableTimes.size(); i++) {
            if (serverAvailableTimes.get(i) <= time) {
                return true;
            }
        }
        return false;
    }

    Server nextServe(double time) {
        for (int i = 0; i < serverAvailableTimes.size(); i++) {
            if (serverAvailableTimes.get(i) <= time) {
                return servers.get(i);
            }
        }
        return new Server(0, 0);
    }

    Pair<Shop, Server> serve(double time, Lazy<Double> serveTime) {
        double serveEndTime = time + serveTime.get();
        for (int i = 0; i < serverAvailableTimes.size(); i++) {
            if (serverAvailableTimes.get(i) <= time) {
                Shop newShop = new Shop(servers, serverAvailableTimes.set(i, serveEndTime));
                return Pair.of(newShop, servers.get(i));
            }
        }
        return Pair.of(this, new Server(0,0));
    }

    Pair<Shop, Customer> waitServe(double time, Lazy<Double> serveTime, 
            Server server) {
        int index = server.getServerID() - 1;
        double serveEndTime = time + serveTime.get();
        if (servers.get(index).isSelfCheck()) {
            return Pair.of(this.serveAllSelfCheck(serveEndTime, index),
                    servers.get(index).serveCustomer().second());
        }

        return Pair.of(new Shop(servers.set(index, 
                        servers.get(index).serveCustomer().first()), 
                    serverAvailableTimes.set(index, serveEndTime)), 
                servers.get(index).serveCustomer().second());
    }
            
    Pair<Shop, Server> wait(Customer customer) {
        Shop newShop = this;
        for (int i = 0; i < servers.size(); i++) {
            if (!servers.get(i).isQueueFull()) {
                if (!servers.get(i).isSelfCheck()) {
                    newShop = new Shop(servers.set(i, servers.get(i)
                                .waitCustomer(customer)), serverAvailableTimes);
                    return Pair.of(newShop, servers.get(i)
                            .waitCustomer(customer));
                } else {
                    return Pair.of(this.waitAllSelfCheck(customer), 
                             servers.get(i).waitCustomer(customer));
                }
                        
            }
        }
        return Pair.of(newShop, new Server(0,0));
    }
    
    Shop waitAllSelfCheck(Customer customer) {
        Shop newShop = this;
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isSelfCheck()) {
                newShop = new Shop(newShop.servers.set(i, servers.get(i)
                            .waitCustomer(customer)), serverAvailableTimes);
            }
        }
        return newShop;
    }

    Shop serveAllSelfCheck(double time, int index) {
        Shop newShop = this;
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isSelfCheck()) {
                newShop = new Shop(newShop.servers.set(i, servers.get(i)
                            .serveCustomer().first()), newShop
                        .serverAvailableTimes.set(index, time));
            }
        }
        return newShop;
    }

    Server getServer(Server server) {
        return servers.get(server.getServerID() - 1);
    }

    Shop restServer(double time, Server server) {
        Shop newShop = new Shop(servers, serverAvailableTimes
                .set(server.getServerID() - 1, serverAvailableTimes.get(server
                        .getServerID() - 1) + time));
        return newShop;
    } 
        
    @Override
    public String toString() {
        return this.servers.toString();
    }
}
