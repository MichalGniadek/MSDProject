package view;

import model.Cell;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.RectanglePortrayal2D;

import java.awt.*;

public class CellPortrayal extends RectanglePortrayal2D {
    public CellPortrayal() {
        super();
        paint = Color.black;
        filled = true;
    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        Cell c = (Cell) object;
        this.paint = c.isAlive() ? Color.red : Color.black;
        super.draw(object, graphics, info);
    }
}
