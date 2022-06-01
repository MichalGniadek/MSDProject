package model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.Grid2D;
import sim.portrayal.Oriented2D;
import sim.portrayal.Scaled2D;
import sim.util.Double2D;
import sim.util.Int2D;
import java.util.EnumMap;

import static java.lang.Math.atan2;

public class Cell implements Steppable, Oriented2D, Scaled2D {
    enum StepType {Collide, Flow}

    private final Int2D position;
    private StepType step = StepType.Collide;

    public EnumMap<Direction, Double> directionalDensity = new EnumMap<>(Direction.class);
    public EnumMap<Direction, Double> newDirectionalDensity = new EnumMap<>(Direction.class);

    boolean isWall;

    Double fixedDensity;
    Double2D fixedU;

    double density = 0;
    Double2D u = new Double2D(0, 0);

    private float velocityScale = 1f;

    public Cell(Int2D position, boolean isWall, Double fixedDensity, Double2D fixedU) {
        this.position = position;
        this.isWall =  isWall;
        this.fixedDensity = fixedDensity;
        this.fixedU = fixedU;
        for (var direction : Direction.values()) {
            directionalDensity.put(direction,  1.0);
            newDirectionalDensity.put(direction, 0.0);
        }
    }

    @Override
    public void step(SimState state) {
        if(isWall) return;
        var sim = (FluidSim) state;

        velocityScale = sim.velocityScale;

        if (step == StepType.Collide) {
            if(fixedDensity != null && fixedU != null) {
                density = fixedDensity;
                u = fixedU;
            }
            else {
                density = 0;

                u = new Double2D(0, 0);
                for (var entry : directionalDensity.entrySet()) {
                    u = u.add(entry.getKey().getVector().multiply(entry.getValue()));
                    density += entry.getValue();
                }

                u = u.add(new Double2D(0, sim.gravity)).multiply(1.0 / density);
            }

            for (var direction : Direction.values()) {
                var eu = direction.getVector().dot(u);
                var eqValue = density * direction.getWeight() *
                        (1 + 3 * eu + 9.0 / 2.0 * eu * eu - 3.0 / 2.0 * u.lengthSq());

                var oldValue = directionalDensity.get(direction);
                final double omega = 1.0;
                newDirectionalDensity.put(direction, oldValue + omega * (eqValue - oldValue));
            }

            step = StepType.Flow;
        } else {
            var neighbors = sim.grid.getMooreNeighbors(position.x, position.y, 1, Grid2D.BOUNDED, true);

            for (var neigh : neighbors) {
                var c = ((Cell) neigh);
                var vector = c.position.subtract(position);
                var direction = Direction.fromVector(new Double2D(vector.x, vector.y));
                if (c.isWall){
                    directionalDensity.put(direction.opposite(), newDirectionalDensity.get(direction));
                }else {
                    c.directionalDensity.put(direction, newDirectionalDensity.get(direction));
                }
            }

            step = StepType.Collide;
        }
    }

    public double getDensity() {
        return density;
    }
    public Double2D getVelocity() {
        return u;
    }
    public boolean isWall() {
        return isWall;
    }
    public void setWall(boolean wall) {
        isWall = wall;
    }

    @Override
    public double orientation2D() {
        return atan2(-u.y, -u.x);
    }

    @Override
    public double getScale2D() {
        return isWall ? 0 : Math.sqrt(u.length()) * 10 * velocityScale;
    }
}
