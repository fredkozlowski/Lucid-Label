package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


import java.io.*;
import java.util.*;

import javafx.application.Platform;
import javafx.stage.WindowEvent;
import javafx.scene.shape.*;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Popup on start
        Platform.setImplicitExit(false);
        Stage popup = new Stage();
        popup.setTitle("Select");

        FileChooser fileChooser = new FileChooser();

        //Button openButton = new Button("Open a picture in desired directory");

        RadioButton radioYOLO = new RadioButton("YOLO");
        RadioButton radioVOC = new RadioButton("VOC");
        RadioButton radioCOCO = new RadioButton("COCO");
        RadioButton radioImageNet = new RadioButton("ImageNet");
        RadioButton radioOpenImages = new RadioButton("OpenImages");

        ToggleGroup formatGroup = new ToggleGroup();
        radioCOCO.setToggleGroup(formatGroup);
        radioImageNet.setToggleGroup(formatGroup);
        radioOpenImages.setToggleGroup(formatGroup);
        radioVOC.setToggleGroup(formatGroup);
        radioYOLO.setToggleGroup(formatGroup);

        VBox vbox = new VBox(radioCOCO, radioImageNet, radioOpenImages, radioVOC, radioYOLO);

        popup.setOnHidden((event) -> {

            primaryStage.setTitle("Image");
            StackPane sp = new StackPane();
            Image flower = new Image("file:flower.png");
            ImageView viewFlower = new ImageView(flower);
            sp.getChildren().add(viewFlower);

            ArrayList<Double> coordinates = new ArrayList<>();

            viewFlower.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent me) {
                    coordinates.add(me.getX());
                    coordinates.add(me.getY());
                    System.out.println(coordinates.toString() + ", ");

                    if (coordinates.size() % 4 == 0 && coordinates.size() != 0) {
                        Rectangle r = new Rectangle();
                        r.setWidth(RectangleCreator.rWidth((coordinates.get(coordinates.size() - 4)), coordinates.get(coordinates.size() - 2)));
                        r.setHeight(RectangleCreator.rHeight((coordinates.get(coordinates.size() - 3)), coordinates.get(coordinates.size() - 1)));
                        r.setTranslateX(RectangleCreator.rX((coordinates.get(coordinates.size() - 4)), (coordinates.get(coordinates.size() - 2)), sp.getWidth(), r.getWidth()));
                        r.setTranslateY(RectangleCreator.rY((coordinates.get(coordinates.size() - 3)), (coordinates.get(coordinates.size() - 1)), sp.getHeight(), r.getHeight()));
                        r.setFill(Color.TRANSPARENT);
                        r.setStroke(Color.RED);
                        r.setStrokeWidth(5);
                        sp.getChildren().add(r);
                    }
                }
            });

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(final WindowEvent event) {
                    try {
                        FileWriter writer = new FileWriter(new File("output.txt"));
                        for (Double k : coordinates) {
                            writer.write(String.valueOf(k) + " ");
                        }
                        writer.close();
                        System.exit(0);
                    } catch (IOException ex) {
                        System.exit(0);
                    }
                }
            });


            Scene scene = new Scene(sp);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        Scene popupScene = new Scene(vbox, 150, 200);
        popup.setScene(popupScene);
        popup.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
