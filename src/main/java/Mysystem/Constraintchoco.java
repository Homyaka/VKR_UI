/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.exception.ContradictionException;

public class Constraintchoco {
    public List<Solution> filterSolutions;
    public Problem problem;
    public  long time;
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
    public String findInDSystem(String filePath,int minSup) throws  IOException,ContradictionException{
        Problem nq = new Problem("test",minSup);
        nq.addsys("test",loadsys(filePath));
        nq.solvemany(true);
        nq.removeWastePattern();
       // System.out.println(nq.removeWastePattern());
        String result="";
        return result;
    }
    public List<Solution> findSolutions(String filePath, int minSup) throws IOException {
        time= java.lang.System.currentTimeMillis();
        problem = new Problem("test",minSup);
        problem.addsys("test",loadsys(filePath));
        problem.generateListBoolVector();
        problem.genBoolVectors();
        problem.generateArrayLong();
        problem.fillArrayLong();
        problem.solvemany(true);
        // Через словарь
        //List<Solution> res=problem.removeWastePattern();
        // Через булевые вектора
        //List<Solution> res=problem.removeByBooleanVector();
        // через числовой вектор БЕЗ СЕТА
        List<Solution> res=problem.removeByVectorsNOSET();
        // Через чиловой вектор С СЕТОМ
        //List<Solution> res=problem.removeByVectorsWITHSET();
        time= java.lang.System.currentTimeMillis()-time;
        return res;
    }
    public List<Solution> findSolutionsWithConstrain(String filePath, int minSup,int containAtt,int noContainAtt) throws IOException {
        time= java.lang.System.currentTimeMillis();
        problem = new Problem("test",minSup,containAtt,noContainAtt);
        problem.addsys("test",loadsys(filePath));
        problem.generateListBoolVector();
        problem.genBoolVectors();
        problem.generateArrayLong();
        problem.fillArrayLong();
        problem.solvemany(true);
        // Через словарь
       // List<Solution> res=problem.removeWastePattern();
        // Через булевые вектора
        //List<Solution> res=problem.removeByBooleanVector();
        // через числовой вектор БЕЗ СЕТА
        List<Solution> res=problem.removeByVectorsNOSET();
        // Через чиловой вектор С СЕТОМ
        //List<Solution> res=problem.removeByVectorsWITHSET();
        time= java.lang.System.currentTimeMillis()-time;
        return res;
    }
}
