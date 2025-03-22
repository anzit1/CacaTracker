package com.ct.cacatrackerproject.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CompressionImagen {

    public File resizeImagen(File originalImagenFichero, int anchoImagen, int alturaImagen, long tamanoMaxBytes) throws IOException {
    
        BufferedImage originalImage = ImageIO.read(originalImagenFichero);      
        Image scaledImage = originalImage.getScaledInstance(anchoImagen, alturaImagen, Image.SCALE_SMOOTH);
        
        BufferedImage bufferedImage = new BufferedImage(anchoImagen, alturaImagen, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
       
        File tempFile = File.createTempFile("resized_", ".jpg");
        tempFile.deleteOnExit();

        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            ImageIO.write(bufferedImage, "jpg", outputStream);
        }

        if (tempFile.length() > tamanoMaxBytes) {
            comprimeImagen(tempFile, tamanoMaxBytes);
        }
        
        return tempFile;
    }

    private void comprimeImagen(File imagenFile, long tamanoMaxBytes) throws IOException {

        File tempFile = imagenFile;
        long tamanoFoto = tempFile.length();

        while (tamanoFoto > tamanoMaxBytes) {

            BufferedImage originalImage = ImageIO.read(imagenFile);
            FileOutputStream fileOutputStream = new FileOutputStream(imagenFile);
            ImageIO.write(originalImage, "jpg", fileOutputStream);
            fileOutputStream.close();
            tamanoFoto = imagenFile.length();
        }
    }
}