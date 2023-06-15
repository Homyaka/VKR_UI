package com.vkrui.vkr_ui;

import constraintchoco.Constraintchoco;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.chocosolver.solver.exception.ContradictionException;

import java.io.File;
import java.io.IOException;

public class MainController {
    public ScrollPane paneSolutions;
    File selectFile;
    Constraintchoco cc=new Constraintchoco();
    @FXML
    private Button btnSelectFile;

    @FXML
    private Button btnStart;

    @FXML
    private Label labelFileName;

    @FXML
    private Label labelSelect;

   // @FXML
    //private Label textSolutions;
    @FXML
    private Label textSolutions;
    @FXML
    private TextField tfFrequency;

    public  Font font= new Font("Times New Roman", 24);


    public  void selectFile(){
        Stage stage=new Stage();
        FileChooser fileChooser=new FileChooser();
        selectFile=fileChooser.showOpenDialog(stage);
        System.out.println(selectFile.getPath());
        if (selectFile.isFile()){
            labelFileName.setText(selectFile.getPath());
            labelSelect.setText("Выбран:");
        }
    }

    @FXML
    void initialize(){
        btnSelectFile.setOnAction(event -> {
            selectFile();
            System.out.println(selectFile.getPath());
        });
        btnStart.setOnAction(event -> {
            System.out.println("qeqe"+selectFile.getPath());
            try {
                if (selectFile.isFile()|| !tfFrequency.getText().isEmpty()) {
                    //paneSolutions.setPrefViewportHeight(500);
                   // paneSolutions.setMinViewportWidth(400);
                    textSolutions=new Label();
                    textSolutions.setFont(font);
                    textSolutions.setText(cc.findInDSystem(selectFile.getPath(), Integer.parseInt(tfFrequency.getText())));
                    paneSolutions.setContent(textSolutions);
                   // paneSolutions.setFitToHeight(false);
                   // paneSolutions.setFitToWidth(false);
                    paneSolutions.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    paneSolutions.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                }
            } catch (IOException | ContradictionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}