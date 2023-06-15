/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constraintchoco;

import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class Testsolvers {
  Model model;  
  public Testsolvers(){
      //model = new Model();
  }
  
  
  public void paretto(){
    model = new Model(); 
    IntVar a = model.intVar("a", 0, 2, false); 
    IntVar b = model.intVar("b", 0, 2, false); 
    IntVar c = model.intVar("c", 0, 2, false); 
    model.arithm(a, "+", b, "=", c).post();
    ParetoOptimizer po = new ParetoOptimizer(Model.MAXIMIZE,new IntVar[]{a,b}); 
    Solver solver = model.getSolver(); 
    solver.plugMonitor(po);
    
    while(solver.solve());
    
    List<Solution> paretoFront = po.getParetoFront(); 
    System.out.println("The pareto front has "+paretoFront.size()+" solutions : "); 
    for(Solution s:paretoFront){ 
        System.out.println("a = "+s.getIntVal(a)+" and b = "+s.getIntVal(b)); 
    }


    }

  public void multi(){
  // Model objective function 3X + 4Y
      model = new Model(); 
      IntVar OBJ = model.intVar("objective", 0, 999); 
      IntVar X = model.intVar("X",0,999);
      IntVar Y = model.intVar("Y",0,999);
      model.scalar(new IntVar[]{X,Y}, new int[]{3,4},"+", OBJ).post();
      model.setObjective(true, OBJ);
      Solver solver = model.getSolver();
      String ret="";
      while (solver.solve()){
          Solution sol = new Solution(model).record();
              ret+="3*"+sol.getIntVal(X)+" + 4*"+sol.getIntVal(Y)+" = "+sol.getIntVal(OBJ)+"\n";
          }
         System.out.print(ret);
         }

 FirstFail q = new FirstFail(model);
  
  }
  

