package view;

import model.Cell;
import model.FluidSim;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.ObjectGridPortrayal2D;

import javax.swing.*;
import java.awt.*;

public class FluidSimGUI extends GUIState {
    private final ObjectGridPortrayal2D gridPortrayal2D = new ObjectGridPortrayal2D();
    private Display2D display;

    public FluidSimGUI(SimState state) {
        super(state);
    }

    @Override
    public void init(Controller controller) {
        super.init(controller);
        display = new Display2D(600, 600, this);
        display.setClipping(false);
        JFrame displayFrame = display.createFrame();
        displayFrame.setTitle("Fluid sim");
        controller.registerFrame(displayFrame);
        displayFrame.setVisible(true);
        display.attach(gridPortrayal2D, "Grid");
    }

    @Override
    public void start() {
        super.start();
        setup();
    }

    @Override
    public void load(SimState state) {
        super.load(state);
        setup();
    }

    void setup() {
        var gof = (FluidSim) state;
        gridPortrayal2D.setField(gof.grid);
        gridPortrayal2D.setPortrayalForClass(Cell.class, new CellPortrayal(gof));

        display.reset();
        display.setBackdrop(Color.BLACK);
        display.repaint();
    }

    @Override
    public Object getSimulationInspectedObject() {
        return state;
    }

    @Override
    public Inspector getInspector() {
        Inspector i = super.getInspector();
        i.setVolatile(true);
        return i;
    }
}
