package BusinesLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server shortestQueue = servers.get(0);
        for(Server server: servers)
            if(server.getTasks().size() < shortestQueue.getTasks().size())
                shortestQueue = server;
        shortestQueue.addTask(task);
        task.setWaitingTime(new AtomicInteger(shortestQueue.getWaitingPeriod().get()));
    }
}
