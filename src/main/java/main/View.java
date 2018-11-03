package main;

import exceptions.LackOfSizeException;
import exceptions.ToLongMessageException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class View {
    Scanner scan;

    public View() {
        scan = new Scanner(System.in);
    }

    public byte[] getMessage(InputStream in) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        ArrayList<Byte> temp = new ArrayList<>();
        byte b;
        while ((b = (byte) buf.read()) != -1 && (b != 10)) {
            System.out.println(b);
            temp.add(b);
        }
        buf.close();
        byte[] out = new byte[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            out[i] = temp.get(i);
        }
        return out;
    }

    public String askQuestion(String question, HashMap<String, String> posible_answers) {
        String answer;
        do {
            System.out.println(question + " " + posible_answers.toString() + ":");
            answer = scan.nextLine().trim().toLowerCase();
        }
        while (!posible_answers.keySet().contains(answer));

        return answer;
    }

    public void communication() throws IOException {
        Controller controller = new Controller();
        HashMap<String, String> posibl_answers = new HashMap<>();
        posibl_answers.put("in", "incode");
        posibl_answers.put("de", "decode");

        String action = askQuestion("What are you want?", posibl_answers);

        System.out.println("Path to Image:");
        String pathToImage = scan.nextLine();

        if (action.equals("in")) {
            byte[] message;
            System.out.println("get Message");
            message = getMessage(System.in);
            try {
                controller.incode(message, pathToImage);
            } catch (LackOfSizeException e) {
                e.printStackTrace();
            } catch (ToLongMessageException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(controller.decode(pathToImage));
        }
    }
}
