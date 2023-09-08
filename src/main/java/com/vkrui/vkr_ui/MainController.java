package com.vkrui.vkr_ui;

import Mysystem.Constraintchoco;
import Mysystem.Solution;
import Mysystem.SolvedVariable;
import Mysystem.Value;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.chocosolver.solver.constraints.nary.nValue.amnv.mis.F;
import workWithFiles.DataWorker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Label textSolutions;
    List<Solution> solutionsContainsAtt;
    List<Solution> solutionsNoOneAtt;
    String fullSolutions;
    String cutOneAttSolutions;
    String cutContainAtt;
    List<String> data;
    File selectFile;
    File fileAttributesDict;
    Constraintchoco cc=new Constraintchoco();
    boolean flagCutOneAtt=false;
    boolean flagContainAtt=false;
    ArrayList<Integer> weights;
    public DataWorker dataWorker=new DataWorker();
    public File OAFile=new File("");
    public File DFile=new File("");

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
    private AnchorPane scrollpaneSol;

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
    private Button OAbtnShowDTable;
    @FXML
    private Label OAlabelCreateDFile;

    @FXML
    private ScrollPane OApane;

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
    private Pane paneContainAtt;

    @FXML
    private AnchorPane paneExtraConstr;

    @FXML
    private SplitPane scrollpaneSol_1;
    @FXML
    private Pane paneOneAtt;
    @FXML
    private ScrollPane dataPane;
    @FXML
    private ScrollPane paneSolutions;
    @FXML
    private ScrollPane scrollpaneExtraConstr;
    @FXML
    private TextField tfFrequency;
    @FXML
    private Button btnCodedAtt;
    @FXML
    private Button btnConvertToOA;
    @FXML
    private Button btnSelectDataFile;
    @FXML
    private Button btn_OneAtt;
    @FXML
    private Label fileNameData;

    @FXML
    private Label labelBuildOAResult;
    @FXML
    private Label DlabelAttributesFile;
    @FXML
    private Label labelCodedAtt;
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
        FileWriter fileWriter;
       fileWriter = new FileWriter(outfile);
        if (type.equals("CodedAtt")) fileWriter.write(" код 1: ");
        fileWriter.write(data.get(0));
        weights=new ArrayList<>(dataWorker.weight);
        for (int i = 1; i < data.size(); i++) {
            if (type.equals("CodedAtt")) fileWriter.write('\n'+" код "+(i+1)+": "+data.get(i));
           if(type.equals("OATable")) fileWriter.write("\n" + data.get(i)+"\t"+dataWorker.weight.get(i-1));
           if(type.equals("DTable")) fileWriter.write("\n" + data.get(i));
        }
        //if (type.equals("CodedAtt")) fileWriter.write(data.size());
        fileWriter.close();
        if (type.equals("DTable")) {
            OAlabelCreateDFile.setText("Создан файл: "+outfile.getPath());
            DFile=outfile;
        }
        if (type.equals("OATable")){
            labelBuildOAResult.setText("Cоздан файл: "+ outfile.getPath());
        }
        if (type.equals("CodedAtt")) {
            labelCodedAtt.setText("Cоздан файл: "+ outfile.getPath());
            fileAttributesDict=outfile;
        }
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
    void initialize() {
        OAbtnShowDTable.setVisible(false);
        DbtnSelectFile.setOnAction(event -> {
            try {
                selectFile(DlabelSelect, DlabelFileName);
                System.out.println(selectFile.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        DbtnStart.setOnAction(event -> {
            try {
                if (selectFile.isFile() || !tfFrequency.getText().isEmpty()) {
                    textSolutions = new Label();
                    textSolutions.setFont(font);
                    solutions = cc.findSolutions(selectFile.getPath(), Integer.parseInt(tfFrequency.getText()));
                    fullSolutions = solutionsToStr(solutions);
                    textSolutions.setText(solutionsToStr(solutions));
                    paneSolutions.setContent(textSolutions);
                    paneSolutions.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    paneSolutions.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    flagContainAtt = false;
                    flagCutOneAtt = false;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        OAbtnSelectFile.setOnAction(event -> {
            try {
                selectFile(OAselectFile, OAFileName);
                OAFile=selectFile;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        OAbtnBuildDTable.setOnAction(event -> {
            dataWorker = new DataWorker();
            try {
                data = dataWorker.txtParse(selectFile.getPath());
                dataWorker.generatematrix(data);
                writter(dataWorker.convertToDTable(), "DTable");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            OAbtnShowDTable.setVisible(true);
        });
        btnSelectDataFile.setOnAction(event -> {
            try {
                selectFile(FileselectFile, fileNameData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnConvertToOA.setOnAction(event -> {
            dataWorker = new DataWorker();
            try {
                data = dataWorker.txtParse(selectFile.getPath());
                dataWorker.codedAttribute(data);
                List<String> OAtable = dataWorker.convertToOATable(data, dataWorker.attributeSet);
                writter(OAtable, "OATable");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnCodedAtt.setOnAction(event -> {
            if (selectFile.isFile()) {
                dataWorker.codedAttribute(data);
                String text="";
                try {
                    writter(dataWorker.attributeSet, "CodedAtt");
                    if(fileAttributesDict.isFile()){
                        List<String> data=dataWorker.txtParse(fileAttributesDict.getPath());
                        for(String str: data)
                            text+=str+"\n";
                    }
                    Label label=new Label();
                    label.setText(text);
                    label.setFont(font);
                    dataPane.setContent(label);
                    dataPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    dataPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn_ContainAtt.setOnAction(event -> {
            if (!tf_OneAtt.getText().isEmpty()) {
                int att = Integer.parseInt(tf_OneAtt.getText());
                solutionsContainsAtt = new ArrayList<>();
                for (Solution solution : solutions) {
                    List<Integer> values = solution.getSolution().get(1).getValues();
                    if (values.contains(att)) solutionsContainsAtt.add(solution);
                }
                textSolutions = new Label();
                textSolutions.setFont(font);
                cutContainAtt = solutionsToStr(solutionsContainsAtt);
                textSolutions.setText(solutionsToStr(solutionsContainsAtt));
                paneSolutions.setContent(textSolutions);
                flagContainAtt = true;
            }
        });
        btn_OneAtt.setOnAction(event -> {
                    if (!flagCutOneAtt) {
                        List<Solution> currentSolutions;
                        if (flagContainAtt) currentSolutions = solutionsContainsAtt;
                        else currentSolutions = solutions;
                        solutionsNoOneAtt = new ArrayList<>();
                        for (Solution solution : currentSolutions) {
                            SolvedVariable Y = solution.getSolution().get(1);
                            if (Y.GetValuesNum() != 1) solutionsNoOneAtt.add(solution);
                        }
                        textSolutions.setText(solutionsToStr(solutionsNoOneAtt));
                        textSolutions.setFont(font);
                        paneSolutions.setContent(textSolutions);
                        flagCutOneAtt = true;
                    } else if (flagContainAtt) {
                        textSolutions.setText(cutContainAtt);
                        textSolutions.setFont(font);
                        paneSolutions.setContent(textSolutions);
                        flagCutOneAtt = false;
                    } else {
                        textSolutions = new Label();
                        textSolutions.setText(fullSolutions);
                        textSolutions.setFont(font);
                        paneSolutions.setContent(textSolutions);
                        flagCutOneAtt = false;
                    }
                });
        DbtnShowAttributes.setOnAction(event -> {
            String text="";
            fileAttributesDict=new File("");
            if(!fileAttributesDict.isFile()||fileAttributesDict==null){
                Label temp=new Label();
                try {
                    selectFile(temp,DlabelAttributesFile);
                    fileAttributesDict=selectFile;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(fileAttributesDict.isFile()){
            try {
                List<String> data=dataWorker.txtParse(fileAttributesDict.getPath());
                for(String line: data)
                    text+=line+'\n';
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Label attDict= new Label();
            attDict.setFont(font);
            attDict.setText(text);
            DpaneShowAttributes.setContent(attDict);
            DpaneShowAttributes.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            DpaneShowAttributes.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        }
        });
        OAbtnShowAtt.setOnAction(event -> {
            String text="";
            fileAttributesDict=new File("");
            if(!fileAttributesDict.isFile()||fileAttributesDict==null){
                Label temp=new Label();
                try {
                    selectFile(temp,temp);
                    fileAttributesDict=selectFile;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(fileAttributesDict.isFile()) {
                try {
                    List<String> data = dataWorker.txtParse(fileAttributesDict.getPath());
                    for (String line : data)
                        text += line + '\n';
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Label attDict = new Label();
                attDict.setFont(font);
                attDict.setText(text);
                OApaneShowAtt.setContent(attDict);
                OApaneShowAtt.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                OApaneShowAtt.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            }
        });
        OAbtnLookTable.setOnAction(event -> {
            Label OApaneText=new Label();
            if(!OAFile.isFile()) {
                OApaneText.setText("Файл не выбран");
                OApaneText.setTextFill(Color.RED);
            }
            if(OAFile.isFile()){
                try {
                    List<String> data= dataWorker.txtParse(OAFile.getPath());
                    String text="";
                    for(String str: data)
                        text+=str+'\n';
                    OApaneText.setText(text);
                } catch (IOException e) {
                    OApaneText.setText("Некорректные данные");
                    OApaneText.setTextFill(Color.RED);
                    throw new RuntimeException(e);
                }
            }
            OApane.setContent(OApaneText);
            OApane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            OApane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        });
        OAbtnShowDTable.setOnAction(event -> {
            if(DFile.isFile()){
                try {
                    System.out.println(DFile.getPath());
                    List<String> data=dataWorker.txtParse(DFile.getPath());
                    String text="";
                    for(String str:data)
                        text+=str+'\n';
                    Label textlabel=new Label();
                    textlabel.setText(text);
                    OApane.setContent(textlabel);
                    OApane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                    OApane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}