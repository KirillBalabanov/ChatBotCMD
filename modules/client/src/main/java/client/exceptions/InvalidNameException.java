package client.exceptions;

public class InvalidNameException extends Exception{
    private String str;
    private String invalidSymbols;
    public InvalidNameException(String str) {
        this.invalidSymbols = null;
        this.str = str;
    }

    public InvalidNameException(String str, String invalidSymbols) {
        this.str = str;
        this.invalidSymbols = invalidSymbols;
    }

    @Override
    public String toString() {
        if(invalidSymbols != null) return str + "\n" + invalidSymbols;
        return str;
    }
}
