package main;

import exceptions.LackOfSizeException;
import exceptions.ToLongMessageException;

import java.io.IOException;

public class Dispatcher {
    public static void main(String[] args) throws IOException, LackOfSizeException, ToLongMessageException {
       View v = new View();
        v.communication();
    }


}
