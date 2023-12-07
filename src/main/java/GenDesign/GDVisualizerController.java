package GenDesign;

import GDSystem.Grid;
import GDSystem.Solution;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.util.List;

public class GDVisualizerController {
    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane paneSolutions;

    private List<Solution> solutions;
    private Grid grid;

    private String wallImage="D:\\wall.jpg";
    private String objectImage="";

    public void initData(List<Solution> solutions,Grid grid){
        this.solutions=solutions;
        this.grid=grid;
        Label test=new Label();
        String s="COUNT SOLUTIONS: "+solutions.size();
        test.setText(s);
        List<Button> buttons=new ArrayList<>();
        for(int i=0;i<solutions.size();i++) {
            Button btn=new Button("Solution "+i);
            buttons.add(btn);
            //paneSolutions.setContent(btn);
        }
        fillGridPane();
        paint();
    }

    public void paint() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics g=image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,100,100);
        try {
            ImageIO.write(image, "jpg", new File("D:\\empty.jpg"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillGridPane(){
        gridPane.setGridLinesVisible(true);
        for(int i=0;i<grid.width-1;i++)
            gridPane.getColumnConstraints().add(new ColumnConstraints(100));
        for(int i=0;i<grid.height-1;i++){
            gridPane.getRowConstraints().add(new RowConstraints(100));
        }
    }
    @FXML
    void initialize() {

    }
}
