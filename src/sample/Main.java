package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.chart.CategoryAxis;

public class Main extends Application {

    private Connection c = null;

    public int tote = 0;
    public int toteFrauen = 0;
    public int toteMaenner = 0;
    public int insMT = 0;
    public int insFt = 0;

    public static void main(String[] args) {
        launch(args);
    }

    public void ausfueren() {
        try {
            //Class.forName("org.sqlite.JDBC");
            Statement stmt = c.createStatement();
            try (ResultSet rs0 = stmt.executeQuery(" select sum(DEATHS_TOTAL) as SUMDEATHS, sum(DEATHS_FEMALE) as " +
                    "SUMFEMALE,sum(DEATHS_MALE) as SUMMALE from mytable;")) {
                while (rs0.next()) {
                    tote = rs0.getInt("SUMDEATHS");
                    toteFrauen = rs0.getInt("SUMFEMALE");
                    toteMaenner = rs0.getInt("SUMMALE");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            //Class.forName("org.sqlite.JDBC");
            Statement stmt = c.createStatement();
            try (ResultSet rs0 = stmt.executeQuery("select sum(DEATHS_TOTAL) as SUMDEATHS, sum(DEATHS_FEMALE) as SUMFEMALE,sum(DEATHS_MALE) as SUMMALE from mytable WHERE NUTS3 = 'AT123';")) {
                while (rs0.next()) {
                    insFt = rs0.getInt("SUMFEMALE");
                    insMT = rs0.getInt("SUMMALE");
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
        } catch (SQLException e) {      //eigenes catch für drive manager und
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

        final BarChart<String,Number> barChart;
        barChart = new BarChart<String,Number>(xAxe,yAxe);

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data(insMT, m));
        series.getData().add(new XYChart.Data(insFt, f));
        /*
        XYChart.Series seriesf = new XYChart.Series();
        XYChart.Series seriesm = new XYChart.Series();
        seriesf.setName("Frauen");
        seriesm.setName("Maenner");
        seriesm.getData().add(new XYChart.Data(insMT, m));
        seriesf.getData().add(new XYChart.Data(insFt, f));
        */
        Scene scene = new Scene(barChart, 1000, 1000);
        barChart.getData().addAll(series);
        //barChart.getData().addAll(seriesm, seriesf);
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