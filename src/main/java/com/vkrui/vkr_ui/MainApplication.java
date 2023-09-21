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
        long l=8;
        char[] c=Long.toBinaryString(l).toCharArray();
        for(char q: c){
            if (q=='1') System.out.print(" !"+q+"! ");
            else System.out.print(" -"+q+"- ");
        }
        System.out.print("\n");
        int[] q=new int[]{0,1,1,1};
        long t=0;
        for(int i=0;i<q.length;i++){
            if(q[i]==1) t+= (long)Math.pow(2,q.length-i-1);
        }
        System.out.print("\nRESULT:"+t);
    }
    public static void main(String[] args) {
        launch();
    }
}