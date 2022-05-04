package model;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;

public class FluidSim extends SimState {
    public ObjectGrid2D grid = new ObjectGrid2D(100, 100);

    public FluidSim(long seed) {
        super(seed);
    }

    @Override
    public void start() {
        super.start();
        grid.clear();
        for (int x = 0; x < grid.width; x++) {
            for (int y = 0; y < grid.height; y++) {
                var position = new Int2D(x, y);
                var cell = new Cell(position, random.nextFloat() < 0.6f);
                schedule.scheduleRepeating(cell);
                grid.set(x, y, cell);
            }
        }
    }
}
