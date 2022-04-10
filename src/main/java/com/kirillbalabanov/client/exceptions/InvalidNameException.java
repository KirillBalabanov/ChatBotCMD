package com.kirillbalabanov.client.exceptions;

public class InvalidNameException extends Exception{
    String str;
    public InvalidNameException(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
