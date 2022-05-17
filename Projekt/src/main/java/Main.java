import model.FluidSim;
import sim.display.Console;
import view.FluidSimGUI;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        // Fixes a weird bug with parsing slider value
        Locale.setDefault(new Locale("en", "us"));

        var model = new FluidSim(System.currentTimeMillis());
        var gui = new FluidSimGUI(model);

        Console console = new Console(gui);
        console.setVisible(true);
    }
}