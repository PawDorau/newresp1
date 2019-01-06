/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onenotegame;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * @author pdora
 */
public class Task extends JTextField {

    public enum Color {CZERWONA, POMARANCZOWA, NIEBIESKA, ZIELONA, CZARNA}

    public enum Value {
        CALANUTA, POLNUTA, CWIERCNUTA, OSEMKA, SZESNASTKA,
        CALAPAUZA, PAUZAPOLNUTOWA, PAUZACWIERCNUTOWA, PAUZAOSEMKOWA, PAUZASZESNASTKOWA
    }

    public static ArrayList<Color> ColorList = new ArrayList<Color>();
    public static ArrayList<Value> ValueList = new ArrayList<Value>();

    public Task() {

        Font font = new Font("SansSerif", Font.BOLD, 17);
        this.setText("");
        this.setPreferredSize(new Dimension(300, 75));
        this.setEditable(false);
        this.setBackground(java.awt.Color.WHITE);
        this.setHorizontalAlignment(JTextField.CENTER);
        this.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
        this.setFont(font);


        ColorList.add(Color.CZERWONA);
        ColorList.add(Color.POMARANCZOWA);
        ColorList.add(Color.NIEBIESKA);
        ColorList.add(Color.ZIELONA);
        ColorList.add(Color.CZARNA);

        ValueList.add(Value.CALANUTA);
        ValueList.add(Value.POLNUTA);
        ValueList.add(Value.CWIERCNUTA);
        ValueList.add(Value.OSEMKA);
        ValueList.add(Value.SZESNASTKA);
        ValueList.add(Value.CALAPAUZA);
        ValueList.add(Value.PAUZAPOLNUTOWA);
        ValueList.add(Value.PAUZACWIERCNUTOWA);
        ValueList.add(Value.PAUZAOSEMKOWA);
        ValueList.add(Value.PAUZASZESNASTKOWA);

        genTask();
    }

    public static String genTask() {
        Random rand1 = new Random();
        int randomColor = rand1.nextInt(5);
        Color genColor = ColorList.get(randomColor);
        String genStgColor = ColorList.get(randomColor).name();

        Random rand2 = new Random();
        int randomValue = rand2.nextInt(10);
        Value genValue = ValueList.get(randomValue);
        String genStgValue = ValueList.get(randomValue).name();

        return (genStgColor + " " + genStgValue);
    }
}
