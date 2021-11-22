package RentalSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

import Exception.ForbiddenName;
import Exception.BrokenCar;
public class CarRentalSystem implements Serializable {

    private static Scanner sc = new Scanner(System.in);
    private HashMap<String, String> rentedCars = new HashMap<String, String>(100, 0.5f);
    private RentedCars numberAndListCars;
    private int totalRented;
    private Hashtable<String, RentedCars> rentedCarsForOwners = new Hashtable<String, RentedCars>(100, 0.5f);

    private static final long serialVersionUID = 1L;

    public void writeToBinaryFile(HashMap<String, String> rentedCars, Hashtable<String, RentedCars> rentedCarsForOwners) throws IOException {
        try (ObjectOutputStream binaryFileOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("List Cars and Owners.dat")))) {
            binaryFileOut.writeObject(rentedCars);
        }
        try (ObjectOutputStream binaryFileOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("List Cars per Owner.dat")))) {
            binaryFileOut.writeObject(rentedCarsForOwners);
        }
    }

    public void readFromBinaryFile() throws IOException {
        try (ObjectInputStream binaryFileOutList = new ObjectInputStream(new BufferedInputStream(new FileInputStream("List Cars and Owners.dat")))) {
            this.rentedCars = (HashMap<String, String>) binaryFileOutList.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("A aparut o eroare: " + e.getMessage());
        }

        try (ObjectInputStream binaryFileOutWaitList = new ObjectInputStream(new BufferedInputStream(new FileInputStream("List Cars per Owner.dat")))) {
            this.rentedCarsForOwners = (Hashtable<String, RentedCars>) binaryFileOutWaitList.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("A aparut o eroare: " + e.getMessage());
        }
    }

    private String getPlateNo() {
        System.out.println("Introduceti numarul de inmatriculare:");
        return sc.nextLine();
    }

    private String getOwnerName() {
        System.out.println("Introduceti numele proprietarului:");
        return sc.nextLine();
    }

    public boolean isCarRent(String plateNo) {
        return rentedCars.containsKey(plateNo);
    }

    private String getCarRent(String plateNo) {
        if (!isCarRent(plateNo)) {
            return ("Autovehiculul nu exista sau nu este inchiriat.");
        }
        return rentedCars.get(plateNo);
    }

    public void rentCar(String plateNo, String ownerName) throws ForbiddenName, BrokenCar {
        if (isCarRent(plateNo)) {
            System.out.println("Autovehiculul este deja inchiriat.");
            return;
        }
            if (ownerName.equals("Mihai Dumitru") || (ownerName.equals("Dumitru Mihai"))) {
                throw new ForbiddenName(ownerName);
            } else if (plateNo.equals("B 100 VMS")) {
                throw new BrokenCar(plateNo);
            }

        rentedCars.put(plateNo, ownerName);
        checkOwners(plateNo, ownerName);
        totalRented++;
    }

    private void checkOwners(String plateNo, String ownerName) {

        if (rentedCarsForOwners.containsKey(ownerName)) {
            rentedCarsForOwners.get(ownerName).addCars(plateNo);
        } else {
            numberAndListCars = new RentedCars(ownerName, plateNo);
            rentedCarsForOwners.put(ownerName, numberAndListCars);
        }
    }

    public void returnCar(String plateNo) {
        if (isCarRent(plateNo)) {
            String carRemoved = rentedCars.remove(plateNo);
            System.out.println("Predarea autovehiculului s-a realizat cu succes.");
            totalRented--;
            rentedCarsForOwners.get(carRemoved).returnCar(plateNo);
            return;
        }
        System.out.println("Autovehiculul nu este inchiriat.");
    }

    private String getTotalRented() {
        return ("Numarul total de autovehicule inchiriate este " + totalRented);
    }

    private String getCarsNo(String ownerName) {
        return ("Numarul de autovehicule inchiriate este: " + rentedCarsForOwners.get(ownerName).getTotalRented());
    }

    private String getCarList(String ownerName) {
        return ("Lista de autovehicule inchiriate este: " + rentedCarsForOwners.get(ownerName).getListOfCars());
    }

    private void checkName(String ownerName) {
        if (rentedCarsForOwners.containsKey(ownerName)) {
            System.out.println(getCarsNo(ownerName));
            System.out.println(getCarList(ownerName));
            return;
        }
        System.out.println("Numele introdus este incorect");
    }

    private static void printCommandsList() {
        System.out.println("help         - Afiseaza aceasta lista de comenzi");
        System.out.println("add          - Adauga o noua pereche (masina, sofer)");
        System.out.println("check        - Verifica daca o masina este deja luata");
        System.out.println("remove       - Sterge o masina existenta din hashtable");
        System.out.println("getOwner     - Afiseaza proprietarul curent al masinii");
        System.out.println("totalRented  - Afiseaza numarul total de masini inchiriate");
        System.out.println("ownerOfCars  - Afiseaza numarul total si lista de autovehicule inchiriate");
        System.out.println("quit         - Inchide aplicatia");
    }

    public void run() {
        boolean quit = false;

        try {
            readFromBinaryFile();
        } catch (IOException e) {
            System.out.println("Deocamdata nu exista nicio varianta de back-up");
        }


        while (!quit) {
            System.out.println("Asteapta comanda: (help - Afiseaza lista de comenzi)");
            String command = sc.nextLine();
            switch (command) {
                case "help":
                    printCommandsList();
                    break;
                case "add":
                    try{
                    rentCar(getPlateNo(), getOwnerName());
                    } catch (ForbiddenName e) {
                        System.out.println(e.getMessage());
                    } catch (BrokenCar e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "check":
                    System.out.println(isCarRent(getPlateNo()));
                    break;
                case "remove":
                    returnCar(getPlateNo());
                    break;
                case "getOwner":
                    System.out.println(getCarRent(getPlateNo()));
                    break;
                case "totalRented":
                    System.out.println(getTotalRented());
                    break;
                case "ownerOfCars":
                    System.out.println("Introduceti numele proprietarului pentru a vedea numarul de masini si lista de autovehicule inchiriate.");
                    checkName(sc.nextLine());
                    break;
                case "quit":
                    try {
                       writeToBinaryFile(this.rentedCars, this.rentedCarsForOwners);
                    } catch (IOException e) {
                        System.out.println("S-a produs o eroare");
                    }

                    System.out.println("Aplicatia se inchide...");
                    quit = true;
                    break;
                default:
                    System.out.println("Unknown command. Choose from:");
                    printCommandsList();
            }
        }
    }

    public static void main(String[] args) {
        new CarRentalSystem().run();

    }
}