package flooringmastery.view;

public class FlooringMasteryView {
    private UserIO io = new UserIOConsoleImpl();

    public int displayMainMenuAndGetSelection(){

        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public void exitMessage(){
        io.print("Cya later, BYE!");
    }

    public void unknownCommand(){
        io.print("Unknown Command!!!");
    }
}
