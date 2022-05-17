package view;

import model.Cell;
import model.FluidSim;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.RectanglePortrayal2D;

import java.awt.*;

public class CellPortrayal extends RectanglePortrayal2D {
    FluidSim sim;

    public CellPortrayal(FluidSim sim) {
        super();
        this.sim = sim;
        paint = Color.BLACK;
        filled = true;
    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        Cell c = (Cell) object;
        if(c.isWall()) this.paint = Color.black;
        else{
            if (sim.showDensity){
                this.paint = Color.getHSBColor((float)c.getDensity() * sim.densityScale, 1.0f, 1.0f);
            }else{
                this.paint = Color.getHSBColor((float)c.getVelocity().length() * sim.velocityScale, 1.0f, 1.0f);
            }
        }
        super.draw(object, graphics, info);
    }
}
