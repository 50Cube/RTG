package rtg.exceptions;

public class PointNotFoundException extends Exception {

    static final public String KEY = "Nie znaleziono takiego punktu";

    public PointNotFoundException() {
        super(KEY);
    }
}
