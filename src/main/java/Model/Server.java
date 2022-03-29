package Model;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod = new AtomicInteger();
    public final Thread thread;
    private boolean isRunning;
    public int ID;

    public Server(int maxNumberOfTasks) {
        ID = 0;
        isRunning = false;
        tasks = new ArrayBlockingQueue<>(maxNumberOfTasks);
        waitingPeriod.set(0);
        thread = new Thread(this);
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    public void run() {
        while (isRunning == true || tasks.size() > 0) {
            if (tasks.size() > 0) {
                try {
                    Task myTask = tasks.peek();
                    int time = myTask.getServiceTime();
                    for(int i = 1; i<= time; i++){
                        Thread.currentThread().sleep(1000);
                        waitingPeriod.decrementAndGet();
                        myTask.setServiceTime(myTask.getServiceTime() - 1);
                    }

                    tasks.take();
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    e.printStackTrace();
                }
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }


    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
