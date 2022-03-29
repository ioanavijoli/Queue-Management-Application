package BusinesLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers = new ArrayList<>();
    int maxNoServers;
    int maxTasksPerServer;
    Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        for (int nrServer = 0; nrServer < maxNoServers; nrServer++) {
            Server server = new Server(maxTasksPerServer);
            server.ID = nrServer + 1;
            server.thread.start();
            server.setRunning(true);
            servers.add(server);
        }

    }
    public void changeStrategy( SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ShortestQueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new TimeStrategy();
        }
    }
    public void dispatchTask(Task t){
         strategy.addTask(servers, t);
    }
    public List<Server> getServers() {
        return servers;
    }
}
