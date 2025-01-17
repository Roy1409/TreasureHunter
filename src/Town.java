import java.awt.*;

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
    private OutputWindow x;
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, OutputWindow x) {
        this.shop = shop;
        this.x=x;
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
       x.addTextToWindow("Welcome to town, " + hunter.getHunterName() + ".",Color.black);
        if (toughTown) {
            x.addTextToWindow("\nIt's pretty rough around here, so watch yourself.",Color.black);
        } else {
           x.addTextToWindow("\nWe're just a sleepy little town with mild mannered folk.",Color.black);
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
            x.clear();
            if(terrain.getTerrainName().equals("Jungle") && hunter.hasItemInKit("sword") ){
                x.addTextToWindow("\nYou slash your way across the jungle!\n",Color.cyan);
            } else{
           x.addTextToWindow( "\nYou used your " + item + " to cross the " + terrain.getTerrainName() + ".\n",Color.black);}
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);

                x.addTextToWindow("\nUnfortunately, you lost your "+item+".\n",Color.black);
            }
            return true;
        }
        x.clear();
        x.addTextToWindow("\nYou can't leave town, " + hunter.getHunterName() + ".\nYou don't have a " + terrain.getNeededItem() + ".",Color.black);
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     * @return
     */
    public String enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
        return printMessage;

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
            x.addTextToWindow("You couldn't find any trouble \n", Color.BLACK);

        } else {
            x.addTextToWindow("You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n",Color.red);
            int goldDiff = (int) (Math.random() * 10) + 1;
            if(Shop.issMode() && hunter.hasItemInKit("sword")) {
               x.addTextToWindow("The brawler, seeing your sword, realizes he picked a losing fight and gives you his gold",Color.GREEN);
               x.addTextToWindow("\nYou won the brawl and receive ",Color.black);
               x.addTextToWindow(goldDiff+" ",Color.yellow);
                hunter.changeGold(goldDiff);
            } else {
                if (Math.random() > noTroubleChance) {

                    if (easyMode && Math.random() < win) {
                        x.addTextToWindow("Okay, stranger! You proved yer mettle. Here, take my gold.",Color.red);
                        x.addTextToWindow("\nYou won the brawl and receive ",Color.black);
                        x.addTextToWindow(goldDiff+" ",Color.yellow);
                        x.addTextToWindow("gold",Color.black);
                        hunter.changeGold(goldDiff);
                    } else {
                        x.addTextToWindow("\nOkay, stranger! You proved yer mettle. Here, take my gold.",Color.red);
                        x.addTextToWindow("\nYou won the brawl and receive ",Color.black);
                        x.addTextToWindow(goldDiff+" ",Color.yellow);
                        x.addTextToWindow("gold",Color.black);
                        hunter.changeGold(goldDiff);
                    }

                } else {
                    x.addTextToWindow("That'll teach you to go lookin' fer trouble in MY town! Now pay up!",Color.red);
                    x.addTextToWindow("\nYou lost the brawl and lose ",Color.black);
                    x.addTextToWindow(goldDiff+" ",Color.yellow);
                    x.addTextToWindow("gold",Color.black);
                    hunter.changeGold(-goldDiff);

                    if (hunter.getGold() < 0) {
                        x.addTextToWindow(getLatestNews()+"\n",Color.black);
                        x.addTextToWindow("\n"+hunter.getHunterName() + " is in debt (-gold) ",Color.CYAN);
                        x.addTextToWindow("\nGAME OVER!",Color.CYAN);
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
            x.clear();
            x.addTextToWindow("\nYou found a crown!",Color.black);
            treasure = "crown";
            if((!hunter.hasTreasure(treasure))) {
            hunter.addCount(1);
            }
            hunter.addTreasure(treasure);

        } else if (rnd < .5){
            x.clear();
            x.addTextToWindow("\nYou found a trophy!",Color.black);
            treasure = "trophy";

            if((!hunter.hasTreasure(treasure))) {
                hunter.addCount(1);
            }
            hunter.addTreasure(treasure);

        } else if (rnd < .75){
            x.clear();
            x.addTextToWindow("\nYou found a gem!\n",Color.black);
            treasure = "gem";

            if((!hunter.hasTreasure(treasure))) {
                hunter.addCount(1);
            }
            hunter.addTreasure(treasure);

        } else {
            x.clear();
            x.addTextToWindow("You found some dust...",Color.black);
        }

    }

    public void infoString() {
        x.addTextToWindow("\nThis nice little town is surrounded by " ,Color.black);
        x.addTextToWindow(terrain.getTerrainName(),Color.CYAN);
        x.addTextToWindow(".",Color.black);
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random() * (1.2);
        if (rnd < .2) {
            return new Terrain("Mountains", "Rope",x);
        } else if (rnd < .4) {
            return new Terrain("Ocean", "Boat",x);
        } else if (rnd < .6) {
            return new Terrain("Plains", "Horse",x);
        } else if (rnd < .8) {
            return new Terrain("Desert", "Water",x);
        } else if ( rnd < 1){
            return new Terrain("Jungle", "Machete",x);
        } else {
            return new Terrain("Marsh", "Boots",x);
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
                x.addTextToWindow("\nYou dug up ",Color.black);
                x.addTextToWindow(gold+"",Color.yellow);
                x.addTextToWindow(" gold!",Color.black);
                hunter.setGold(hunter.getGold()+gold);
                foundGold=true;
            } else{
                x.addTextToWindow("You dug but only found dirt",Color.black);
            }
        } else{
            x.addTextToWindow("You can't dig for gold without a shovel.",Color.black);
        } } else{
            x.addTextToWindow("You already dug for gold in this town.", Color.black);
        }
}
    private void setEasy(boolean x) {
        easyMode=x;
    }
}