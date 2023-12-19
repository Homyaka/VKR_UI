package GenDesign;

import GDSystem.*;
import GDSystem.Box;
import com.vkrui.vkr_ui.MainApplication;
import com.vkrui.vkr_ui.MainController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
    private GridPane gridPaneVars;

    @FXML
    private Label labelAddVar;

    @FXML
    private Label labelSolCount;

    @FXML
    private ListView<String> listViewVars;

    @FXML
    private Pane paneAddVar;

    @FXML
    private Pane paneSolutions;

    @FXML
    private Pane paneVars;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TextArea tfFillVar;

    @FXML
    private TextField tfHeight;

    @FXML
    private TextField tfVarName;

    @FXML
    private TextField tfWidth;
    private List<Variable> variables;
    public long time;
    private Grid grid;
    private MySolver solver;

    int widthGridPane=1;
    int heightGridPane=1;

    List<String> strings=new ArrayList<>();

    List<java.awt.Color> objectColor;
    BufferedImage objectImage;
    BufferedImage freeImage;
    BufferedImage emptyImage;


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
    public Grid createGrid(){
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
        String[] strs=name.split(" ");
        newVar.id=variables.size()+1;
        int[][] cells=new int[height][width];
        for(int y=0;y<height;y++){
            String[] lineContent=lines[y].split(" ");
            for(int x=0;x<width;x++){
                if(Integer.parseInt(lineContent[x])==2) cells[y][x]=1;
                else cells[y][x]=Integer.parseInt(lineContent[x])*10+newVar.id;
            }
        }
        newVar.obj=cells;
        System.out.println(newVar.name);
        System.out.print(Arrays.deepToString(newVar.obj));
        return newVar;
    }
    @FXML
    void initialize() {
        generateBufferedImages();
        fillListIObjColor();
        variables=new ArrayList<>();
        paneAddVar.setVisible(false);
        paneSolutions.setVisible(false);
        btnStart.setOnAction(event -> {
            grid=new Grid(createGrid());
            paneSolutions.setVisible(true);
            JOptionPane.showMessageDialog(null,"QWE");
            if(variables.size()==0) JOptionPane.showMessageDialog(null,"Нет объектов!","NO OBJECT",JOptionPane.ERROR_MESSAGE);
            else solve();
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
                variables.add(addVar(tfVarName.getText(),tfFillVar.getText()));
                labelAddVar.setText("Переменная "+tfVarName.getText()+" добавлена");
                strings.add(tfVarName.getText());
                ObservableList<String> obsList= FXCollections.observableArrayList(strings);
                listViewVars.setItems(obsList);
                showVarOnGridPane(variables.get(0));
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
