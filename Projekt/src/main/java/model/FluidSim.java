package model;

import sim.engine.ParallelSequence;
import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Double2D;
import sim.util.Int2D;
import sim.util.Interval;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class FluidSim extends SimState {
    public ObjectGrid2D grid = new ObjectGrid2D(100, 100);
    private ParallelSequence parallelSequence;

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

    public float velocityScale = 1f;
    public float getVelocityScale() { return velocityScale; }
    public void setVelocityScale(float velocityScale) {
        this.velocityScale = velocityScale;
    }
    public Interval domVelocityScale() {return new Interval(0.1f, 5f); }

    public float gravity = 0.0f;
    public float getGravity() {return gravity;}
    public void setGravity(float gravity) {this.gravity = gravity;}

    public String loadFile = "test.store";

    public String getLoadFile(){return loadFile;}
    public void setLoadFile(String loadFile){this.loadFile = loadFile; load();}

    public String storeFile;
    public String getStoreFile(){return storeFile;}
    public void setStoreFile(String storeFile){this.storeFile = storeFile; store();}

    public FluidSim(long seed) {
        super(seed);
    }

    @Override
    public void start() {
        super.start();
        grid.clear();
        load();
    }

    @Override
    public void finish() {
        super.finish();
        if(parallelSequence != null) parallelSequence.cleanup();
    }

    public void load()
    {
        if(parallelSequence != null) parallelSequence.cleanup();

        try(FileReader fileReader = new FileReader(loadFile)) {
            Scanner scanner = new Scanner(fileReader).useLocale(Locale.US);
            grid.reshape(scanner.nextInt(), scanner.nextInt()); // maybe something else
            boolean[][] isWall = new boolean[grid.width][grid.height];
            for (int x = 0; x < grid.width; x++) {
                for (int y = 0; y < grid.height; y++) {
                    isWall[x][y] = scanner.nextBoolean();
                }
            }
            for (int x = 0; x < grid.width; x++) {
                for (int y = 0; y < grid.height; y++) {
                    Double fixedDensity = scanner.nextDouble();
                    Double2D fixedU = new Double2D(scanner.nextDouble(), scanner.nextDouble());
                    if(fixedDensity.isNaN())
                        fixedDensity = null;
                    if(((Double)fixedU.x).isNaN() || ((Double)fixedU.y).isNaN())
                        fixedU = null;
                    Cell c = new Cell(new Int2D(x, y), isWall[x][y], fixedDensity, fixedU);
                    grid.set(x, y, c);
                }
            }

            parallelSequence = new ParallelSequence(grid.elements(), ParallelSequence.CPUS);
            schedule.scheduleRepeating(parallelSequence);
        }
        catch(IOException e)
        {
            System.out.println("cannot load file");
        }
    }
    public void store()
    {
        try(FileWriter fileWriter = new FileWriter(storeFile)){
            fileWriter.write(grid.width + " " + grid.height + "\n");
            for (int x = 0; x < grid.width; x++) {
                for (int y = 0; y < grid.height; y++) {
                    Cell cell = (Cell)grid.get(x, y);
                    fileWriter.write( " " + cell.isWall);
                }
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
            for (int x = 0; x < grid.width; x++) {
                for (int y = 0; y < grid.height; y++) {
                    Cell cell = (Cell)grid.get(x, y);
                    if(cell.fixedDensity == null || cell.fixedU == null)
                        fileWriter.write(" " + Double.NaN + " " + Double.NaN + " " + Double.NaN );
                    else
                        fileWriter.write( " " + cell.fixedDensity + " " + cell.fixedU.x + " "  + cell.fixedU.y);
                }
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            System.out.println("unable to store\n");
        }

    }

}
