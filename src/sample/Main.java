package sample;

import javafx.application.Application;

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

        // Popup on start
        Platform.setImplicitExit(false);
        Stage popup = new Stage();
        popup.setTitle("Select");

        FileChooser fileChooser = new FileChooser();

        // Button openButton = new Button("Open a picture in desired directory");

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

        // Creating new stage so I can showAndWait
        Stage imageStage = new Stage();

        popup.setOnHidden((event) -> {
            // Iterate over all images images directory
            File imageFolder = new File("images/");
            File[] listOfImages = imageFolder.listFiles();
            for (int i = 0; i < listOfImages.length; i++) {
                StackPane sp = new StackPane();
                // Load in current image, exception bc may be null
                File currentFile = listOfImages[i];
                Image currentImage = new Image("file:" + listOfImages[i].toString());
                imageStage.setTitle("Image " + listOfImages[i].getName());
                ImageView viewCurrentImage = new ImageView(currentImage);
                sp.getChildren().add(viewCurrentImage);

                ArrayList<Double> coordinates = new ArrayList<>();

                viewCurrentImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

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

                Button imageCloser = new Button("Next Image");
                sp.getChildren().add(imageCloser);

                imageCloser.setOnAction(new EventHandler<ActionEvent>() {
                    // Write the labels to a text file
                    public void handle(ActionEvent event) {
                        try {
                            FileWriter writer = new FileWriter(new File("output/output" + currentFile.getName().substring(0, currentFile.getName().indexOf('.')) + ".txt"));
                            for (Double k : coordinates) {
                                writer.write(String.valueOf(k) + " ");
                            }
                            writer.close();
                            imageStage.hide();
                        } catch (IOException ex) {
                            imageStage.hide();
                        }
                    }
                });


                Scene scene = new Scene(sp);
                imageStage.setResizable(false);
                imageStage.setScene(scene);
                imageStage.showAndWait();
            }

        });
        Scene popupScene = new Scene(vbox, 150, 200);
        popup.setScene(popupScene);
        popup.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
