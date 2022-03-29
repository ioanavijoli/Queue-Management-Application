package BusinesLogic;

import Controller.SimulationResultsController;
import Exceptions.InvalidInputException;
import Model.Server;
import Model.Task;
import javafx.application.Platform;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int maxArrivalTime;
    private int minArrivalTime;
    private int maxServiceTime;
    private int minServiceTime;
    private int numberOfQueues;
    private int numberOfClients;

    private SelectionPolicy selectionPolicy;
    private Scheduler scheduler;
    private CopyOnWriteArrayList<Task> generatedTasks = new CopyOnWriteArrayList<>();
    private String text = "";
    public void setInput(String timeLimit, String maxArrivalTime, String minArrivalTime, String maxServiceTime, String minServiceTime,
                         String numberOfQueues, String numberOfClients, String selectionPolicy) {

        this.timeLimit = Integer.valueOf(timeLimit);
        this.maxArrivalTime = Integer.valueOf(maxArrivalTime);
        this.minArrivalTime = Integer.valueOf(minArrivalTime);
        this.maxServiceTime = Integer.valueOf(maxServiceTime);
        this.minServiceTime = Integer.valueOf(minServiceTime);
        this.numberOfQueues = Integer.valueOf(numberOfQueues);
        this.numberOfClients = Integer.valueOf(numberOfClients);
        if (selectionPolicy.equals("Shortest Queue Strategy"))
            this.selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
        else
            this.selectionPolicy = SelectionPolicy.SHORTEST_TIME;
        scheduler = new Scheduler(this.numberOfQueues, this.numberOfClients);
        scheduler.changeStrategy(this.selectionPolicy);
        generateRandomTasks();
    }

    public void validateInput(String timeLimit, String maxArrivalTime, String minArrivalTime, String maxServiceTime, String minServiceTime,
                              String numberOfQueues, String numberOfClients) throws InvalidInputException {

        if (timeLimit.matches("\\d+") == false)
            throw new InvalidInputException();
        if (maxArrivalTime.matches("\\d+") == false || minArrivalTime.matches("\\d+") == false)
            throw new InvalidInputException();
        if (maxServiceTime.matches("\\d+") == false || minServiceTime.matches("\\d+") == false)
            throw new InvalidInputException();
        if (numberOfQueues.matches("\\d+") == false || numberOfClients.matches("\\d+") == false)
            throw new InvalidInputException();
        if(Integer.valueOf(maxArrivalTime) < Integer.valueOf(minArrivalTime) || Integer.valueOf(maxServiceTime) < Integer.valueOf(minServiceTime))
            throw new InvalidInputException();
    }

    public SimulationManager() {
    }

    public int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void generateRandomTasks() {
        for (int i = 1; i <= numberOfClients; i++) {
            int serviceTime = generateRandomNumber(minServiceTime, maxServiceTime);
            int arrivalTime = generateRandomNumber(minArrivalTime, maxArrivalTime);
            Task newTask = new Task(i, arrivalTime, serviceTime);
            generatedTasks.add(newTask);
        }
        Collections.sort(generatedTasks);
    }
    public void saveSimulationDataToFile(String result) throws IOException {
        String fileName = "Simulation" + selectionPolicy + System.currentTimeMillis() + ".txt";
        PrintWriter file = new PrintWriter(fileName);
        file.println(result);
        file.close();
    }
    public int numberOfClientsWaitingNow() {
        int numberOfClientsWaiting = 0;
        for (Server server : scheduler.getServers()) {
            if (!server.getTasks().isEmpty())
                numberOfClientsWaiting += server.getTasks().size();
        }
        return numberOfClientsWaiting;
    }
    public void run() {
        int currentTime = 0;
        float totalWaitingTime = 0;
        float totalServiceTime = 0;
        int peakHour = 0;
        int maxNrClientsWaiting = 0;
        while (currentTime < timeLimit) {
            for (Task task : generatedTasks) {
                if (task.getArrivalTime() == currentTime) {
                    totalServiceTime = totalServiceTime + task.getServiceTime();
                    scheduler.dispatchTask(task);
                    totalWaitingTime = totalWaitingTime + task.getWaitingTime().get();
                    generatedTasks.remove(task);
                }
            }
            int finalCurrentTime = currentTime;
            Platform.runLater(()->SimulationResultsController.display(finalCurrentTime, generatedTasks, scheduler));
            if (maxNrClientsWaiting < numberOfClientsWaitingNow()) {
                maxNrClientsWaiting = numberOfClientsWaitingNow();
                peakHour = currentTime;
            }
            currentTime++;
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Server server : scheduler.getServers()) {
            server.setRunning(false);
        }
        float finalTotalWaitingTime = totalWaitingTime; float finalTotalServiceTime = totalServiceTime;int finalPeakHour = peakHour;
        Platform.runLater(()->SimulationResultsController.text.appendText("Average waiting time: " + finalTotalWaitingTime / numberOfClients + "\n"));
        Platform.runLater(()->SimulationResultsController.text.appendText("Average service time: " + finalTotalServiceTime / numberOfClients + "\n"));
        Platform.runLater(()->SimulationResultsController.text.appendText("Peak hour: " + finalPeakHour));
        try {
            saveSimulationDataToFile(SimulationResultsController.text.getText() + "Average service time: " + finalTotalServiceTime / numberOfClients + "\n" + "Peak hour: " + finalPeakHour);
        } catch (IOException e) {
            System.out.println("Impossible to save data to file");
        }
    }
}
