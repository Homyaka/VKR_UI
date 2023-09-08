package workWithFiles;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataWorker {
    public Boolean[][] matrix;
    public List<String> attributeSet;
    public ArrayList<Integer> weight;
    public List<String> data;

    public void generatematrix(List<String> data){
        LinkedHashSet<String> set= new LinkedHashSet<>();
        String firstLine=data.get(0);
        String[] arr=firstLine.split("\t");
        for (int n=1;n<arr.length;n++){
            set.add(arr[n]);
        }
        attributeSet= new ArrayList<>(set);
        matrix= new Boolean[data.size()-1][attributeSet.size()];
        for (int i=1;i<data.size();i++) {
            String[] line=data.get(i).split("\t");
            for(int j=1;j<attributeSet.size()+1;j++){
                if (line[j].equals("1")) matrix[i-1][j-1]=true;
                else matrix[i-1][j-1]=false;
            }
        }
    }
    public List<String> txtParse(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        data=new ArrayList<>(lines);
        for (String s:lines) {
            System.out.println(s);
        }
        return lines;
    }
    public void codedAttribute(List<String> data){
        LinkedHashSet<String> set= new LinkedHashSet<>();
        for (int i=1;i<data.size();i++){
            String line=data.get(i);
            String[] linesplit=line.split("\t");
            for (int n=1;n<linesplit.length-1;n++){
                set.add(linesplit[n]);
            }
        }
        attributeSet= new ArrayList<>(set);
    }
    public List<String> convertToOATable(List<String> data,List<String> attributes){//Преобразует данные в ОП таблицу
        List<String> OAtable= new ArrayList<>();
        String firstLine="#";
        matrix =new Boolean[data.size()-1][attributes.size()];
        weight= new ArrayList<>();
        for(int i=1;i<=attributes.size();i++){
            firstLine+="\tm"+i;
        }
        OAtable.add(firstLine);
        for (int i=1;i<data.size();i++) {
            String lineOATable="g"+i;
            for(int j=0;j<attributes.size();j++){
                if (data.get(i).contains(attributes.get(j))){
                    lineOATable+="\t"+1;
                    matrix[i-1][j]=true;
                }
                else {
                    lineOATable +="\t"+0;
                    matrix[i-1][j]=false;
                }
            }
            OAtable.add(lineOATable);
            weight.add(Integer.parseInt(data.get(i).substring(data.get(i).lastIndexOf('\t')+1)));
        }
        return OAtable;
    }
    public String buildLine(ArrayList<Integer> x, ArrayList<Integer> y){
        String line="{"+x.get(0);
        for (int i=1;i<x.size();i++) line+=(";"+x.get(i));
        line+=("} {"+y.get(0));
        for (int j=1;j< y.size();j++)line+=(";"+y.get(j));
        line+="}";
        return line;
    }
    public List<String> convertToDTable(){
        weight= new ArrayList<>();
        for (String s:attributeSet) System.out.print(s+" ");
        System.out.println("\n");
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<attributeSet.size();j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println("\n");
        }
        List<String> dtable=new ArrayList<>();
        dtable.add("X Y");
        ArrayList<Integer> att=new ArrayList<>();
        ArrayList<Integer> obj= new ArrayList<>();
        for (int m = 1; m< matrix.length+1; m++) obj.add(m);
        for (int n=1;n<attributeSet.size()+1;n++) att.add(n);
        for (int q=1;q<data.size();q++) weight.add(Integer.parseInt(data.get(q).substring(data.get(q).lastIndexOf('\t')+1)));
        dtable.add(buildLine(obj,att));
        ArrayList<Integer> empty=new ArrayList<>();
        empty.add(-1);
        dtable.add(buildLine(weight,empty));
        for(int j=0;j<attributeSet.size();j++){
            ArrayList<Integer>  x= new ArrayList<>();
            ArrayList<Integer> y= new ArrayList<>(att);
            for (int i = 0; i< matrix.length; i++){
                if (matrix[i][j]) x.add(i+1);
            }
            y.remove(j);
            dtable.add(buildLine(x,y));
        }
        return dtable;
    }
}
