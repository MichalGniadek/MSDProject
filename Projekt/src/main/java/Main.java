import model.FluidSim;
import sim.display.Console;
import view.FluidSimGUI;

public class Main {
    public static void main(String[] args) {
        var model = new FluidSim(System.currentTimeMillis());
        var gui = new FluidSimGUI(model);

        Console console = new Console(gui);
        console.setVisible(true);
    }
}