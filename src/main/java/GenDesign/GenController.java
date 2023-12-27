package GenDesign;

import GDSystem.*;
import GDSystem.Box;
import GDSystem.UnarConstaints.DistanceToWallConstraint;
import com.vkrui.vkr_ui.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenController {

    public Stage stage=new Stage();
    @FXML
    private Button btnAddVar;

    @FXML
    private Button btnNewVar;

    @FXML
    private Button btnShowSol;

    @FXML
    private Button btnStart;
    @FXML
    private Button btnReafFileGrid;

    @FXML
    private GridPane gridPaneVars;

    @FXML
    private Label labelAddVar;

    @FXML
    private Label labelSolCount;

    @FXML
    private Label labelFileNameGrid;

    @FXML
    private ListView<String> listViewVars;

    @FXML
    private Pane paneAddVar;

    @FXML
    private Pane paneSolutions;

    @FXML
    private Pane paneVars;

    @FXML
    private Pane paneWidthHeightGrid;
    @FXML
    private Pane paneExtraConstraints;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TextArea tfFillVar;

    @FXML
    private TextArea textAreaGridContent;

    @FXML
    private TextField tfHeight;

    @FXML
    private TextField tfVarName;

    @FXML
    private TextField tfWidth;
    @FXML
    private TextField tfDTW;
    @FXML
    private TextField tfConWithObj;
    @FXML
    private CheckBox checkBoxEasySetting;
    @FXML
    private CheckBox checkBoxExtraConstraints;
    @FXML
    private CheckBox checkBoxInCorner;
    @FXML
    private ChoiceBox<String> choiceBoxDTW;
    @FXML
    private ChoiceBox<String> choiceBoxConWithObj;

    private List<Variable> variables;
    public long time;
    private Grid grid;
    private MySolver solver;
    int widthGridPane=1;
    int heightGridPane=1;

    public File fileGridContent=new File("");

    List<String> strings=new ArrayList<>();

    List<java.awt.Color> objectColor;
    BufferedImage objectImage;
    BufferedImage freeImage;
    BufferedImage emptyImage;

    public void fillChoiceBox(){
        ObservableList<String> walls=FXCollections.observableArrayList("Любой","Сверху","Снизу","Справа","Слева");
        choiceBoxDTW.setItems(walls);
        ObservableList<String> connectWithObject=FXCollections.observableArrayList("Рядом","Напротив");
        choiceBoxConWithObj.setItems(connectWithObject);
    }
    public void generateBufferedImages(){
        emptyImage=new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
        freeImage=new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
        objectImage=new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
        Graphics freeG=freeImage.createGraphics();
        Graphics emptyG=emptyImage.createGraphics();
        freeG.fillRect(1,1,47,47);
        freeG.setColor(java.awt.Color.BLACK);
        for(int i=0;i<48;i++){
            for(int j=0;j<48;j++)
                if(i==j) freeG.fillRect(i,j,1,1);
        }
        emptyG.setColor(java.awt.Color.WHITE);
        emptyG.fillRect(0,0,50,50);
    }
    public void fillListIObjColor(){
        objectColor=new ArrayList<>();
        objectColor.add(new java.awt.Color(0, 128, 128));
        objectColor.add(new java.awt.Color(0, 128, 0));
        objectColor.add(new java.awt.Color(255, 160, 122));
        objectColor.add(new java.awt.Color(189, 183, 107));
        objectColor.add(new java.awt.Color(153, 50, 204));
    }
    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }
        return new ImageView(wr).getImage();
    }
    public void showVarOnGridPane(Variable v) {
        for(int i=0;i<widthGridPane;i++){
            for(int j=0;j<heightGridPane;j++){
                ImageView imageView=new ImageView(convertToFxImage(emptyImage));
                gridPaneVars.add(imageView,i,j);
            }
        }
        GridPane.clearConstraints(gridPaneVars);
        for (int y = 0; y < v.obj.length; y++) {
            for (int x = 0; x < v.obj[0].length; x++) {
                switch (v.obj[y][x]){
                    case 1:{
                        ImageView imageView=new ImageView(convertToFxImage(freeImage));
                        gridPaneVars.add(imageView,x,y);
                        break;
                    }
                    default:{
                        Graphics objG=objectImage.createGraphics();
                        objG.setColor(objectColor.get(v.id));
                        objG.fillRect(1,1,47,47);
                        ImageView imageView=new ImageView(convertToFxImage(objectImage));
                        gridPaneVars.add(imageView,x,y);
                        break;
                    }
                }

            }
        }
    }

    public List<String> txtParse(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        return lines;
    }
    public Grid createEasyGrid(){
        Grid grid=new Grid(Integer.parseInt(tfWidth.getText()),Integer.parseInt(tfHeight.getText()));
        for(int i=0;i<grid.grid.length;i++)
            Arrays.fill(grid.grid[i],0);
        Arrays.fill(grid.grid[0],-1);
        Arrays.fill(grid.grid[grid.grid.length-1],-1);
        for(int i=1;i<grid.grid.length-1;i++){
            grid.grid[i][0]=-1;
            grid.grid[i][grid.grid[0].length-1]=-1;
        }
        return grid;
    }
    public void setMainWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("GDVisualizer.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GDVisualizerController visualizerController= fxmlLoader.getController();
        visualizerController.initData(solver.solutions,grid);
        Parent root=fxmlLoader.getRoot();
        try {
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void solve(){
        solver=new MySolver(variables,grid);
        System.out.println(variables.get(0).intVar);
        time=solver.manySolve(true);
    }

    public Variable addVar(String name,String content){
        String[] lines=content.split("\n");
        int height=lines.length;
        String[] firstLine=lines[0].split(" ");
        int width=firstLine.length;
        Variable newVar=new Variable(new Box(width,height));
        newVar.name=name;
        newVar.id=variables.size()+1;
        int[][] cells=new int[height][width];
        for(int y=0;y<height;y++){
            String[] lineContent=lines[y].split(" ");
            if(lineContent.length!=width) return null;
            for(int x=0;x<width;x++){
                if(Integer.parseInt(lineContent[x])==0) cells[y][x]=1;
                else cells[y][x]=Integer.parseInt(lineContent[x])*10+newVar.id;
            }
        }
        newVar.obj=cells;
        System.out.println(newVar.name);
        System.out.print(Arrays.deepToString(newVar.obj));
        if (checkBoxExtraConstraints.isSelected()){
            if(checkBoxInCorner.isSelected()){
                JOptionPane.showMessageDialog(null,"ПОКА НЕ РЕАЛИЗОВАНО");
            }
            if (!tfDTW.getText().isEmpty()){
                newVar.unarConstraints=new ArrayList<>();
                switch (choiceBoxDTW.getValue()){
                    case "Любой":{
                        newVar.unarConstraints.add(new DistanceToWallConstraint(Integer.parseInt(tfDTW.getText())));
                        break;
                    }
                    case "Сверху":{
                        newVar.unarConstraints.add(new DistanceToWallConstraint(Direction.NORTH,Integer.parseInt(tfDTW.getText())));
                        break;
                    }
                    case "Снизу":{
                        newVar.unarConstraints.add(new DistanceToWallConstraint(Direction.SOUTH,Integer.parseInt(tfDTW.getText())));
                        break;
                    }
                    case "Справа":{
                        newVar.unarConstraints.add(new DistanceToWallConstraint(Direction.EAST,Integer.parseInt(tfDTW.getText())));
                        break;
                    }
                    case "Слева": {
                        newVar.unarConstraints.add(new DistanceToWallConstraint(Direction.WEST,Integer.parseInt(tfDTW.getText())));
                        break;
                    }
                }
            }
        }
        return newVar;
    }
    public Grid createGrid(){
        String[] lines=textAreaGridContent.getText().split("\n");
        int height=lines.length;
        String[] firstLine=lines[0].split(" ");
        int width=firstLine.length;
        Grid resGrid=new Grid(width,height);
        int[][] grid=new int[width][height];
        for(int y=0;y<height;y++){
            String[] line=lines[y].split(" ");
            if (line.length!=width) return null;
            for(int x=0;x<width;x++){
                int cell=Integer.parseInt(line[x]);
                switch (cell){
                   case 1:{
                       grid[y][x] = -1;
                       break;
                   }
                   case 2:{
                       grid[y][x]=2;
                       if(y!=0&&grid[y-1][x]!=-1) grid[y-1][x]=1;
                       if(y!=height-1&&grid[y+1][x]!=-1) grid[y+1][x]=1;
                       if(x!=0&&grid[y][x-1]!=-1) grid[y][x-1]=1;
                       if(x!=width-1&&grid[y][x+1]!=-1) grid[y][x+1]=1;
                       break;
                   }
                   default:{
                       if (grid[y][x]!=1) grid[y][x] = cell;
                       break;
                   }
               }
            }
        }
        resGrid.grid=grid;
        return resGrid;
    }
    @FXML
    void initialize() {
        fillChoiceBox();
        paneWidthHeightGrid.setVisible(false);
        generateBufferedImages();
        fillListIObjColor();
        variables=new ArrayList<>();
        paneAddVar.setVisible(false);
        paneSolutions.setVisible(false);
        paneExtraConstraints.setVisible(false);
        checkBoxEasySetting.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkBoxEasySetting.isSelected()) paneWidthHeightGrid.setVisible(true);
                if (!checkBoxEasySetting.isSelected()) paneWidthHeightGrid.setVisible(false);
            }
        });
        checkBoxExtraConstraints.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(checkBoxExtraConstraints.isSelected()) paneExtraConstraints.setVisible(true);
                if(!checkBoxExtraConstraints.isSelected()) paneExtraConstraints.setVisible(false);
            }
        });
        btnStart.setOnAction(event -> {
            paneSolutions.setVisible(true);
            if (checkBoxEasySetting.isSelected()){
                if (tfWidth.getText()==null||tfHeight.getText()==null)
                    JOptionPane.showMessageDialog(null,"Поле ширины или высоты пустое!","ERROR",JOptionPane.ERROR_MESSAGE);
                else
                    grid=new Grid(createEasyGrid());
            }
            else {
                    grid=new Grid(createGrid());
            }
            if(variables.size()==0) JOptionPane.showMessageDialog(null,"Нет объектов!","NO OBJECT",JOptionPane.ERROR_MESSAGE);
            else if(grid==null){
                JOptionPane.showMessageDialog(null,"Не удалось создать пространство!","FAILED CREATE GRID",JOptionPane.ERROR_MESSAGE);
            } else solve();
            int count=solver.solutions.size();
            labelSolCount.setTextFill(Color.BLACK);
            labelSolCount.setText("Найдено "+count+" решений за "+time+" ms.");
        });
        btnShowSol.setOnAction(event -> {
            setMainWindow();
        });
        btnNewVar.setOnAction(event -> {
            paneAddVar.setVisible(true);
            tfFillVar.setText(null);
            tfVarName.setText(null);
        });
        btnAddVar.setOnAction(event -> {
            if(tfVarName.getText()==null||tfFillVar.getText()==null){
                JOptionPane.showMessageDialog(null,"Заполните поля!","ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else {
                Variable v=(addVar(tfVarName.getText(),tfFillVar.getText()));
                if (v!=null) {
                    variables.add(v);
                    labelAddVar.setTextFill(Color.GREEN);
                    labelAddVar.setText("Переменная " + tfVarName.getText() + " добавлена");
                    strings.add(tfVarName.getText());
                    ObservableList<String> obsList = FXCollections.observableArrayList(strings);
                    listViewVars.setItems(obsList);
                    showVarOnGridPane(variables.get(0));
                }
                else {
                    JOptionPane.showMessageDialog(null,"Некоректное содержимое переменной!","ERROR",JOptionPane.ERROR_MESSAGE);
                    labelAddVar.setText("Не удалось добавить переменную "+tfVarName.getText()+"!");
                    labelAddVar.setTextFill(Color.RED);
                }
            }
        });
        btnReafFileGrid.setOnAction(event -> {
            Stage stage=new Stage();
            FileChooser fileChooser=new FileChooser();
            fileGridContent=fileChooser.showOpenDialog(stage);
            if(fileGridContent.isFile()){
                labelFileNameGrid.setText("Выбран файл: "+fileGridContent.getPath());
                List<String> dataGrid;
                String gridContent="";
                try {
                    dataGrid=txtParse(fileGridContent.getPath());
                    for(String s: dataGrid) gridContent+=s+"\n";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                textAreaGridContent.setText(gridContent);
            }
        });
        SplitPane.setResizableWithParent(paneVars,false);
        MultipleSelectionModel<String> langsSelectionModel = listViewVars.getSelectionModel();
        langsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                listViewVars.getSelectionModel().getSelectedIndices();
                String[] ss=t1.split(" ");
                int ind=listViewVars.getSelectionModel().getSelectedIndices().get(0);
                Variable v=variables.get(ind);
                if(heightGridPane<v.obj.length) heightGridPane=v.obj.length;
                if (widthGridPane<v.obj[0].length) widthGridPane=v.obj[0].length;
                showVarOnGridPane(v);
            }
        });
    }
}
