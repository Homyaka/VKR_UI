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
    public Problem problem;
    public  long time;

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

    public List<Solution> findSolutions(String filePath, int minSup) throws IOException {
        time= java.lang.System.currentTimeMillis();
        problem = new Problem("test",minSup);
        problem.addsys("test",loadsys(filePath));
        problem.generateArrayLong();
        problem.fillArrayLong();
        problem.solvemany(true);
        List<Solution> res=problem.removeByVectorsNOSET();
        time= java.lang.System.currentTimeMillis()-time;
        return res;
    }

    public List<Solution> findSolutionsWithConstrain(String filePath, int minSup,int containAtt,int noContainAtt,int lengthPattern,ArrayList<Integer> subPattern,ArrayList<Integer> superPattern) throws IOException {
        time= java.lang.System.currentTimeMillis();
        problem = new Problem("test",minSup,containAtt,noContainAtt,lengthPattern,subPattern,superPattern);
        problem.addsys("test",loadsys(filePath));
        problem.generateArrayLong();
        problem.fillArrayLong();
        problem.solvemany(true);
        List<Solution> res=problem.removeByVectorsNOSET();
        time= java.lang.System.currentTimeMillis()-time;
        return res;
    }
}