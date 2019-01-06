/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onenotegame;

import java.awt.image.*;

/**
 * @author pdora
 */



public class Note {

    public enum nColor {CZERWONA, POMARANCZOWA, NIEBIESKA, ZIELONA, CZARNA}

    public enum nValue {
        CALANUTA, POLNUTA, CWIERCNUTA, OSEMKA, SZESNASTKA,
        CALAPAUZA, PAUZAPOLNUTOWA, PAUZACWIERCNUTOWA, PAUZAOSEMKOWA, PAUZASZESNASTKOWA
    }

    int id;

    int width;
    int height;

    double xCoords;
    double yCoords;

    BufferedImage noteImg;

    nColor noteColor;
    nValue noteValue;


    public Note(int id, int width, int height, double xCoords, double yCoords, BufferedImage noteImg, nColor noteColor, nValue noteValue) {
        this.xCoords = xCoords;
        this.yCoords = yCoords;
        this.id = id;
        this.width = width;
        this.height = height;
        this.noteImg = noteImg;
        this.noteColor = noteColor;
        this.noteValue = noteValue;
    }

}
