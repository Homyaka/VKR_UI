package GenDesign;

import GDSystem.*;
import com.vkrui.vkr_ui.MainApplication;
import com.vkrui.vkr_ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenController {

    public Stage stage=new Stage();
    @FXML
    private Button btnShowSol;
    @FXML
    private Button btnStart;
    @FXML
    private Label labelSolCount;
    @FXML
    private Pane paneSolutions;
    @FXML
    private TextField tfWidth;
    @FXML
    private TextField tfHeight;
    private List<Variable> variables;
    private Grid grid;
    private MySolver solver;

    public List<Variable> createVars(){
        List<Variable> vs=new ArrayList<>();
        Variable var1=new GDSystem.Variable(new Box(2,2));
        Variable var2= new GDSystem.Variable(new Box(2,1));
        Variable var3=new GDSystem.Variable(new Box(3,1));
        var1.obj=new Cell[][]{{Cell.OBJECT,Cell.FREE},{Cell.OBJECT,Cell.OBJECT}};
        var2.obj= new Cell[][]{{Cell.OBJECT,Cell.OBJECT}};
        var3.obj= new Cell[][]{{Cell.OBJECT,Cell.FREE,Cell.OBJECT}};
        vs.add(var1);
        vs.add(var2);
        vs.add(var3);
        var1.name="var1";
        var2.name="var2";
        var3.name="var3";
        return vs;
    }
    public Grid createGrid(){
        Grid grid=new Grid(Integer.parseInt(tfWidth.getText()),Integer.parseInt(tfHeight.getText()));
        for(int i=0;i<grid.grid.length;i++)
            Arrays.fill(grid.grid[i],Cell.EMPTY);
        Arrays.fill(grid.grid[0],Cell.WALLS);
        Arrays.fill(grid.grid[grid.grid.length-1],Cell.WALLS);
        for(int i=1;i<grid.grid.length-1;i++){
            grid.grid[i][0]=Cell.WALLS;
            grid.grid[i][grid.grid[0].length-1]=Cell.WALLS;
        }
        return grid;
    }
    public void setMainWindow(){
        btnShowSol.getScene().getWindow().hide();
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
        solver.manySolve(true);
    }
    @FXML
    void initialize() {
        paneSolutions.setVisible(false);
        btnStart.setOnAction(event -> {
            grid=createGrid();
            variables=createVars();
            labelSolCount.setText("Пока не работает");
            labelSolCount.setTextFill(Color.RED);
            paneSolutions.setVisible(true);
            solve();
        });
        btnShowSol.setOnAction(event -> {
            setMainWindow();
        });
    }
}
