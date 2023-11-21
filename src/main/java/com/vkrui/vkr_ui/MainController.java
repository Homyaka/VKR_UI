package com.vkrui.vkr_ui;

import Mysystem.*;
import com.sun.javafx.scene.control.SelectedItemsReadOnlyObservableList;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.chocosolver.solver.constraints.nary.nValue.amnv.differences.D;
import workWithFiles.DataWorker;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainController {
    //                                                  ПОЛЯ КЛАССА

    //Пользовательские переменные
    private List<Solution> solutions; //список решений
    @FXML
    private Label textSolutions; //лейбл с решениями
    String fullSolutions; // строка с решениями
    List<String> data; // данные (списком строк)
    File selectFile=new File(""); //выбранный файл
    File fileAttributesDict; //файл словаря аттрибутов
    Constraintchoco cc=new Constraintchoco(); // объект класса Constraintchoco
    ArrayList<Integer> weights; //список весов объектов
    HashMap<String,Integer> attributeType; //словарь типов аттрибутов
    public  Font font= new Font("Times New Roman", 24); //шрифт
    public DataWorker dataWorker=new DataWorker(); // объект класса DataWorker
    public File OAFile=new File(""); // файл с D-таблицей
    public File DFile=new File(""); //файл с ОА таблицей


    // вкладка D-таблиц
    @FXML
    private CheckBox DcbOnlyWeigth; // чекбокс на "показывать только вес"
    @FXML
    private ScrollPane paneSolutions;// панель с найденными решениями
    @FXML
    private CheckBox DcbUseFCodedFile; // чекбокс на "использовать файл кодировок"
    @FXML
    private ChoiceBox<String> DchoiceboxAT; // выпадающий список на выбор типа аттрибута
    @FXML
    private Button DbtnSelectFile; // кнопка выбора файла с D-таблицей
    @FXML
    private Button DbtnShowAttributes; // кнопка выбора файла с кодировкой аттрибутов
    @FXML
    private Button DbtnStart; //кнопка запуска поиска
    @FXML
    private Label DlabelFileName; // лейбл пути выбранного файла с D-таблицей
    @FXML
    private Label DlabelSelect; //лейбл что файл выбран
    @FXML
    private ScrollPane DpaneShowAttributes; // панель для отображения файла кодировок
    @FXML
    private Label DextraFilename; //лейбл путь до файла с кодировкой
    @FXML
    private Label DextraSelect; //лейбл что выбран файл кодировки
    @FXML
    private Button DbtnCodedFile; // кнопка выбора файла с кодировками для доп ограничения
    @FXML
    private Pane DextraWindow; //панель доп.ограничения с кодировками аттрибутов
    @FXML
    private TextField DtfContainAtt; // Текстовое поле ограничения "показать решения содержащие аттрибут"
    @FXML
    private TextField DtfNOcontainAtt; // Текстовое поле ограничения "показать решения НЕ содержащие аттрибут"
    @FXML
    private TextField DtfPatternLength; // Текстовое поле ограничения "длина паттерна"
    @FXML
    private TextField DtfSubPattern; // Текстовое поле ограничения "подраттерн"
    @FXML
    private TextField DtfSuperPattern; // Текстовое поле ограничения "суперпаттерн"
    @FXML
    private TextField DtfFrequency; // Текстовое поле частоты встречаемости


    // вкладка OA-таблиц
    @FXML
    private Label OAFileName; // лейбл путь до файла с OA таблицей
    @FXML
    private Button OAbtnBuildDTable; // кнопка чтоб построить D-таблицу
    @FXML
    private Button OAbtnLookTable; // кнопка просмотра ОА-таблицы
    @FXML
    private Button OAbtnSelectFile; //кнопка выбор файла с ОА таблицей
    @FXML
    private Button OAbtnShowAtt; //кнопка выбора файла с кодировками аттрибутов
    @FXML
    private Button OAbtnShowDTable; // кнопка показа сгенерированного файла с D-таблицей
    @FXML
    private Label OAlabelCreateDFile; // лейбл пути сгенерированного файла D-таблицы
    @FXML
    private ScrollPane OApane; //панель для показа файлов с ОА- и D- таблицей
    @FXML
    private ScrollPane OApaneShowAtt; //панель показа словаря закодированных аттрибутов
    @FXML
    private Label OAselectFile; //лейбл о том выбран ли файл ОА-таблицы


    //вкладка с файлом БД
    @FXML
    private Button btnCodedAtt;  // кнопка создания файла кодировок из БД
    @FXML
    private Button btnConvertToOA; //кнопка конвертирования БД в ОА и создания соотв. файла
    @FXML
    private Button btnSelectDataFile; // кнопка выбора файла БД
    @FXML
    private Label fileNameData; //лейбл путь выбранного файла БД
    @FXML
    private Label labelBuildOAResult; // лейбл о создании файла с ОА таблицей
    @FXML
    private Label DlabelAttributesFile; // лебйл с путем выбранного файла показа словаря закодированных аттрибутов
    @FXML
    private Label labelCodedAtt; // лейбл с путем до созданного файла кодировок
    @FXML
    private Label FileselectFile; // лейбл что выбран файл с БД
    @FXML
    private ScrollPane dataPane;

    //                                                  МЕТОДЫ КЛАССА

    // метод генерирующий из списка решений solutions текст для вывода пользователю
    public String solutionsToStr(List<Solution> solutions){
        String s;
        if(solutions.size()%10==1) s=" решение ";
        else {
            if(solutions.size()%10>1&& solutions.size()%10<5)s=" решения ";
            else s=" решений ";
        }
        String res ="Найдено "+solutions.size()+s+'\n'+'\n';
        for(int i=0;i<solutions.size();i++){
            List<Integer> varX=solutions.get(i).getSolution().get(0).getValues();
            if (DcbOnlyWeigth.isSelected())
                res+=i+1+". "+ "Суммарный вес объектов: "+cc.problem.calcObjectWeight(varX)+'\n';
            else
                res+=i+1+". "+ solutions.get(i).getSolution().get(0).toString(false)+" вес: "+cc.problem.calcObjectWeight(varX)+'\n';
            res+=solutions.get(i).getSolution().get(1).toString(false)+"\n\n";
        }
        res+="Время поиска: "+cc.time+" ms";
        return res;
    }

    //метод создания файла с содержимым data
    public void writter(List<String> data,String type) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM_dd_HH_mm");
        LocalDateTime now = LocalDateTime.now();
        File outfile = new File("D:\\files\\" + type + "_" + dtf.format(now) + ".txt");
        FileWriter fileWriter;
        fileWriter = new FileWriter(outfile);
        if (type.equals("CodedAtt")) {
            String columnAtt="";
            for(String s:attributeType.keySet()){
                columnAtt += s + "-"+attributeType.get(s)+"\t";
            }
            fileWriter.write(columnAtt+"\n");
            fileWriter.write(" код 1: ");
        }
        fileWriter.write(data.get(0));
        weights=new ArrayList<>(dataWorker.weight);
        for (int i = 1; i < data.size(); i++) {
            switch (type){
                case ("CodedAtt"):
                    fileWriter.write('\n'+" код "+(i+1)+": "+data.get(i));
                    break;
                case ("OATable"):
                    fileWriter.write("\n" + data.get(i)+"\t"+dataWorker.weight.get(i-1));
                    break;
                case ("DTable"):
                    fileWriter.write("\n" + data.get(i));
                    break;
            }
        }
        fileWriter.close();
        switch (type){
            case ("DTable"):
                OAlabelCreateDFile.setText("Создан файл: "+outfile.getPath());
                DFile=outfile;
                break;
            case ("OATable"):
                labelBuildOAResult.setText("Cоздан файл: "+ outfile.getPath());
                break;
            case ("CodedAtt"):
                labelCodedAtt.setText("Cоздан файл: "+ outfile.getPath());
                fileAttributesDict=outfile;
                break;
            default:
                System.out.print(type);
                break;
        }
    }

   // метод для выбора файла и записи его в selectFile (также заполняются соответствующие лейблы)
    public void selectFile(Label labelSelect,Label fileName) throws IOException {
        Stage stage=new Stage();
        FileChooser fileChooser=new FileChooser();
        selectFile=fileChooser.showOpenDialog(stage);
        if(selectFile==null){
            JOptionPane.showMessageDialog(null,"Файл не был выбран!","Ошибка",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if (selectFile.isFile()){
            fileName.setText(selectFile.getPath());
            labelSelect.setText("Выбран:");
        }
    }

    //метод который заполняет ChoiceBox аттрибутами списка att
    public void fillChoiceBox(List<String> att){
        ObservableList<String> listAtt= FXCollections.observableArrayList(att);
        DchoiceboxAT.setItems(listAtt);
    }

    // метод заполнения подпаттерна
    public ArrayList<Integer> fillSupPattern(){
        ArrayList<Integer> subPattern=null;
        if(!DtfSubPattern.getText().isEmpty()){
            subPattern=new ArrayList<>();
            String[] values= DtfSubPattern.getText().split(",");
            for(String s:values)
                subPattern.add(Integer.parseInt(s));
        }
        return subPattern;
    }

    // метод заполнения суперпаттерна
    public ArrayList<Integer> fillSuperPattern() {
        ArrayList<Integer> superPattern = null;
        if (!DtfSuperPattern.getText().isEmpty()) {
            String[] values = DtfSuperPattern.getText().split(",");
            superPattern = new ArrayList<>();
            for (String s : values)
                superPattern.add(Integer.parseInt(s));
        }
        return superPattern;
    }

    @FXML
    void initialize() {
        DextraWindow.setVisible(false);
        DchoiceboxAT.setVisible(false);
        DcbUseFCodedFile.setOnAction(event -> {
            DextraWindow.setVisible(DcbUseFCodedFile.isSelected());
        });
        //события для выбора файла
        DbtnSelectFile.setOnAction(event -> {
            try {
                selectFile(DlabelSelect, DlabelFileName);
                if (selectFile!=null)
                    DFile=selectFile;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        OAbtnSelectFile.setOnAction(event -> {
            try {
                selectFile(OAselectFile, OAFileName);
                if (selectFile!=null)
                    OAFile=selectFile;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnSelectDataFile.setOnAction(event -> {
            try {
                selectFile(FileselectFile, fileNameData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Вкладка D-таблиц
        DbtnStart.setOnAction(event -> {
            try {
                if(!DFile.isFile()||DFile==null)
                    JOptionPane.showMessageDialog(null,"Выберите файл!","Ошибка",JOptionPane.ERROR_MESSAGE);
                if(DtfFrequency.getText().isEmpty())
                    JOptionPane.showMessageDialog(null,"Введите частоту встречаемости!","Ошибка",JOptionPane.ERROR_MESSAGE);
                if (DFile.isFile() && !DtfFrequency.getText().isEmpty()) {
                    int numContainAtt=-1;
                    int numNoContainAtt=-1;
                    int numLengthAtt=-1;
                    ArrayList<Integer> subPattern = fillSupPattern();
                    ArrayList<Integer> superPattern= fillSuperPattern();
                    if(!DtfContainAtt.getText().isEmpty()) numContainAtt=Integer.parseInt(DtfContainAtt.getText());
                    if(!DtfNOcontainAtt.getText().isEmpty()) numNoContainAtt=Integer.parseInt(DtfNOcontainAtt.getText());
                    if(!DtfPatternLength.getText().isEmpty()) numLengthAtt=Integer.parseInt(DtfPatternLength.getText());
                    textSolutions = new Label();
                    textSolutions.setFont(font);
                    if(numContainAtt==-1 && numNoContainAtt==-1 && numLengthAtt==-1 && subPattern==null && superPattern==null)
                        solutions=cc.findSolutions(DFile.getPath(),Integer.parseInt(DtfFrequency.getText()));
                    else
                        solutions=cc.findSolutionsWithConstrain(DFile.getPath(),Integer.parseInt(DtfFrequency.getText()),numContainAtt,numNoContainAtt,numLengthAtt,subPattern,superPattern);
                    fullSolutions = solutionsToStr(solutions);
                    textSolutions.setText(solutionsToStr(solutions));
                    textSolutions.setTextFill(Color.BLACK);
                    paneSolutions.setContent(textSolutions);
                    paneSolutions.setPrefWidth(700);
                    paneSolutions.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    paneSolutions.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DextraWindow.setVisible(true);
            if (fileAttributesDict.isFile())
                DextraFilename.setText("Выбран файл: "+fileAttributesDict.getPath());
            else
                DextraFilename.setText("Файл не выбран");
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
        DbtnCodedFile.setOnAction(event -> {
            try {
                selectFile(DextraSelect,DextraFilename);
                if (selectFile.isFile()){
                    DataWorker dw=new DataWorker();
                    //List<String> dw.txtParse(selectFile.getPath());
                    System.out.print(selectFile.getPath());
                    List<String> test= new ArrayList<>();
                    test.add("1");
                    test.add("2");
                    test.add("Q");
                    fillChoiceBox(test);
                    DchoiceboxAT.setVisible(true);
                }
            }catch (Exception e){
                throw new RuntimeException();
            }
        });
        // Вкладка ОА-таблиц
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
                JOptionPane.showMessageDialog(null, "Файл не выбран!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
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
            OApane.setContent(OApaneText);
            OApane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            OApane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        });
        OAbtnShowDTable.setOnAction(event -> {
            if(DFile.isFile()){
                try {
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
        //Вкладка первоначальных файлов
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
                attributeType=dataWorker.attributeType;
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
    }
}