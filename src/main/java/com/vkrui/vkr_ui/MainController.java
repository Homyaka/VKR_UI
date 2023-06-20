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
import workWithFiles.DataWorker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainController {
    @FXML
    private Label textSolutions;
    List<String> data;
    File selectFile;
    Constraintchoco cc=new Constraintchoco();

    public DataWorker dataWorker;

    @FXML
    private Button DbtnSelectFile;

    @FXML
    private Button DbtnStart;

    @FXML
    private Label DlabelFileName;

    @FXML
    private Label DlabelSelect;

    @FXML
    private Label OAFileName;

    @FXML
    private Button OAbtnBuildDTable;

    @FXML
    private Label OAlabelCreateDFile;
    @FXML
    private Button OAbtnLookTable;

    @FXML
    private Button OAbtnSelectFile;

    @FXML
    private Pane OApane;

    @FXML
    private Label OAselectFile;

    @FXML
    private Button btnDCheckFile;

    @FXML
    private Button btnSelectDataFile;

    @FXML
    private Button btnConvertToOA;

    @FXML
    private Label fileNameData;

    @FXML
    private Label labelBuildOAResult;
    @FXML
    private Label FileselectFile;

    @FXML
    private ScrollPane paneSolutions;

    @FXML
    private Label selectDataFile;

    @FXML
    private TextField tfFrequency;



    public  Font font= new Font("Times New Roman", 24);

    public void writter(List<String> data,String type) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM_dd_HH_mm");
        LocalDateTime now = LocalDateTime.now();
        File outfile=new File("D:\\"+type+"_"+dtf.format(now)+".txt");
        FileWriter fileWriter= new FileWriter(outfile);
        fileWriter.write(data.get(0));
        for (int i=1;i<data.size();i++) fileWriter.write("\n"+data.get(i));
        fileWriter.close();
        if (type.equals("DTable")) OAlabelCreateDFile.setText("Создан файл: "+outfile.getPath());
        if (type.equals("OATable")) labelBuildOAResult.setText("Cоздан файл: "+ outfile.getPath());

    }
    public void selectFile(Label labelSelect,Label fileName) throws IOException {
        Stage stage=new Stage();
        FileChooser fileChooser=new FileChooser();
        selectFile=fileChooser.showOpenDialog(stage);
        System.out.println(selectFile.getPath());
        /*DataWorker dataWorker = new DataWorker();
        List<String> data= dataWorker.txtParse(selectFile.getPath());
        dataWorker.codedAttribute(data);
        writter(dataWorker.convertToOATable(data, dataWorker.attributeSet));*/
        if (selectFile.isFile()){
            fileName.setText(selectFile.getPath());
            labelSelect.setText("Выбран:");
        }
    }
    @FXML
    void initialize(){
        DbtnSelectFile.setOnAction(event -> {
            try {
                selectFile(DlabelSelect,DlabelFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(selectFile.getPath());
        });
        DbtnStart.setOnAction(event -> {
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
        OAbtnSelectFile.setOnAction(event -> {
            try {
                selectFile(OAselectFile,OAFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        OAbtnBuildDTable.setOnAction(event -> {
            dataWorker= new DataWorker();
            try {
                data = dataWorker.txtParse(selectFile.getPath());
                dataWorker.generatematrix(data);
                writter(dataWorker.convertToDTable(),"DTable");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnSelectDataFile.setOnAction(event -> {
            try {
                selectFile(FileselectFile,fileNameData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnConvertToOA.setOnAction(event -> {
            dataWorker= new DataWorker();
            try {
                List<String> data=dataWorker.txtParse(selectFile.getPath());
                dataWorker.codedAttribute(data);
                List<String> OAtable=dataWorker.convertToOATable(data,dataWorker.attributeSet);
                writter(OAtable,"OATable");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}