package Controller;

import Application.QueueManagementSystem;
import BusinesLogic.SimulationManager;
import Exceptions.InvalidInputException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {
    @FXML
    TextField nrClients;
    @FXML
    TextField nrQueues;
    @FXML
    TextField minArrivalTime;
    @FXML
    TextField maxArrivalTime;
    @FXML
    TextField minServiceTime;
    @FXML
    TextField maxServiceTime;
    @FXML
    TextField periodOfSimulation;
    @FXML
    Label invalidFormat;
    @FXML
    Button startButton;
    @FXML
    ChoiceBox strategy;

    public void initialize(){
        strategy.getItems().add("Shortest Time Strategy");
        strategy.getItems().add("Shortest Queue Strategy");
        strategy.getSelectionModel().selectFirst();
    }
    @FXML
    public void onStartClick() throws IOException {
        SimulationManager simulationManager = new SimulationManager();
        try {
            invalidFormat.setVisible(false);
            simulationManager.validateInput(periodOfSimulation.getText(), maxArrivalTime.getText(), minArrivalTime.getText(), maxServiceTime.getText()
            ,minServiceTime.getText(), nrQueues.getText(), nrClients.getText());
            simulationManager.setInput(periodOfSimulation.getText(), maxArrivalTime.getText(), minArrivalTime.getText(), maxServiceTime.getText()
                    ,minServiceTime.getText(), nrQueues.getText(), nrClients.getText(), strategy.getSelectionModel().getSelectedItem().toString());

            Stage stage = QueueManagementSystem.getPrimaryStage();
            URL url = QueueManagementSystem.class.getResource("SimulationResults.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Scene scene = new Scene(fxmlLoader.load(), 698, 492);
            stage.setScene(scene);
            Thread t = new Thread(simulationManager);
            t.start();
        } catch (InvalidInputException e) {
            invalidFormat.setVisible(true);
        }
    }
}
