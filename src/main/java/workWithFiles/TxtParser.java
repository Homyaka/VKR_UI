package workWithFiles;

import Mysystem.Line;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TxtParser {

    public List<String> txtParse(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName("windows-1251"));
        for (String s:lines) {
            System.out.println(s);
        }
        List<String> t=convertToOATable(lines,codedAttribute(lines));
        for (String str:t) System.out.println(str);
        return lines;
    }

    public Boolean containsAttribute(List<String> data,int object ,String attrubute){
        String line=data.get(object);
        if(line.contains(attrubute)) return true;
        return false;
    }

    public List<String> codedAttribute(List<String> data){
        LinkedHashSet<String> set= new LinkedHashSet<>();
        for (int i=1;i<data.size();i++){
            String line=data.get(i);
            String[] linesplit=line.split("\t");
            for (int n=1;n<linesplit.length-1;n++){
                set.add(linesplit[n]);
            }
        }
        return new ArrayList<>(set);
    }

    public List<String> buildAttribute(List<String> data){
        return data;
    }

    public List<String> convertToOATable(List<String> data,List<String> attributes){//Преобразует данные в ОП таблицу
        List<String> OAtable= new ArrayList<>();
        String firstLine="#";
        for(int i=1;i<=attributes.size();i++){
            firstLine+="\tm"+i;
        }
        OAtable.add(firstLine);
        for (int i=1;i<data.size();i++) {
            String lineOATable="g"+i;
            for(String att: attributes){
                if (data.get(i).contains(att))
                    lineOATable+="\t"+1;
                else lineOATable +="\t"+0;
            }
            OAtable.add(lineOATable);
        }
        return OAtable;
    }
}
