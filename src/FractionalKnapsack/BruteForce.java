package FractionalKnapsack;

import KnapsackItems.Item;
import KnapsackItems.Knapsack;

import java.util.ArrayList;

/**
 * Brute force solution to the Fractional Knapsack problem: O(2^n)
 * @author Rory Hackney
 */
public class BruteForce {
//    public static void main(String[] args) {
////        displayWeightAndBenefit(
////                new Knapsack(1, 5, new Item[]{new Item(3, 3), new Item(2, 2), new Item(1, 1)}),
////                2, 0, 0, new ArrayList<>());
//
//        Knapsack bag = new Knapsack(1, 5, new Item[]{new Item(3, 3), new Item(2, 2), new Item(1, 1)});
//        int res = solveTheProblem(bag);
//        System.out.println(res);
//    }

    //need: items (weight, benefit, item number)
    //I think recursively split between (include this) and (don't include this)
    //then return the max of the two such that the weight <= max
    //if the remaining weight is a fraction, just add that much of current item
    //do we return the final value? the sequence of items/amount?
    public static void test(int n, String test) {
        if (n < 1) {
            System.out.println("[" + test + "]");
        } else {
            test(n - 1, 0 + test);
            test(n - 1, 1 + test);
        }
    }

    public static void testItems(Item[] items, int currentIndex, String test) {
        if (currentIndex < 0) {
            System.out.println("[" + test + "]");
        } else {
            testItems(items, currentIndex - 1, test);
            testItems(items, currentIndex - 1, items[currentIndex] + test);
        }
    }

    public static double testTotalBenefit(Item[] items, int currentIndex, double sum) {
        if (currentIndex < 0) {
            return sum;
        }
        return Math.max(testTotalBenefit(items, currentIndex - 1, sum),
                testTotalBenefit(items, currentIndex - 1, sum + items[currentIndex].getBenefit()));
    }

    public static double testTotalBenefit(Knapsack sack, int currentIndex, double sum) {
        if (currentIndex < 0) return sum;
        return Math.max(testTotalBenefit(sack, currentIndex - 1, sum),
                testTotalBenefit(sack, currentIndex - 1, sum + sack.getItems()[currentIndex].getBenefit()));
    }

    //problem: we are passing the same arraylist around, so it is adding the same item multiple times
    public static void displayWeightAndBenefit(Knapsack sack, int currentIndex, double sum, double weight, ArrayList<Item> items) {
        if (currentIndex < 0) {
            System.out.println("Items: " + items);
            System.out.println("Profit: " + sum);
            System.out.println("Weight: " + weight);
        } else {
            displayWeightAndBenefit(sack, currentIndex - 1, sum, weight, (ArrayList<Item>) items.clone()); //don't add
            if (weight + sack.getItems()[currentIndex].getWeight() <= sack.getCapacity()) {
                items.add(sack.getItems()[currentIndex]);
                displayWeightAndBenefit(sack, currentIndex - 1,
                        sum + sack.getItems()[currentIndex].getBenefit(),
                        weight + sack.getItems()[currentIndex].getWeight(), (ArrayList<Item>) items.clone()); //do add
            } else {
                double diff = sack.getCapacity() - weight; //have space for 2 weight
                if (diff > 0) { // if there is room for stuff
                    double ratio = diff / (double) sack.getItems()[currentIndex].getWeight();
                    double profit = ratio * sack.getItems()[currentIndex].getBenefit(); //
//                    System.out.println("profit" + profit + ", diff" + diff + "ratio" + ratio);
                    items.add(sack.getItems()[currentIndex]);
                    displayWeightAndBenefit(sack, currentIndex - 1,
                            (int) (sum + profit), sack.getCapacity(), (ArrayList<Item>) items.clone());
                }
            }
        }
    }

    private double maxBenefit;
    private String finalItems;

    public BruteForce() {
        maxBenefit = 0;
        finalItems = "";
    }

    public double getMaxBenefit() {return maxBenefit;}

    public String getFinalItems() {return finalItems;}

    //put it all together
    //should return 5 with the given main
    //should somehow return the list of items added, the profit (a String?)
    //Greedy just returns the benefit so idk
    //output (in Main.main)
    //Knapsack #1, Max capacity X
    //Potential Items
    //Added Items x, and x/x of item x, for a total profit of x
    public double solveTheProblem(Knapsack sack) {
        double maxProfit = solveTheProblem(sack, sack.getItems().length - 1, 0, 0, "");
        return maxProfit;
    }

    private double solveTheProblem(Knapsack sack, int currentIndex, double profit, int weight, String items) {
        if (currentIndex < 0 || weight >= sack.getCapacity()) {
            if (Double.compare(profit, maxBenefit) > 0) finalItems = items;
            return profit;
        } else {
            Item item = sack.getItems()[currentIndex];
            if (weight + item.getWeight() > sack.getCapacity()) {
                double ratio = (double)(sack.getCapacity() - weight) / item.getWeight();
                double benefitToAdd = ratio * item.getBenefit();
                return Math.max(solveTheProblem(sack, currentIndex - 1, profit + benefitToAdd, sack.getCapacity(),
                        String.format("Full amounts of items %s\nPartial benefit %.2f, weight %d of item %s",
                                items, benefitToAdd, (sack.getCapacity() - weight), item)),

                        solveTheProblem(sack, currentIndex - 1, profit, weight, items));
            } else {
                return Math.max(solveTheProblem(sack, currentIndex - 1, profit, weight, items),
                        solveTheProblem(sack, currentIndex - 1, profit + item.getBenefit(),
                                weight + item.getWeight(), item + items));
            }
        }
    }

}
