package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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
    public int insMT = 0;
    public int insFt = 0;

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
            e.printStackTrace();
        }
        try {
            //Class.forName("org.sqlite.JDBC");
            Statement stmt = c.createStatement();
            try (ResultSet rs0 = stmt.executeQuery("select * from mytable WHERE NUTS3 = 'AT123';")) {
                while (rs0.next()) {
                    insFt = rs0.getInt("DEATHS_FEMALE");
                    insMT = rs0.getInt("DEATHS_MALE");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) {
        Stage ps=primaryStage;

        try {
            c=DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\SterbenDatenbankFertig.db");
            //System.out.println("erfolgreich verbindetet");
            ausfueren();
        } catch (SQLException e) {
            //System.out.println("weinen");
            e.printStackTrace();
        }

        //Normales PiChart
        //piChartDiagramm(ps);

        //Balkendiamgramm mit St.Pölten männer vs frauen
        barChartDiagramm(ps);

        primaryStage.show();
    }

    private void barChartDiagramm(Stage primaryStage) {
        primaryStage.setTitle("Sterblichkeit der Männer und Frauen in St.Pölten in 2018");

        final String m = "Maenner";
        final String f = "Frauen";

        final NumberAxis yAxe = new NumberAxis();
        final CategoryAxis xAxe = new CategoryAxis();
        xAxe.setLabel("Geschlecht");

        //BarChart barChart= new BarChart(xAxe, yAxe);
        final BarChart<String,Number> barChart;
        barChart = new BarChart<String,Number>(xAxe,yAxe);

        XYChart.Series series = new XYChart.Series();

        series.getData().add(new XYChart.Data(m, insMT));
        series.getData().add(new XYChart.Data(f, insFt));

        Scene scene = new Scene(barChart, 1000, 1000);
        barChart.getData().addAll(series);

        primaryStage.setScene(scene);
    }

    private void piChartDiagramm(Stage primaryStage) {
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
    }
}