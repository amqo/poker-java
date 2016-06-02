/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.gui;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.junit.runner.notification.RunListener.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
@ThreadSafe
public enum ImageManager {
    INSTANCE;
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageManager.class);
    public static final String IMAGES_PATH = "/images/";
    private final Map<String, Image> images = new HashMap<>();

    private ImageManager() {
    }

    public synchronized Image getImage(String imageFile) {
        System.out.println("Tratando de abrir " + imageFile);
        Image image = images.get(imageFile);
        if (image == null) {
            try {
                InputStream resourceStream = this.getClass().getResourceAsStream(imageFile);
                //System.out.println("Resource es " + resource);
                image = ImageIO.read(resourceStream);
                images.put(imageFile, image);
            } catch (IOException ex) {
                LOGGER.error("getImage \"" + imageFile + "\"", ex);
            }
        }
        return image;
    }
}
