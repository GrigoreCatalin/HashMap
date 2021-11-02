package Exception;

public class ForbiddenName extends Exception {
    public ForbiddenName(String name) {
        super(name + " este pe lista neagra.");
    }
}
