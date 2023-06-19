package com.vkrui.vkr_ui;

import Mysystem.Constraintchoco;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.chocosolver.solver.exception.ContradictionException;
import workWithFiles.TxtParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainController {
    @FXML
    private Label textSolutions;
    public ScrollPane paneSolutions;
    File selectFile;
    Constraintchoco cc=new Constraintchoco();
    @FXML
    private Button DbtnSelectFile;

    @FXML
    private Button DbtnStart;

    @FXML
    private Label DlabelFileName;

    @FXML
    private Label DlabelSelect;

    @FXML
    private Button OAbtnBuildDTable;

    @FXML
    private Button OAbtnLookTable;

    @FXML
    private Button OAbtnSelectFile;

    @FXML
    private Pane OApane;

    @FXML
    private Label OAselectFile;

    @FXML
    private TextField tfFrequency;


    public  Font font= new Font("Times New Roman", 24);

    public void writter(List<String> data) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM_dd_HH_mm");
        LocalDateTime now = LocalDateTime.now();
        File outfile=new File("D:\\OATable_"+dtf.format(now)+".txt");
        FileWriter fileWriter= new FileWriter(outfile);
        for (String s:data) fileWriter.write(s+"\n");
        fileWriter.close();
    }
    public  void selectFile() throws IOException {
        Stage stage=new Stage();
        FileChooser fileChooser=new FileChooser();
        selectFile=fileChooser.showOpenDialog(stage);
        System.out.println(selectFile.getPath());
        TxtParser txtParser= new TxtParser();
       // txtParser.txtParse(selectFile.getPath());
        List<String> data=txtParser.txtParse(selectFile.getPath());
        writter(txtParser.convertToOATable(data,txtParser.codedAttribute(data)));
        if (selectFile.isFile()){
            DlabelFileName.setText(selectFile.getPath());
            DlabelSelect.setText("Выбран:");
        }
    }

    @FXML
    void initialize(){
        DbtnSelectFile.setOnAction(event -> {
            try {
                selectFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(selectFile.getPath());
        });
        DbtnStart.setOnAction(event -> {
            System.out.println("qeqe"+selectFile.getPath());
            try {
                if (selectFile.isFile()|| !tfFrequency.getText().isEmpty()) {
                    textSolutions=new Label();
                    textSolutions.setFont(font);
                    textSolutions.setText(cc.findInDSystem(selectFile.getPath(), Integer.parseInt(tfFrequency.getText())));
                    paneSolutions.setContent(textSolutions);
                    paneSolutions.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    paneSolutions.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                }
            } catch (IOException | ContradictionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}