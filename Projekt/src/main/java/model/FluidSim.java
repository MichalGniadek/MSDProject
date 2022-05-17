package model;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Double2D;
import sim.util.Int2D;
import sim.util.Interval;

public class FluidSim extends SimState {
    public ObjectGrid2D grid = new ObjectGrid2D(100, 100);

    public boolean showDensity = true;
    public boolean isShowDensity() { return showDensity; }
    public void setShowDensity(boolean showDensity) {
        this.showDensity = showDensity;
    }

    public float densityScale = 0.1f;
    public float getDensityScale() { return densityScale; }
    public void setDensityScale(float densityScale) {
        this.densityScale = densityScale;
    }
    public Interval domDensityScale() {return new Interval(0.05f, 0.5f); }

    public float gravity = 0.0f;
    public float getGravity() {return gravity;}
    public void setGravity(float gravity) {this.gravity = gravity;}

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
                var isWall = y == 0 || y == grid.height - 1 || x == grid.width - 1;
                isWall |= 45 < x && x < 55 && 45 < y && y < 55;

                Double fixedDensity = null;
                Double2D fixedU = null;

                if(x == 0 && y != 0 && y != grid.height - 1){
                    fixedDensity = 1.0;
                    fixedU = new Double2D(-1,0);
                }

                var cell = new Cell(position, isWall, fixedDensity, fixedU);
                schedule.scheduleRepeating(cell);
                grid.set(x, y, cell);
            }
        }
    }
}