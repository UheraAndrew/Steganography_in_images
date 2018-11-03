package main;

import exceptions.LackOfSizeException;
import exceptions.ToLongMessageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

public class Controller {

    public void incode(byte[] message, String pathToImage) throws IOException, LackOfSizeException, ToLongMessageException {
        hasSpace(message, pathToImage);
        BufferedImage bufferedImage = ImageIO.read(new File(pathToImage));
        embed(message, bufferedImage);
        save(bufferedImage, pathToImage);
    }

    private boolean hasSpace(byte[] message, String pathToImage) throws IOException, LackOfSizeException, ToLongMessageException {
        System.out.println(pathToImage);
        BufferedImage bimg = ImageIO.read(new File(pathToImage));
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        if (message.length > 0xff) {
            throw new ToLongMessageException("Message is more than 256 bytes");
        }
        if ((width * height - 1) * 2 < message.length * 8) {
            throw new LackOfSizeException("Image is yo small for this picture");
        }

        return Boolean.TRUE;
    }

    private boolean embed(byte[] message, BufferedImage bufImage) {
        int width, height;
        width = bufImage.getWidth();
        int counter = 0;
        // set numbers of bytes in message
        {
            int rgb = bufImage.getRGB(0, 0);
            // reset first byte
            rgb = rgb & 0xFF00FFFF;//0000 0000 1111 1111 1111 1111
            rgb = rgb | message.length << 16;
            bufImage.setRGB(0, 0, rgb);
        }

        for (int i = 0; i < message.length * 4; ) {
            int x = ((i / 3) + 1) % width;
            int y = ((i / 3) + 1) / width;
            // reset places to insert
            int mask = 0xfffcfcfc;//1111 1111 1111 1100 1111 1100 1111 1100
            int rgb = bufImage.getRGB(x, y);
            rgb = rgb & mask;
            //  form data for current pixel
            int in = 0;
            for (int k = 0; k < 3; k++) {
                in = in << 8;
                if (i / 4 >= message.length) {
                    continue;
                }
                in = in | (message[i / 4] >>> (3 - i % 4) * 2) & 3;
                i++;
            }
            rgb = rgb | in;
            bufImage.setRGB(x, y, rgb);
        }

        return Boolean.TRUE;
    }

    private boolean save(BufferedImage bufferedImage, String pathToFile) throws IOException {
        File outputfile = new File(pathToFile);
        String[] format = pathToFile.split("\\.");
        System.out.println(Arrays.toString(format));

        ImageIO.write(bufferedImage, format[format.length - 1], outputfile);
        return Boolean.TRUE;
    }

    private byte[] exstract(BufferedImage bimg) {
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        int size = ((bimg.getRGB(0, 0)) & 0x00ff0000) >>> 16;

        byte[] message = new byte[size];

        for (int i = 0; i < message.length * 4; ) {
            int x = ((i / 3) + 1) % width;
            int y = ((i / 3) + 1) / width;

            int in = bimg.getRGB(x, y);
            for (int k = 0; k < 3; k++) {
                if (i / 4 >= message.length) {
                    continue;
                }
                message[i / 4] = (byte) (message[i / 4] << 2);
                message[i / 4] = (byte) (message[i / 4] | ((in >> ((2 - k) * 8)) & 3));
                i++;
            }
        }
        return message;
    }

    public String decode(String pathToImage) throws IOException {
        BufferedImage bimg = ImageIO.read(new File(pathToImage));
        byte[] message = exstract(bimg);
        return bytesToString(message);
    }

    private String bytesToString(byte[] message) {
        StringBuilder b = new StringBuilder(message.length);
        for (int i = 0; i < message.length; i++) {
            b.replace(i, i + 1, (char) message[i] + " ");
        }
        return b.toString();
    }


}
