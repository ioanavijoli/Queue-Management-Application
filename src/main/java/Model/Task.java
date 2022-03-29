package Model;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.compare;

public class Task implements Comparable <Task>{
    private int ID;
    private int arrivalTime;
    private int serviceTime;
    private AtomicInteger waitingTime = new AtomicInteger();

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    @Override
    public int compareTo(Task t) {
        return compare(this.arrivalTime, t.getArrivalTime());
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(AtomicInteger waitingTime) {
        this.waitingTime = waitingTime;
    }
}
