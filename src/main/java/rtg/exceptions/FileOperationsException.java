package rtg.exceptions;

public class FileOperationsException extends Exception {

    static final public String KEY = "Wystapil problem z operacjami na pliku";

    public FileOperationsException() {
        super(KEY);
    }
}
