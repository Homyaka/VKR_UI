package com.vkrui.vkr_ui;

import Mysystem.Problem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main_3.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Application");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        boolean[] a1=new boolean[4];
        boolean[] a2=new boolean[4];
        a1[0]=false;
        a1[1]=true;
        a1[2]=true;
        a1[3]=false;
        a2[0]=true;
        a2[1]=true;
        a2[2]=true;
        a2[3]=false;
        Problem p=new Problem("q",2);
        //p.vectorMultiply(a1,a2);
        File testfile=new File("D:\\OA_test.txt");
        p.generateBoolVectorsList(Collections.singletonList("test"),testfile);
        System.out.print('\n');
        Boolean[] res=p.vectorMultiply(p.listBoolVectors.get(0),p.listBoolVectors.get(1));
        System.out.print('\n');
        //System.out.print(p.checkVectors(a1,a2));
    }
    public static void main(String[] args) {
        launch();
    }
}