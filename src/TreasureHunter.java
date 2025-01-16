import java.awt.*;
import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean searched;
    private boolean win;
    private final OutputWindow x=new OutputWindow();

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode=false;
        searched = false;
        win = false;
    }




    /**
     * Starts the game; this is the only public method
     */
    public void play() {
            welcomePlayer();
            x.clear();
            if (!win) {
                enterTown();
                showMenu();
            } else {
                x.addTextToWindow("Congratulations, you have found the last of the three treasures, you win!", Color.CYAN);
            }
            x.clear();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        x.addTextToWindow("Welcome to TREASURE HUNTER!\n",Color.BLACK);
        x.addTextToWindow("\nGoing hunting for the big treasure, eh?",Color.BLACK);
        x.addTextToWindow("\nWhat's your name, Hunter? ",Color.black);
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, 20,x);

        x.addTextToWindow("\nmode? (e/n/h): \n",Color.black);
        String hard = SCANNER.nextLine().toLowerCase();

        if (hard.equals("e")) {
            easyMode=true;
            hunter=new Hunter(name,40,x);

        }
        if(hard.equals("s")) {
            Shop.setsMode(true);
        }
        if (hard.equals("h")) {
            hardMode = true;
            hunter=new Hunter(name,10,x);

        } else if (hard.equals("test")) {
            hunter=new Hunter(name, 100,x);
            hunter.addAll();
        }
        else if (hard.equals("testlose")) {
            hunter=new Hunter(name,5,x);
            hunter.setTestLose(true);

        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        } else if(easyMode){
            markdown=1;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown,x);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness,x);
        searched = false;

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }


    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x")) {

         {

             x.addTextToWindow(currentTown.getLatestNews()+"\n***\n",Color.black);
            hunter.infoString();
            currentTown.infoString();
            x.addTextToWindow("\n"+"(B)uy something at the shop\n(S)ell something at the shop."+"\n(E)xplore surrounding terrain."+"\n(M)ove on to a different town."+"\n(L)ook for trouble!"+"\n(H)unt for treasure \n(D)ig for gold"+"\nGive up the hunt and e(X)it.\nWhat's your next move? ",Color.black);

            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice); }
            if (hunter.getGold()<0) {
                choice="x";
            }
            if(hunter.treasureCount()==3) {
                System.out.println(Colors.CYAN+"Congratulations, you have found the last of the three treasures, you win!"+Colors.RESET);
                choice="x";
            }


        }


        }


    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            x.clear();
            x.addTextToWindow(currentTown.enterShop(choice),Color.black);
        } else if (choice.equals("e")) {
            x.clear();
           currentTown.getTerrain().infoString();
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                currentTown.getLatestNews();
                enterTown();
            }
        } else if (choice.equals("l")) {
            x.clear();
            currentTown.lookForTrouble();
        } else if (choice.equals("x")) {
            x.clear();
            x.addTextToWindow("\nFare thee well, " + hunter.getHunterName() + "!",Color.black);
        } else if (choice.equals("h")) {
            if (!searched) {
                currentTown.Treasure();
                searched = true;
            } else {
                x.clear();
                x.addTextToWindow("\nYou have already searched this town!",Color.black);

            }
        } else if(choice.equals("d" )){
            x.clear();
            currentTown.dig();
        }else {
            x.clear();

            x.addTextToWindow("\nYikes! That's an invalid option! Try again.",Color.black);
        }

    }


}