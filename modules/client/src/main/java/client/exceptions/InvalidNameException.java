package client.exceptions;

public class InvalidNameException extends Exception{
    private final String str;
    public InvalidNameException(String str) {
        this.str = str;
    }
    @Override
    public String toString() {
        return str;
    }
}
