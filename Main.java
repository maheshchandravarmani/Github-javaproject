import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Food implements Serializable {
    int itemNo;
    int quantity;   
    float price;
    
    Food(int itemNo, int quantity) {
        this.itemNo = itemNo;
        this.quantity = quantity;
        this.price = calculatePrice(itemNo, quantity);
    }

    private float calculatePrice(int itemNo, int quantity) {
        switch (itemNo) {
            case 1: return quantity * 50;
            case 2: return quantity * 60;
            case 3: return quantity * 70;
            case 4: return quantity * 30;
            default: return 0;
        }
    }
}

class SingleRoom implements Serializable {
    String name;
    String contact;
    String gender;   
    ArrayList<Food> foodOrders = new ArrayList<>();

    SingleRoom(String name, String contact, String gender) {
        this.name = name;
        this.contact = contact;
        this.gender = gender;
    }
}

class DoubleRoom extends SingleRoom {
    String name2;
    String contact2;
    String gender2;  

    DoubleRoom(String name, String contact, String gender, String name2, String contact2, String gender2) {
        super(name, contact, gender);
        this.name2 = name2;
        this.contact2 = contact2;
        this.gender2 = gender2;
    }
}

class NotAvailable extends Exception {
    @Override
    public String toString() {
        return "Not Available!";
    }
}

class HotelRooms implements Serializable {
    DoubleRoom[] luxuryDoubleRooms = new DoubleRoom[10]; 
    DoubleRoom[] deluxeDoubleRooms = new DoubleRoom[20]; 
    SingleRoom[] luxurySingleRooms = new SingleRoom[10]; 
    SingleRoom[] deluxeSingleRooms = new SingleRoom[20]; 
}

class Hotel {
    static HotelRooms hotelData = new HotelRooms();
    static Scanner scanner = new Scanner(System.in);

    static void enterCustomerDetails(int roomType, int roomIndex) {
        String name, contact, gender;
        String name2 = null, contact2 = null; 
        String gender2 = "";

        System.out.print("Enter customer name: ");
        name = scanner.next();
        System.out.print("Enter contact number: ");
        contact = scanner.next();
        System.out.print("Enter gender: ");
        gender = scanner.next();

        if (roomType < 3) { // If it's a double room
            System.out.print("Enter second customer name: ");
            name2 = scanner.next();
            System.out.print("Enter second contact number: ");
            contact2 = scanner.next();
            System.out.print("Enter second gender: ");
            gender2 = scanner.next();
        }

        switch (roomType) {
            case 1: hotelData.luxuryDoubleRooms[roomIndex] = new DoubleRoom(name, contact, gender, name2, contact2, gender2); break;
            case 2: hotelData.deluxeDoubleRooms[roomIndex] = new DoubleRoom(name, contact, gender, name2, contact2, gender2); break;
            case 3: hotelData.luxurySingleRooms[roomIndex] = new SingleRoom(name, contact, gender); break;
            case 4: hotelData.deluxeSingleRooms[roomIndex] = new SingleRoom(name, contact, gender); break;
            default: System.out.println("Wrong option"); break;
        }
    }

    static void bookRoom(int roomType) {
        int roomIndex = findAvailableRoom(roomType);
        if (roomIndex != -1) {
            enterCustomerDetails(roomType, roomIndex);
            System.out.println("Room Booked");
        } else {
            System.out.println("No rooms available!");
        }
    }

    static int findAvailableRoom(int roomType) {
        int startIndex = 0, endIndex = 0;

        switch (roomType) {
            case 1: startIndex = 0; endIndex = hotelData.luxuryDoubleRooms.length; break;
            case 2: startIndex = 0; endIndex = hotelData.deluxeDoubleRooms.length; break;
            case 3: startIndex = 0; endIndex = hotelData.luxurySingleRooms.length; break;
            case 4: startIndex = 0; endIndex = hotelData.deluxeSingleRooms.length; break;
            default: System.out.println("Enter valid option"); return -1;
        }

        for (int j = startIndex; j < endIndex; j++) {
            if ((roomType == 1 && hotelData.luxuryDoubleRooms[j] == null) || 
                (roomType == 2 && hotelData.deluxeDoubleRooms[j] == null) ||
                (roomType == 3 && hotelData.luxurySingleRooms[j] == null) ||
                (roomType == 4 && hotelData.deluxeSingleRooms[j] == null)) {
                return j;
            }
        }
        return -1; // No available room
    }

    static void displayFeatures(int roomType) {
        switch (roomType) {
            case 1: System.out.println("Luxury Double Room: AC: Yes, Free breakfast: Yes, Charge per day: 4000"); break;
            case 2: System.out.println("Deluxe Double Room: AC: No, Free breakfast: Yes, Charge per day: 3000"); break;
            case 3: System.out.println("Luxury Single Room: AC: Yes, Free breakfast: Yes, Charge per day: 2200"); break;
            case 4: System.out.println("Deluxe Single Room: AC: No, Free breakfast: Yes, Charge per day: 1200"); break;
            default: System.out.println("Enter valid option"); break;
        }
    }

    static void checkAvailability(int roomType) {
        int count = 0;
        switch (roomType) {
            case 1: for (DoubleRoom room : hotelData.luxuryDoubleRooms) if (room == null) count++; break;
            case 2: for (DoubleRoom room : hotelData.deluxeDoubleRooms) if (room == null) count++; break;
            case 3: for (SingleRoom room : hotelData.luxurySingleRooms) if (room == null) count++; break;
            case 4: for (SingleRoom room : hotelData.deluxeSingleRooms) if (room == null) count++; break;
            default: System.out.println("Enter valid option"); return;
        }
        System.out.println("Number of rooms available: " + count);
    }

    static void generateBill(int roomIndex, int roomType) {
        double amount = 0;
        String[] foodItems = {"Sandwich", "Pasta", "Noodles", "Coke"};

        switch (roomType) {
            case 1: amount += 4000; break;
            case 2: amount += 3000; break;
            case 3: amount += 2200; break;
            case 4: amount += 1200; break;
            default: System.out.println("Invalid room type"); return;
        }

        System.out.println("\n*******");
        System.out.println(" Bill:-");
        System.out.println("*******");
        System.out.println("Room Charge - " + amount);

        System.out.println("\nFood Charges:- ");
        System.out.println("===============");
        System.out.println("Item   Quantity    Price");
        System.out.println("-------------------------");

        ArrayList<Food> foodOrders = (roomType <= 2) ? hotelData.luxuryDoubleRooms[roomIndex].foodOrders : hotelData.luxurySingleRooms[roomIndex].foodOrders;
        
        for (Food food : foodOrders) {
            amount += food.price;
            System.out.printf("%-10s%-10s%-10s%n", foodItems[food.itemNo - 1], food.quantity, food.price);
        }

        System.out.println("\nTotal Amount: " + amount);
    }

    static void deallocateRoom(int roomIndex, int roomType) {
        char confirm;
        switch (roomType) {
            case 1:
                if (hotelData.luxuryDoubleRooms[roomIndex] != null) {
                    System.out.println("Room used by " + hotelData.luxuryDoubleRooms[roomIndex].name);
                } else {
                    System.out.println("Room is already empty");
                    return;
                }
                break;
            case 2:
                if (hotelData.deluxeDoubleRooms[roomIndex] != null) {
                    System.out.println("Room used by " + hotelData.deluxeDoubleRooms[roomIndex].name);
                } else {
                    System.out.println("Room is already empty");
                    return;
                }
                break;
            case 3:
                if (hotelData.luxurySingleRooms[roomIndex] != null) {
                    System.out.println("Room used by " + hotelData.luxurySingleRooms[roomIndex].name);
                } else {
                    System.out.println("Room is already empty");
                    return;
                }
                break;
            case 4:
                if (hotelData.deluxeSingleRooms[roomIndex] != null) {
                    System.out.println("Room used by " + hotelData.deluxeSingleRooms[roomIndex].name);
                } else {
                    System.out.println("Room is already empty");
                    return;
                }
                break;
            default: System.out.println("Invalid room type"); return;
        }

        System.out.println("Do you want to free this room? (y/n): ");
        confirm = scanner.next().charAt(0);
        if (confirm == 'y') {
            switch (roomType) {
                case 1: hotelData.luxuryDoubleRooms[roomIndex] = null; break;
                case 2: hotelData.deluxeDoubleRooms[roomIndex] = null; break;
                case 3: hotelData.luxurySingleRooms[roomIndex] = null; break;
                case 4: hotelData.deluxeSingleRooms[roomIndex] = null; break;
            }
            System.out.println("Room deallocated successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    public static void main(String[] args) {
        int choice;
        while (true) {
            System.out.println("1. Book Room");
            System.out.println("2. Check Availability");
            System.out.println("3. Generate Bill");
            System.out.println("4. Deallocate Room");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Select room type: \n1. Luxury Double Room\n2. Deluxe Double Room\n3. Luxury Single Room\n4. Deluxe Single Room");
                    int roomType = scanner.nextInt();
                    displayFeatures(roomType);
                    bookRoom(roomType);
                    break;
                case 2:
                    System.out.println("Select room type: \n1. Luxury Double Room\n2. Deluxe Double Room\n3. Luxury Single Room\n4. Deluxe Single Room");
                    roomType = scanner.nextInt();
                    checkAvailability(roomType);
                    break;
                case 3:
                    System.out.println("Select room type: \n1. Luxury Double Room\n2. Deluxe Double Room\n3. Luxury Single Room\n4. Deluxe Single Room");
                    roomType = scanner.nextInt();
                    System.out.print("Enter room number: ");
                    int roomIndex = scanner.nextInt();
                    generateBill(roomIndex, roomType);
                    break;
                case 4:
                    System.out.println("Select room type: \n1. Luxury Double Room\n2. Deluxe Double Room\n3. Luxury Single Room\n4. Deluxe Single Room");
                    roomType = scanner.nextInt();
                    System.out.print("Enter room number: ");
                    roomIndex = scanner.nextInt();
                    deallocateRoom(roomIndex, roomType);
                    break;
                case 5:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }
}
