/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.gui;

import com.amqo.pokermaven.game.strategy.IStrategy;
import static com.amqo.pokermaven.gui.ImageManager.IMAGES_PATH;
import java.awt.Dimension;
import javax.swing.GroupLayout;

/**
 *
 * @author alberto
 */
public class TexasHoldEmView extends javax.swing.JFrame {

    private static final int WINDOW_HEIGHT = 800;
    private static final int WINDOW_WITH = 1280;
    private static final String WINDOW_TITLE = "J Poker";
    private static final String WINDOW_ICON = IMAGES_PATH + "poker-chip.png";
    private TexasHoldEmTablePanel jTablePanel;

    public TexasHoldEmView(IStrategy delegate) {
        initComponents();
        setTitle(WINDOW_TITLE);
        setIconImage(ImageManager.INSTANCE.getImage(WINDOW_ICON));
        setBounds(0, 0, WINDOW_WITH, WINDOW_HEIGHT);
        jTablePanel.setStrategy(delegate);
    }

    public IStrategy getStrategy() {
        return jTablePanel;
    }

    private void initComponents() {
        jTablePanel = new TexasHoldEmTablePanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jTablePanel.setPreferredSize(new Dimension(WINDOW_WITH, WINDOW_HEIGHT));
        javax.swing.GroupLayout jTablePanelLayout = new GroupLayout(jTablePanel);
        jTablePanel.setLayout(jTablePanelLayout);
        jTablePanelLayout.setHorizontalGroup(
                jTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, WINDOW_WITH, Short.MAX_VALUE)
        );
        jTablePanelLayout.setVerticalGroup(
                jTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, WINDOW_HEIGHT, Short.MAX_VALUE)
        );
        javax.swing.GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                        WINDOW_WITH, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                        WINDOW_HEIGHT, Short.MAX_VALUE)
        );
        pack();
    }
}
