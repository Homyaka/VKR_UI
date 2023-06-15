/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constraintchoco;

import java.io.PrintWriter;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class Ship {
        Model model = new Model("ship problem");
        IntVar W = model.intVar("W", new int[]{0,1,2}); 
        IntVar X = model.intVar("X", new int[]{0,1,2}); 
        IntVar Y = model.intVar("Y", new int[]{0,1});   
        IntVar Z = model.intVar("Z", new int[]{0,1,2});
        Constraint cons=model.arithm(Y, "=", 1);
        
    public void pst(){
        model.post(cons);
    }

    public void unpst(){
        model.unpost(cons);
    }
    public void prepare() throws ContradictionException{
        model.ifThen(model.arithm(X, "=", 1), model.arithm(W, "=", 1));
        model.ifThen(
                model.or(new Constraint[]{
                    model.and(new Constraint[]{
                        model.arithm(X, "=", 2),
                        model.arithm(Y, "=", 1)}
                    ),
                    model.arithm(Z, "=", 1)}
                ), 
                model.arithm(W, "=", 2));
        model.and(new Constraint[]{model.arithm(X, "=", 1),model.or(new Constraint[]{model.arithm(Z, "=", 0),model.arithm(Z, "=", 1)})}).post();
    }
    
    public String solve(boolean prop) throws ContradictionException{
        model.getEnvironment().worldPush();
        Solver solver = model.getSolver();
        String ret="";
        if(prop){
         solver.propagate();
         ret=X.toString()+"\n"+Y.toString()+"\n"+Z.toString()+"\n"+W.toString()+"\n\n";
        }
        else{
         while (solver.solve()){
          Solution sol = new Solution(model).record();
          int sX=sol.getIntVal(X);
          int sY=sol.getIntVal(Y);
          int sZ=sol.getIntVal(Z);
          int sW=sol.getIntVal(W);
          ret+="X = "+sX+"("+(char)(sX+97)+")\nY = "+sY+"("+(char)(sY+97)+")\nZ = "+sZ+"("+(char)(sZ+97)+")\nW = "+sW+"("+(char)(sW+97)+")\n\n";
         }
        }
        if (ret.length()==0)ret="no solutions";
        solver.reset();
        model.getEnvironment().worldPop();
        return ret;
/*        List<Solution> lstsol = solver.findAllSolutions();
        if (!lstsol.isEmpty()) return lstsol.toString();
        return "no solutions";*/
    }
    
}
