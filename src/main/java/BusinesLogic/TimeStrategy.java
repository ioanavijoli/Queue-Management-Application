package BusinesLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server minWaitingPeriodServer = servers.get(0);
        for(Server server: servers)
            if(server.getWaitingPeriod().get() < minWaitingPeriodServer.getWaitingPeriod().get())
                minWaitingPeriodServer = server;
       minWaitingPeriodServer.addTask(task);
       task.setWaitingTime(new AtomicInteger(minWaitingPeriodServer.getWaitingPeriod().get()));
    }
}
