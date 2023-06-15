package com.vkrui.vkr_ui;

import constraintchoco.Constraintchoco;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.chocosolver.solver.exception.ContradictionException;

import java.io.File;
import java.io.IOException;

public class MainController {
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

    @FXML
    private Label textSolutions;

    @FXML
    private TextField tfFrequency;


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
        });
        btnStart.setOnAction(event -> {
            try {
                if (selectFile.isFile())
                    cc.findInDSystem(selectFile.getPath());
            } catch (IOException | ContradictionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}