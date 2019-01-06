/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onenotegame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

import static onenotegame.Note.nColor.*;
import static onenotegame.Note.nValue.*;



/**
 * @author pdora
 */
public class Panel extends JPanel implements Runnable, KeyListener {

    private  int NOTES_COUNT = 5;
    private final int MAX_NOTES_COUNT = 15;
    private final double BASE_ACCELERATION = 0.04;
    private final double RESULT_ACCELERATION = 0.003;

    private static final int pWIDTH = 924;
    private static final int pHEIGHT = 418;

    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    private static boolean running = false;

    private LinkedList<Note> notes = new LinkedList<>();
    private Note markedNote;

    private Random rnd = new Random();

    //int[] yConPos = new int[4];

    private int lastNoteId = 0;
    private int result = 0;

    private int clickedX = -1;
    private int clickedY = -1;

    Image background;

    public Panel() {
        this.setPreferredSize(new Dimension(pWIDTH, pHEIGHT));
        this.setMaximumSize(new Dimension(pWIDTH, pHEIGHT));
        this.setMinimumSize(new Dimension(pWIDTH, pHEIGHT));
        this.setBounds(50, 50, pWIDTH, pHEIGHT);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setVisible(true);
        this.addKeyListener(this);

        try {
            background = ImageIO.read(getClass().getResource("/panelbg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickedX = e.getPoint().x;
                clickedY = e.getPoint().y;
            }
        });

        for (int i = 0; i < NOTES_COUNT; i++) {
            Note note = createNote();
            note.xCoords = note.xCoords + (i * 100.0 / NOTES_COUNT);
            notes.add(note);
        }
        markedNote = notes.getFirst();
    }

    /**
     * Tworzy nowa losowa nute.
     */
    private Note createNote() {
        //losuj img nuty/pauzy
        int min = 1, max = 40;

        int randomNum = rnd.nextInt((max - min) + 1) + min;

        BufferedImage noteImg = null;
        try {
            noteImg = ImageIO.read(getClass().getResource("/grafiki/notes/note" + randomNum + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //pobierz wymiary
        int noteX = noteImg.getHeight();
        int noteY = noteImg.getHeight();

        Note.nColor noteColor = Note.nColor.values()[rnd.nextInt(Note.nColor.values().length)];

        //przypis kolor
        if ((randomNum >= 1 && randomNum <= 4) || (randomNum >= 21 && randomNum <= 24)) {
            noteColor = CZARNA;
        } else if ((randomNum >= 5 && randomNum <= 8) || (randomNum >= 25 && randomNum <= 28)) {
            noteColor = CZERWONA;
        } else if ((randomNum >= 9 && randomNum <= 12) || (randomNum >= 29 && randomNum <= 32)) {
            noteColor = NIEBIESKA;
        } else if ((randomNum >= 13 && randomNum <= 16) || (randomNum >= 33 && randomNum <= 36)) {
            noteColor = POMARANCZOWA;
        } else if ((randomNum >= 17 && randomNum <= 20) || (randomNum >= 37 && randomNum <= 40)) {
            noteColor = ZIELONA;
        }

        Note.nValue noteValue = Note.nValue.values()[rnd.nextInt(Note.nValue.values().length)];

        if ((randomNum == 1 || randomNum  == 5 || randomNum  == 9 || randomNum  == 13 || randomNum == 17)) {
            noteValue = CALANUTA;
        } else if ((randomNum == 2 || randomNum  == 6 || randomNum  == 10 || randomNum  == 14 || randomNum == 18)) {
            noteValue = POLNUTA;
        } else if ((randomNum == 3 || randomNum  == 7 || randomNum  == 11 || randomNum  == 15 || randomNum == 19)) {
            noteValue = CWIERCNUTA;
        } else if ((randomNum == 4 || randomNum  == 8 || randomNum  == 12 || randomNum  == 16 || randomNum == 20)) {
            noteValue = OSEMKA;
        }

        if ((randomNum  == 21 || randomNum  == 25 || randomNum  == 29 || randomNum  == 33 || randomNum == 37)) {
            noteValue = CALAPAUZA;
        } else if ((randomNum  == 22 || randomNum  == 26 || randomNum  == 30 || randomNum  == 34 || randomNum == 38)) {
            noteValue = PAUZAPOLNUTOWA;
        } else if ((randomNum  == 23 || randomNum  == 27 || randomNum  == 31 || randomNum  == 35 || randomNum == 39)) {
            noteValue = PAUZACWIERCNUTOWA;
        } else if ((randomNum  == 24 || randomNum  == 28 || randomNum  == 32 || randomNum  == 36 || randomNum == 40)) {
            noteValue = PAUZAOSEMKOWA;
        }

        //Losujemy
        double xCords = 100.0;
        double yCords = Math.abs(rnd.nextDouble()) * 100;

        lastNoteId++;

        return new Note(lastNoteId, noteX, noteY, xCords, yCords, noteImg, noteColor, noteValue);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        running = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Glowna petla programu.
     */
    @Override
    public void run() {
        long start, elapsed, wait;

        while (running) {
            start = System.nanoTime();

            cycle();
            repaint();

            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000;

            if (wait <= 0) {
                wait = 5;
            }

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void start() {
        running = true;
        addNotify();
    }

    void stop() {
        running = false;
    }


    /**
     * Rysowanie wszystkiego.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Background
        g.drawImage(background, 0, 0, null);

        for (Note note : notes) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.WHITE);
            g2d.drawImage(note.noteImg, getNotePositionX(note), getNotePositionY(note), null);

            Graphics2D g2s = (Graphics2D) g;

            //antyaliasing
            g2s.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2s.setFont(new Font("SansSerif", Font.BOLD, 25));
            g2s.setColor(Color.BLACK);
            g2s.drawString("WYNIK: " + (Integer.toString(result)), pWIDTH - 100, 468);
            g2s.drawString((markedNote.noteColor.name() + " " + markedNote.noteValue.name()), 150, 468);
            g2s.drawRect(50, 418, pWIDTH, 84);

            if (note == markedNote) {
                //markedNote.noteImg = null;
                g2d.setColor(Color.BLUE);
            } else {
                g2d.setColor(Color.BLACK);
            }

        }

    }

    /**
     * Wszystkie obliczenia zwiazane z gra:
     * - obsluga klikniecia
     * - obliczenie nowej pozycji
     * - usuniecie tych nut ktore wyszly za ekran
     */
    private void cycle() {
        handleMouseClick();
        moveNotes();

        int removed = 0;
        boolean removedMarked = false;
        Iterator<Note> noteIterator = notes.iterator();
        while (noteIterator.hasNext()) {
            Note note = noteIterator.next();
            if (note.xCoords < 0) {
                noteIterator.remove();
                removed++;
                if (note == markedNote) {
                    markedNote.noteImg = null;
                    removedMarked = true;
                }
            }
        }
        for (int i = 0; i < removed; i++) {
            Note note = createNote();
            notes.add(note);
        }
        if (removedMarked) {
            selectNewMarkedNote();

            result--;

        System.out.println("Twój wynik: " + result);

            if (result < 0) {
                 result = 0;
            }
    }
    }

    /**
     * Wybieramy nowa losowa nutke - losujemy tak dlugo az bedzie wybrana inna niz dotychczas.
     */
    private void selectNewMarkedNote() {
        Note newMarkedNote = null;
        while (newMarkedNote == null) {
            newMarkedNote = notes.get(rnd.nextInt(notes.size()));
            if ((newMarkedNote == markedNote)&&(newMarkedNote.xCoords<=50)&&(newMarkedNote.xCoords>=0)) {
                newMarkedNote = null;
            }
        }
        markedNote = newMarkedNote;
    }

    /**
     * Oblicza nowa pozycje dla wszystkich nut.
     */
    private void moveNotes() {
        for (Note note : notes) {
            note.xCoords -= BASE_ACCELERATION + getResultAcceleration();
        }
    }

    /**
     * Sprawdza czy w miedzyczasie gracz nie kliknal - jezeli tak to obsluguje klikniecie
     */
    private void handleMouseClick() {
        if (clickedX != -1 && clickedY != -1) {
            int positionX = getNotePositionX(markedNote);
            int positionY = getNotePositionY(markedNote);
            if (clickedX > positionX && clickedX < (positionX + markedNote.width) &&
                    clickedY > positionY && clickedY < (positionY + markedNote.height)) {
                System.out.println("Twój wynik: " + result);
                result++;

                selectNewMarkedNote();
                addMoreNotes();
            }
            clickedY = -1;
            clickedX = -1;
        }
    }

    /**
     * Oblicza dodatkowe przyspieszenie ze wzgledu na wynik.
     */
    private double getResultAcceleration() {
        return result * RESULT_ACCELERATION;
    }

    /**
     * Oblicza pozycje, w nutach pozycja trzymana jest w postaci zmiennoprzecinkowiej od 0 do 100.
     * Dzieki czemu mozna szczegolowiej obliczyc pozycje (na przyklad w ciagu jednego cyklu nuta moze przesunac sie o 0.1 pixela)
     */
    private int getNotePositionY(Note note) {
        int yConPos = 0,  i = rnd.nextInt(4) + 1;

        if (note.id % 4 == 0) {
            if(note.width < 100){
                yConPos=250;
            }else yConPos = 50;
        } else if (note.id % 4 == 3) {
            yConPos = 60;
        } else if (note.id % 4 == 2) {
            if(note.width < 100){
                yConPos=200;
            }else yConPos = 50;
        } else if(note.id % 4 == 1) {
            yConPos = 80;
        }
            /*   /100 * pHEIGHT); */
            return (int) (note.yCoords = yConPos);
        }


    /**
     * Jak wyzej.
     */
    private int getNotePositionX(Note note) {
        return (int) (note.xCoords / 100 * pWIDTH);
    }

    private int addMoreNotes(){
        if(result%2==0){
            NOTES_COUNT ++;
        }

        if(NOTES_COUNT>=MAX_NOTES_COUNT){
            NOTES_COUNT=MAX_NOTES_COUNT;
        }
        return(NOTES_COUNT);
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int KeyCode = e.getKeyCode();
        if (KeyCode == KeyEvent.VK_SPACE) {
            start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
    

