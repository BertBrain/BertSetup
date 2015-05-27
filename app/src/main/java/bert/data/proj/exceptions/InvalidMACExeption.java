package bert.data.proj.exceptions;

/**
 * Created by liamcook on 5/27/15.
 */
public class InvalidMACExeption extends Exception {

    public String message;

    public InvalidMACExeption() {
        super();
        message = "";
    }

    public InvalidMACExeption(String message){
        super();
        this.message = message;
    }
}
