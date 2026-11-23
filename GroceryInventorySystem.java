
import java.util.*;
import java.io.*;

class GroceryItem {
    String id;
    String name;
    int quantity;
    double price;

    GroceryItem(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    void display() {
        System.out.println(id + " | " + name +
                " | Qty: " + quantity + " | Price: " + price);
    }
}

public class GroceryInventorySystem {

    static ArrayList<GroceryItem> inventory = new ArrayList<>();

    static HashMap<String, GroceryItem> itemMap = new HashMap<>();

    static int nextId = 101;

    static Scanner sc = new Scanner(System.in);

    static final String FILE_NAME = "inventory.txt";

    // Add item
    static void addItem() {

        System.out.print("Enter item name: ");
        String name = sc.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();

        System.out.print("Enter price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        if (quantity < 0 || price < 0) {
            System.out.println("Invalid quantity or price.");
            return;
        }

        String id = "G" + nextId++;

        GroceryItem item = new GroceryItem(id, name, quantity, price);

        inventory.add(item);
        itemMap.put(id, item);

       System.out.println("Item added. ID: " + id +
        " | " + name +
        " | Qty: " + quantity +
        " | Price: " + price);
    }

    // Sell item
    static void sellItem() {

        System.out.print("Enter item ID: ");
        String id = sc.nextLine();

        GroceryItem item = itemMap.get(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.print("Enter quantity to sell: ");
        int sellQty = sc.nextInt();
        sc.nextLine();

        if (sellQty <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (item.quantity == 0) {
            System.out.println("Out of stock.");
            return;
        }

        if (sellQty > item.quantity) {
            System.out.println("Insufficient stock. Available: "
                    + item.quantity);
            return;
        }

        item.quantity -= sellQty;

        System.out.println("Sold " + sellQty + " of " + item.name + ".");

        if (item.quantity == 0) {
            System.out.println("Stock: 0 (Out of stock)");
        } else {
            System.out.println("Remaining stock: " + item.quantity);
        }
    }

    // Display items sorted by name
    static void displaySorted() {

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        ArrayList<GroceryItem> sorted = new ArrayList<>(inventory);

        // Bubble sort (manual sorting logic)
        for (int i = 0; i < sorted.size() - 1; i++) {

            for (int j = 0; j < sorted.size() - i - 1; j++) {

                String name1 = sorted.get(j).name;
                String name2 = sorted.get(j + 1).name;

                if (name1.compareToIgnoreCase(name2) > 0) {

                    GroceryItem temp = sorted.get(j);

                    sorted.set(j, sorted.get(j + 1));

                    sorted.set(j + 1, temp);
                }
            }
        }

        System.out.println("\n--- Inventory Sorted by Name ---");

        int count = 1;

        for (GroceryItem item : sorted) {
            System.out.print(count + ". ");
            item.display();
            count++;
        }
    }

    // Low stock report
    static void lowStockReport() {

        System.out.println("\n--- Low Stock Report ---");

        boolean found = false;

        for (GroceryItem item : inventory) {

            if (item.quantity < 5) {

                System.out.println(item.id + " | " + item.name
                        + " | Qty: " + item.quantity);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No low-stock items.");
        }
    }

    // Load inventory from file
    static void loadData() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length != 4) {
                    continue;
                }

                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                double price = Double.parseDouble(data[3]);

                GroceryItem item = new GroceryItem(
                        id, name, quantity, price
                );

                inventory.add(item);
                itemMap.put(id, item);

                int number = Integer.parseInt(id.substring(1));

                if (number >= nextId) {
                    nextId = number + 1;
                }
            }

            System.out.println("Inventory loaded successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    // Save inventory to file
    static void saveData() {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME))) {

            for (GroceryItem item : inventory) {

                bw.write(item.id + "|" + item.name + "|"
                        + item.quantity + "|" + item.price);

                bw.newLine();
            }

            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {

        loadData();

        while (true) {

            System.out.println("\n===== GROCERY INVENTORY SYSTEM =====");
            System.out.println("1. Add Item");
            System.out.println("2. Sell Item");
            System.out.println("3. Display Sorted by Name");
            System.out.println("4. Low-Stock Report");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid menu choice.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addItem();
                    break;

                case 2:
                    sellItem();
                    break;

                case 3:
                    displaySorted();
                    break;

                case 4:
                    lowStockReport();
                    break;

                case 5:
                    saveData();
                    System.out.println("Thank you for using Grocery Inventory System!");
                    sc.close();
                    return;


                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
