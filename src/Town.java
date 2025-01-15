/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private static boolean easyMode;
    private boolean foundGold;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;

        this.terrain = getNewTerrain();
        foundGold=false;



        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your "+item+".";
            }
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        double win=0;

        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (hunter.isTestLose()) {
            noTroubleChance=0.99;
        }
        if(easyMode) {
            win=0.90;
            {

            }
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if(Shop.issMode() && hunter.hasItemInKit("sword")) {
                printMessage += Colors.GREEN + "The brawler, seeing your sword, realizes he picked a losing fight and gives you his gold" + Colors.RESET;
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                if (Math.random() > noTroubleChance) {

                    if (easyMode && Math.random() < win) {
                        printMessage += Colors.RED + "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                        printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                        hunter.changeGold(goldDiff);
                    } else {
                        printMessage += Colors.RED + "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                        printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                        hunter.changeGold(goldDiff);
                    }

                } else {
                    printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                    printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                    hunter.changeGold(-goldDiff);

                    if (hunter.getGold() < 0) {
                        System.out.println(getLatestNews());
                        System.out.println(hunter.getHunterName() + " is in debt (-gold) ");
                        System.out.println("GAME OVER");
                    }
                }
            }
        }

    }

    public void Treasure(){
        double rnd = Math.random();
        int count = 0;
        String treasure = "";
        if (rnd < .25){
            System.out.println("You found a crown!");
            treasure = "crown";
            hunter.addTreasure(treasure);
            if (hunter.hasTreasure(treasure)){
                count++;
            }
        } else if (rnd < .5){
            System.out.println("You found a trophy!");
            treasure = "trophy";
            hunter.addTreasure(treasure);
            if (hunter.hasTreasure(treasure)){
                count++;
            }
        } else if (rnd < .75){
            System.out.println("You found a gem!");
            treasure = "gem";
            hunter.addTreasure(treasure);
            if (hunter.hasTreasure(treasure)){
                count++;
            }
        } else {
            System.out.println("You found some dust...");
        }
        if (count == 3){
            TreasureHunter.win(true);
        }
    }

    public String infoString() {
        return "This nice little town is surrounded by " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random() * (1.2);
        if (rnd < .2) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < .4) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < .6) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < .8) {
            return new Terrain("Desert", "Water");
        } else if ( rnd < 1){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        if(easyMode) {
            return true;
        }
        double rand = Math.random();
        return (rand < 0.5);
    }
public void dig() {
        if (!foundGold) {
        if (hunter.hasItemInKit("shovel")) {
            if(Math.random()>0.5) {
                int gold= (int) (Math.random() * 20) +1;
                System.out.println("You dug up "+gold+" gold!");
                hunter.setGold(hunter.getGold()+gold);
                foundGold=true;
            } else{
                System.out.println("You dug but only found dirt.");
            }
        } else{
            System.out.println("You can't dig for gold without a shovel.");
        } } else{
            System.out.println( "You already dug for gold in this town.");
        }
}
    private void setEasy(boolean x) {
        easyMode=x;
    }
}