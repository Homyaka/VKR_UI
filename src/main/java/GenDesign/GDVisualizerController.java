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
    private List<Solution> solutions;

    public List<Grid> filledGrids;
    @FXML
    private ListView<String> listView;
    private Grid grid;

    private String wallImage="D:\\wall.jpg";
    private String object1Image="D:\\";
    private String emptyImage="D:\\empty.jpg";
    private String freeImage="D:\\free.jpg";

    public void initData(List<Solution> solutions,Grid grid){
        this.solutions=solutions;
        this.grid=grid;
        fillGridPane();
        List<String> strings=new ArrayList<>();
        for(int i=1;i<=solutions.size();i++){
            strings.add(i+" решение");
        }
        ObservableList<String> obsList= FXCollections.observableArrayList(strings);
        listView.setItems(obsList);
    }

    public void paint() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics g=image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1,1,98,98);
        try {
            ImageIO.write(image, "jpg", new File("D:\\empty.jpg"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Grid fillGrid(Solution solution){
        Grid resGrid=grid;
        for(int i=0;i<solution.variables.size();i++){
            Variable v=solution.variables.get(i);
            int point=solution.variables.get(i).intVar.getValue();
            for(int y=0;y<v.obj.length;y++){
                for(int x=0;x<v.obj[0].length;x++){
                    int p=point+y*resGrid.width+x;
                    System.out.println(v.name+" "+point+" point "+p);
                    int[] ps=resGrid.getPointCell(p);
                    resGrid.grid[ps[1]][ps[0]]= Cell.OBJECT;
                }
            }
        }
        return resGrid;
    }

    public void fillGridPane(){
        gridPane.setGridLinesVisible(true);
        for(int i=0;i<grid.width-1;i++)
            gridPane.getColumnConstraints().add(new ColumnConstraints(100));
        for(int i=0;i<grid.height-1;i++){
            gridPane.getRowConstraints().add(new RowConstraints(100));
        }
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
                    }
                }
            }
       }
    }
    @FXML
    void initialize() {
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
                System.out.println(solutions.get(Integer.parseInt(ss[0])-1));
                System.out.println(fillGrid(solutions.get(Integer.parseInt(ss[0])-1)));
            }
        });

        //System.out.print(Arrays.toString(splitPane.getDividerPositions()));
    }
}
