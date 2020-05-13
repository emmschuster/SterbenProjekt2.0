package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

    public void ausfuerenIrgendwas() {
        try {
            Statement stmt = c.createStatement();
            try (ResultSet rs0 = stmt.executeQuery("select from mytable;")) {
                while (rs0.next()) {
                    tote = rs0.getInt("DEATHS_TOTAL");
                    toteFrauen += rs0.getInt("DEATHS_FEMALE");
                    toteMaenner += rs0.getInt("DEATHS_MALE");
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
            c=DriverManager.getConnection("jdbc:sqlite:C:/sqlite/SterbenMitCoronaZahlen.db");
            //System.out.println("erfolgreich verbindetet");
            ausfuerenIrgendwas();
        } catch (SQLException e) {
            //System.out.println("weinen");
            e.printStackTrace();
        }
        StackPane root = new StackPane();
        ObservableList<PieChart.Data> valueList = FXCollections.observableArrayList(
                new PieChart.Data("Frauen", tote),
                new PieChart.Data("Maenner", 33));
        // create a pieChart with valueList data.
        PieChart pieChart = new PieChart(valueList);
        pieChart.setTitle("Sterberate von Maenner und Frauen in Niederösterreich 2018");
        //adding pieChart to the root.
        root.getChildren().addAll(pieChart);
        Scene scene = new Scene(root, 1000, 1000);

        primaryStage.setTitle("Sterblichkeit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
