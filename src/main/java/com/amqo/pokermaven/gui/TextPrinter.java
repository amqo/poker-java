/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 *
 * @author alberto
 */
public class TextPrinter {

    public enum VerticalAlign {
        TOP, MIDDLE, BOTTOM
    }

    public enum HorizontalAlign {
        LEFT, CENTER, RIGHT
    }
    private Font font;
    private Color color;
    private int width;
    private int height;
    private VerticalAlign vAlign = VerticalAlign.TOP;
    private HorizontalAlign hAlign = HorizontalAlign.LEFT;

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public VerticalAlign getVerticalAlign() {
        return vAlign;
    }

    public void setVerticalAlign(VerticalAlign vAlign) {
        this.vAlign = vAlign;
    }

    public HorizontalAlign getHorizontalAlign() {
        return hAlign;
    }

    public void setHorizontalAlign(HorizontalAlign hAlign) {
        this.hAlign = hAlign;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private int getOffSetX(int widthText) {
        int result = 0;
        if (hAlign == HorizontalAlign.CENTER) {
            result = (width - widthText) / 2;
        } else if (hAlign == HorizontalAlign.RIGHT) {
            result = width - widthText;
        }
        return result;
    }

    private int getOffSetY(int ascent, int descent) {
        int result = ascent;
        if (vAlign == VerticalAlign.MIDDLE) {
            result = (height + ascent - descent) / 2;
        } else if (vAlign == VerticalAlign.BOTTOM) {
            result = height - descent;
        }
        return result;
    }

    public void print(Graphics g, String text, int x, int y) {
        g.setColor(color);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);
        int widthText = fm.stringWidth(text);
        g.drawString(text,
                x + getOffSetX(widthText),
                y + getOffSetY(fm.getAscent(), fm.getDescent()));
    }
}
