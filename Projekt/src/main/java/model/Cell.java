package model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.Grid2D;
import sim.util.Int2D;

public class Cell implements Steppable {
    enum StepType {Calculate, Update}

    private final Int2D position;
    private boolean alive;
    private boolean aliveNext;
    private StepType step = StepType.Calculate;

    public Cell(Int2D position, boolean alive) {
        this.position = position;
        this.alive = alive;
    }

    @Override
    public void step(SimState state) {
        if (step == StepType.Calculate) {
            var gof = (FluidSim) state;
            var neighbors = gof.grid.getMooreNeighbors(position.x, position.y, 1, Grid2D.TOROIDAL, false);
            var count = neighbors.stream().filter(o -> ((Cell) o).alive).count();

            if (alive && (count < 2 || count > 3)) {
                aliveNext = false;
            } else if (!alive && count == 3) {
                aliveNext = true;
            } else {
                aliveNext = alive;
            }

            step = StepType.Update;
        } else {
            alive = aliveNext;
            step = StepType.Calculate;
        }
    }

    public boolean isAlive() {
        return alive;
    }
}
