package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Main extends Application {

    Connection c = null;

    public static void main(String[] args) {
        launch(args);
    }

    public int tote = 0;
    public int toteFrauen = 0;
    public int toteMaenner = 0;

    public void ausfueren() {
        try {
            //Class.forName("org.sqlite.JDBC");
            Statement stmt = c.createStatement();
            try (ResultSet rs0 = stmt.executeQuery("select * from mytable;")) {
                while (rs0.next()) {
                    tote = rs0.getInt("DEATHS_TOTAL");
                    toteFrauen = rs0.getInt("DEATHS_FEMALE");
                    toteMaenner = rs0.getInt("DEATHS_MALE");
                }
            }

        } catch (SQLException e) {
            //System.out.println("weinen statement nicht erfolgreich");
            //primaryStage.setTitle("weinen statement nicht erfolgreich");

            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            c=DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\SterbenDatenbankFertig.db");
            //System.out.println("erfolgreich verbindetet");
            ausfueren();
        } catch (SQLException e) {
            //System.out.println("weinen");
            e.printStackTrace();
        }
        StackPane root = new StackPane();
        ObservableList<PieChart.Data> valueList = FXCollections.observableArrayList(
                new PieChart.Data("Frauen", toteFrauen),
                new PieChart.Data("Männer", toteMaenner));
        PieChart pieChart = new PieChart(valueList);
        pieChart.setTitle("Sterberate von Männer und Frauen in Niederösterreich 2018\nFrauen: "+toteFrauen+"\nMänner: "+toteMaenner);
        root.getChildren().addAll(pieChart);
        Scene scene = new Scene(root, 1000, 1000);

        primaryStage.setTitle("Sterblichkeit");
        primaryStage.setScene(scene);
        pieChart.setLabelsVisible(true);

        //mit maus drüber fahren und dann kommt zahl
        Label caption = new Label("");
        caption.setAlignment(Label.CENTER);
     /*
        for (PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler <MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    caption.setText(String.valueOf(data.getPieValue()));
                }
            });
        }
    */
        primaryStage.show();
    }
}