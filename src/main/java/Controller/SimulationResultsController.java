package Controller;

import BusinesLogic.Scheduler;
import Model.Server;
import Model.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;


public class SimulationResultsController implements Initializable {
    @FXML
    TextArea resultsArea;
    public static TextArea text;

    public void initialize(URL url, ResourceBundle rb){
       text = resultsArea;
    }
    public static String display(int currentTime, CopyOnWriteArrayList<Task> generatedTasks, Scheduler scheduler) {
        text.appendText("Time: " + currentTime + "\n");
        text.appendText("Waiting Clients:");
        for (Task task : generatedTasks)
            text.appendText("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ");");
        text.appendText("\n");
        for (Server server : scheduler.getServers()) {
            text.appendText("Queue " + server.ID + ": ");
            if (server.getTasks().isEmpty())
           text.appendText("closed\n");
            else {
                for (Task task : server.getTasks())
                    text.appendText("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + "); ");
            text.appendText(" \n");
            }
        }
      text.appendText("\n");
      return text.getText();
    }
}
