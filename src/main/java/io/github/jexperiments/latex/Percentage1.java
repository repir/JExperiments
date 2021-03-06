package io.github.jexperiments.latex;
import java.text.DecimalFormat;
import io.github.jexperiments.latex.Tabular.Cell;

/**
 *
 * @author Jeroen Vuurens
 */
public class Percentage1 extends RightAlign {
   public Percentage1( Tabular tabular, int column) {
      super( tabular, column );
   }
   
   @Override
   public void format(Cell cell, Object v) {
      Double d = (Double)v;
      cell.value = ((d < 0)?'-':'+') + new DecimalFormat("#0.0").format(100 * d) + "\\%";    
   }

}
