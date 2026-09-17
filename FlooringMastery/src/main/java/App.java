import flooringmastery.controller.FlooringMasteryController;
import flooringmastery.view.FlooringMasteryView;
import flooringmastery.view.UserIO;
import flooringmastery.view.UserIOConsoleImpl;

public class App {

    public static void main(String[] args) {
        FlooringMasteryController masteryController = new FlooringMasteryController();
        masteryController.run();
    }
}
