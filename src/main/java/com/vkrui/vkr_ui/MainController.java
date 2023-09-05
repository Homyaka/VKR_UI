package com.vkrui.vkr_ui;

import Mysystem.Constraintchoco;
import Mysystem.Solution;
import Mysystem.Value;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import workWithFiles.DataWorker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Label textSolutions;
    List<String> data;
    File selectFile;
    Constraintchoco cc=new Constraintchoco();
    ArrayList<Integer> weights;
    public DataWorker dataWorker;

    @FXML
    private Button DbtnSelectFile;

    @FXML
    private Button DbtnShowAttributes;

    @FXML
    private Button DbtnShowWeights;

    @FXML
    private Button DbtnStart;

    @FXML
    private Label DlabelFileName;

    @FXML
    private Label DlabelSelect;

    @FXML
    private ScrollPane DpaneShowAttributes;

    @FXML
    private ScrollPane DpaneShowWeight;

    @FXML
    private Label FileselectFile;

    @FXML
    private Label OAFileName;

    @FXML
    private Button OAbtnBuildDTable;

    @FXML
    private Button OAbtnLookTable;

    @FXML
    private Button OAbtnSelectFile;

    @FXML
    private Button OAbtnShowAtt;

    @FXML
    private Button OAbtnShowWeights;

    @FXML
    private Label OAlabelCreateDFile;

    @FXML
    private Pane OApane;

    @FXML
    private ScrollPane OApaneShowAtt;

    @FXML
    private ScrollPane OApaneShowWeights;

    @FXML
    private Label OAselectFile;

    @FXML
    private TextField tf_OneAtt;

    @FXML
    private Button btn_ContainAtt;

    @FXML
    private CheckBox cb_OneAtt;

    @FXML
    private Button btnCodedAtt;

    @FXML
    private Button btnConvertToOA;

    @FXML
    private Button btnSelectDataFile;

    @FXML
    private Label fileNameData;

    @FXML
    private Label labelBuildOAResult;

    @FXML
    private Label labelCodedAtt;

    @FXML
    private ScrollPane paneSolutions;

    @FXML
    private TextField tfFrequency;

    private List<Solution> solutions;

    public String solutionsToStr(List<Solution> solutions){
        String res ="Найдено "+solutions.size()+" решений"+'\n';
        for(Solution sol: solutions)
            res+=sol.solutiontoString(false)+'\n';
        System.out.println(res);
        return res;
    }
    public  Font font= new Font("Times New Roman", 24);

    public void writter(List<String> data,String type) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM_dd_HH_mm");
        LocalDateTime now = LocalDateTime.now();
        File outfile = new File("D:\\" + type + "_" + dtf.format(now) + ".txt");
        FileWriter fileWriter = new FileWriter(outfile);
        fileWriter.write(data.get(0));
        weights=new ArrayList<>(dataWorker.weight);
        for (int i = 1; i < data.size(); i++) {
            if (type.equals("CodedAtt")) fileWriter.write(" -m"+i);
           if(type.equals("OATable")) fileWriter.write("\n" + data.get(i)+"\t"+dataWorker.weight.get(i-1));
           if(type.equals("DTable")) fileWriter.write("\n" + data.get(i));
        }
        if (type.equals("CodedAtt")) fileWriter.write(data.size());
        fileWriter.close();
        if (type.equals("DTable")) OAlabelCreateDFile.setText("Создан файл: "+outfile.getPath());
        if (type.equals("OATable")) labelBuildOAResult.setText("Cоздан файл: "+ outfile.getPath());
        if (type.equals("CodedAtt")) labelCodedAtt.setText("Cоздан файл: "+ outfile.getPath());
    }
    public void selectFile(Label labelSelect,Label fileName) throws IOException {
        Stage stage=new Stage();
        FileChooser fileChooser=new FileChooser();
        selectFile=fileChooser.showOpenDialog(stage);
        System.out.println(selectFile.getPath());
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
                System.out.println(selectFile.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        DbtnStart.setOnAction(event -> {
            try {
                if (selectFile.isFile()|| !tfFrequency.getText().isEmpty()) {
                    textSolutions=new Label();
                    textSolutions.setFont(font);
                    solutions=cc.findSolutions(selectFile.getPath(),Integer.parseInt(tfFrequency.getText()));
                    textSolutions.setText(solutionsToStr(solutions));
                    paneSolutions.setContent(textSolutions);
                    paneSolutions.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    paneSolutions.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                }
            } catch (IOException e) {
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
                data=dataWorker.txtParse(selectFile.getPath());
                dataWorker.codedAttribute(data);
                List<String> OAtable=dataWorker.convertToOATable(data,dataWorker.attributeSet);
                writter(OAtable,"OATable");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnCodedAtt.setOnAction(event -> {
            if(selectFile.isFile()) {
                dataWorker.codedAttribute(data);
                try {
                    writter(dataWorker.attributeSet, "CodedAtt");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn_ContainAtt.setOnAction(event-> {
            if (!tf_OneAtt.getText().isEmpty()){
                String att=tf_OneAtt.getText();
                for(Solution solution: solutions){
                    if (solution.getSolution().get(1)==)
                }
            }
        });
    }
}