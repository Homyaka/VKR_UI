package GDSystem;

import java.util.List;

public class Grid {
    public Cell[][] grid;
    public int[] points;
    public int width;
    public int height;

    public String toString(){
        String s="";
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++)
                s+=grid[i][j]+" ";
            s+="\n";
        }
        return s;
    }

    public String pointsToGridString(){
        String res="";
        for(int i=0;i<points.length;i++){
            res+=i+" ";
            if((i+1)%width==0) res+="\n";
        }
        return res;
    }

    public Grid(int width,int height){
        this.width=width;
        this.height=height;
        grid=new Cell[height][width];
        points=new int[width*height];
        for(int i=0;i<points.length;i++){
            points[i]=i;
        }
    }


    public int fromCellToInt(int x,int y){
        return y*width+x;
    }
    public Cell fromIntToCell(int point){
        Cell c;
        int y=point/width;
        int x=point%width;
        c=grid[y][x];
        return c;
    }
    public int[] getPointCell(int point){
        int[] p=new int[2];
        p[0]=point%width;
        p[1]=point/width;
        return p;
    }
}

