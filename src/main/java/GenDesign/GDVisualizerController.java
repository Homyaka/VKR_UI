package GenDesign;

import GDSystem.Cell;
import GDSystem.Grid;
import GDSystem.Solution;
import GDSystem.Variable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GDVisualizerController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane paneSolutions;
    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane scrollPane;
    private List<Solution> solutions;

    public List<Grid> filledGrids;
    @FXML
    private ListView<String> listView;
    private Grid grid;
    private int cellsSize=50;

    private BufferedImage wallImage;
    private BufferedImage objectImage;
    private BufferedImage emptyImage;
    private BufferedImage freeImage;

    private Color wallColor=Color.GRAY;
    private Color emptyColor=Color.WHITE;
    private Color freeColor=Color.WHITE;
    private List<Color> objectColor;

    public void generateBufferedImages(){
        wallImage=new BufferedImage(cellsSize,cellsSize,BufferedImage.TYPE_INT_RGB);
        objectImage=new BufferedImage(cellsSize,cellsSize,BufferedImage.TYPE_INT_RGB);
        freeImage=new BufferedImage(cellsSize,cellsSize,BufferedImage.TYPE_INT_RGB);
        emptyImage=new BufferedImage(cellsSize,cellsSize,BufferedImage.TYPE_INT_RGB);

        Graphics wallG =wallImage.createGraphics();
        Graphics emptyG=emptyImage.createGraphics();
        Graphics objG=objectImage.createGraphics();
        Graphics freeG=freeImage.createGraphics();

        wallG.setColor(wallColor);
        emptyG.setColor(emptyColor);
        freeG.setColor(freeColor);

        wallG.fillRect(1,1,cellsSize-2,cellsSize-2);
        emptyG.fillRect(1,1,cellsSize-2,cellsSize-2);
        freeG.fillRect(1,1,cellsSize-2,cellsSize-2);
        freeG.setColor(Color.BLACK);
        for(int i=0;i<cellsSize;i++){
            for(int j=0;j<cellsSize;j++)
                if(i==j) freeG.fillRect(i,j,1,1);
        }
    }
    public void fillListIObjColor(){
        objectColor=new ArrayList<>();
        objectColor.add(new Color(0, 128, 128));
        objectColor.add(new Color(0, 128, 0));
        objectColor.add(new Color(255, 160, 122));
        objectColor.add(new Color(189, 183, 107));
        objectColor.add(new Color(153, 50, 204));
    }

    public void initData(List<Solution> solutions,Grid grid){
       //paint();
        this.solutions=solutions;
        this.grid=grid;
        visualizeSols(fillGrid(solutions.get(0)));
        List<String> strings=new ArrayList<>();
        for(int i=1;i<=solutions.size();i++){
            strings.add(i+" решение");
        }
        ObservableList<String> obsList= FXCollections.observableArrayList(strings);
        listView.setItems(obsList);
    }

    public Grid fillGrid(Solution solution){
        Grid resGrid=new Grid(grid);
        for(int i=0;i<solution.variables.size();i++){
            Variable v=solution.variables.get(i);
            int point=solution.values.get(i);
            for(int y=0;y<v.obj.length;y++){
                for(int x=0;x<v.obj[0].length;x++){
                    int p=point+y*resGrid.width+x;
                    System.out.println(v.name+" "+point+" point "+p);
                    int[] ps=resGrid.getPointCell(p);
                    resGrid.grid[ps[1]][ps[0]]=v.obj[y][x];
                    System.out.print(v.name+" "+v.obj[y][x]);
                }
            }
        }
        System.out.println(Arrays.deepToString(resGrid.grid));
        return resGrid;
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

    public void visualizeSols(Grid solsGrid){
       int size =cellsSize;
        /*for(int i=0;i<grid.width;i++)
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        for(int i=0;i<grid.height;i++)
            gridPane.getRowConstraints().add(new RowConstraints(size));*/
        for(int x=0;x<solsGrid.width;x++){
            for(int y=0;y<solsGrid.height;y++){
                switch (solsGrid.grid[y][x]) {
                    case WALLS:{
                        ImageView imageView=new ImageView(convertToFxImage(wallImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case EMPTY:{
                        ImageView imageView=new ImageView(convertToFxImage(emptyImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case FREE:{
                        ImageView imageView=new ImageView(convertToFxImage(freeImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case OBJ1:{
                        Graphics objG=objectImage.createGraphics();
                        objG.setColor(objectColor.get(0));
                        objG.fillRect(1,1,cellsSize-2,cellsSize-2);
                        ImageView imageView=new ImageView(convertToFxImage(objectImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case OBJ2:{
                        Graphics objG=objectImage.createGraphics();
                        objG.setColor(objectColor.get(1));
                        objG.fillRect(1,1,cellsSize-2,cellsSize-2);
                        ImageView imageView=new ImageView(convertToFxImage(objectImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case OBJ3:{
                        Graphics objG=objectImage.createGraphics();
                        objG.setColor(objectColor.get(2));
                        objG.fillRect(1,1,cellsSize-2,cellsSize-2);
                        ImageView imageView=new ImageView(convertToFxImage(objectImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case OBJ4:{
                        Graphics objG=objectImage.createGraphics();
                        objG.setColor(objectColor.get(3));
                        objG.fillRect(1,1,cellsSize-2,cellsSize-2);
                        ImageView imageView=new ImageView(convertToFxImage(objectImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case OBJ5:{
                        Graphics objG=objectImage.createGraphics();
                        objG.setColor(objectColor.get(4));
                        objG.fillRect(1,1,cellsSize-2,cellsSize-2);
                        ImageView imageView=new ImageView(convertToFxImage(objectImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                }
            }
        }
        gridPane.getColumnConstraints().setAll(new ColumnConstraints(size));
        gridPane.getRowConstraints().setAll(new RowConstraints(size));
    }
    /*public void fillGridPane(){
        int size=100;
        if(grid.height==8) size=10;
        else size=100;
        gridPane.getRowConstraints().removeAll();
        gridPane.getColumnConstraints().removeAll();
        gridPane.getChildren().removeAll();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        for(int x=0;x<grid.width;x++){
            for(int y=0;y<grid.height;y++){
                switch (grid.grid[y][x]) {
                    case WALLS:{
                        ImageView imageView=new ImageView(wallImage);
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case EMPTY:{
                        ImageView imageView=new ImageView();
                        imageView.setImage(new Image(emptyImage));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                    case FREE:{
                        ImageView imageView=new ImageView();
                        imageView.setImage(new Image(freeImage));
                        break;
                    }
                    case OBJECT:{
                        ImageView imageView=new ImageView();
                        imageView.setImage(new Image(object1Image));
                        gridPane.add(imageView,x,y);
                        break;
                    }
                }
            }
       }
        for(int i=0;i<grid.width;i++)
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        for(int i=0;i<grid.height;i++)
            gridPane.getRowConstraints().add(new RowConstraints(size));
    }*/
    @FXML
    void initialize() {
        generateBufferedImages();
        fillListIObjColor();
        /*scrollPane.setContent(gridPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);*/
        paneSolutions.setMinWidth(300);
        SplitPane.setResizableWithParent(paneSolutions,false);
        //splitPane.setDividerPositions(0.0f);
        /*listView.setOnMouseClicked(mouseEvent -> {
            System.out.println(paneSolutions.getWidth());
            System.out.println(Arrays.toString(splitPane.getDividerPositions()));
        });*/
        MultipleSelectionModel<String> langsSelectionModel = listView.getSelectionModel();
        langsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.println(t1);
                String[] ss=t1.split(" ");
                int ind=Integer.parseInt(ss[0])-1;
                //System.out.println(solutions.get(Integer.parseInt(ss[0])-1));
                System.out.println(fillGrid(solutions.get(Integer.parseInt(ss[0])-1)));
                //fillGrid(solutions.get(ind));
                visualizeSols(fillGrid(solutions.get(ind)));
            }
        });

        //System.out.print(Arrays.toString(splitPane.getDividerPositions()));
    }
}
