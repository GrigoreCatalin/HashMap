package RentalSystem;

import java.util.ArrayList;

public class RentedCars extends CarRentalSystem {
    private int totalRented;
    private String ownerName;
    private ArrayList<String> listOfCars = new ArrayList<>();

    public RentedCars(String ownerName, String plateNo) {
        this.ownerName = ownerName;
        addCars(plateNo);
    }

    public void addCars(String plateNo) {
        this.listOfCars.add(plateNo);
        this.totalRented++;
    }

    public int getTotalRented() {
        return totalRented;
    }

    public ArrayList<String> getListOfCars() {
        return listOfCars;
    }

    @Override
    public void returnCar(String plateNo) {
        listOfCars.remove(plateNo);
        totalRented--;
    }
}