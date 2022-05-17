package view;

import model.Cell;
import model.FluidSim;
import sim.display.GUIState;
import sim.display.Manipulating2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.LocationWrapper;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.portrayal.simple.VectorPortrayal2D;
import java.awt.*;
import java.awt.event.MouseEvent;

public class CellPortrayal extends SimplePortrayal2D {
    private final RectanglePortrayal2D rectanglePortrayal = new RectanglePortrayal2D();
    private final VectorPortrayal2D vectorPortrayal2D = new VectorPortrayal2D();

    FluidSim sim;

    public CellPortrayal(FluidSim sim) {
        super();
        this.sim = sim;
        rectanglePortrayal.paint = Color.BLACK;
        rectanglePortrayal.filled = true;
    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        Cell c = (Cell) object;

        if (sim.showDensity){
            rectanglePortrayal.paint = c.isWall() ? Color.black :
                    Color.getHSBColor((float)c.getDensity() * sim.densityScale, 1.0f, 1.0f);
        }

        currentPortrayal().draw(object, graphics, info);
    }

    SimplePortrayal2D currentPortrayal(){
        return sim.showDensity ? rectanglePortrayal : vectorPortrayal2D;
    }

    @Override
    public boolean hitObject(Object object, DrawInfo2D range) {
        return currentPortrayal().hitObject(object, range);
    }

    @Override
    public boolean setSelected(LocationWrapper wrapper, boolean selected) {
        return currentPortrayal().setSelected(wrapper, selected);
    }

    @Override
    public boolean handleMouseEvent(GUIState guistate, Manipulating2D manipulating, LocationWrapper wrapper, MouseEvent event, DrawInfo2D fieldPortrayalDrawInfo, int type) {
        return currentPortrayal().handleMouseEvent(guistate, manipulating, wrapper, event, fieldPortrayalDrawInfo, type);
    }

    @Override
    public Inspector getInspector(LocationWrapper wrapper, GUIState state) {
        return currentPortrayal().getInspector(wrapper, state);
    }
}
