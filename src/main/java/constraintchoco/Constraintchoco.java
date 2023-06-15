/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constraintchoco;

import Mysystem.Problem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.exception.ContradictionException;

/**
 *
 * @author User
 */
public class Constraintchoco {
    private static final String PATH = "E:\\benchmarks";
    static void logging(PrintWriter fout, String mes){
    fout.append(mes+"\n");
    System.out.println(mes);
    }
    
    static List <List<String>> loadsys(String filepath) throws FileNotFoundException, IOException{
            List<String> lines = Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
            List <List<String>> problem = new ArrayList<>();
            String tmp = lines.get(0);
            int ii=0;
            while (tmp.contains(" ")){
                problem.add(new ArrayList<>());
                problem.get(ii).add(tmp.substring(0, tmp.indexOf(" ")));
                ii++;
                tmp=tmp.substring(tmp.indexOf(" ")+1);
            }
            problem.add(new ArrayList<>());
            problem.get(ii).add(tmp);
            
            for (int i=1; i<lines.size();i++){
                tmp = lines.get(i);
                for(int j=0; j<ii; j++){
                    problem.get(j).add(tmp.substring(0, tmp.indexOf(" ")));
                    tmp=tmp.substring(tmp.indexOf(" ")+1);
                }
            problem.get(ii).add(tmp);
            }
    return problem;
    }


    private static void spill(){
        Spill sp = new Spill();
        sp.prepare();
        sp.solve(0);
    }
    
    public static void main(String[] args) throws IOException, ContradictionException {
        Problem problem = new Problem("test",2);
        String dir="c:\\Users\\polin\\IdeaProjects\\VKR\\src\\dataFile\\";
        problem.addsys("test",loadsys(dir+"test1.txt"));
        /*String dir = "e:\\systems\\10\\";
        qq.addsys("XY",loadsys(dir+"XY.txt"), true);
        qq.addsys("YZ",loadsys(dir+"YZ.txt"), true);
        qq.addsys("WZ",loadsys(dir+"WZ.txt"), true);
        qq.addsys("LW",loadsys(dir+"LW.txt"), true);
        qq.simplesolve(true);
        System.out.println(qq.showsolutions(true, false));*/
        /*Spilltest sptst= new Spilltest(7);
        sptst.solve();
        */
        //nq.buildqueen(12);
        //qq.solvemanymin(true);
        problem.solvemany(true);
        System.out.println(problem.showsolutions(false,false));
        //Autoshop tst=new Autoshop();
        //tst.solve(false,2);
        //0-пессимистичный
        //1-оптимистичный
        //2-общий
        //bench(29);
         //spill();

    }
}
/*4	2	
5	10	
6	4	
7	40	
8	92	
9	352	
10	724	
11	2 680	
12	14 200	
13	73 712	
14	365 596	
15	2 279 184	
16	14 772 512	
17	95 815 104	
18	666 090 624*/	
