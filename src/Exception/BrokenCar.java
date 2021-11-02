package Exception;

public class BrokenCar extends Exception {
    public BrokenCar(String plateNo){
        super("Masina cu numarul de inmatriculare " + " este in service. Nu se poate inchiria");
    }
}
