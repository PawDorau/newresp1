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

    private final int NOTES_COUNT = 10;
    private final double BASE_ACCELERATION = 0.05;
    private final double RESULT_ACCELERATION = 0.005;

    private static final int pWIDTH = 924;
    private static final int pHEIGHT = 418;

    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    private static boolean running = false;

    private LinkedList<Note> notes = new LinkedList<>();
    private Note markedNote;

    private Random rnd = new Random();

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
        if ((randomNum > 0 && randomNum < 5) || (randomNum > 20 && randomNum < 25)) {
            noteColor = CZARNA;
        } else if ((randomNum > 4 && randomNum < 9) || (randomNum > 24 && randomNum < 29)) {
            noteColor = CZERWONA;
        } else if ((randomNum > 8 && randomNum < 13) || (randomNum > 28 && randomNum < 33)) {
            noteColor = NIEBIESKA;
        } else if ((randomNum > 12 && randomNum < 17) || (randomNum > 32 && randomNum < 37)) {
            noteColor = POMARANCZOWA;
        } else if ((randomNum > 16 && randomNum < 21) || (randomNum > 36 && randomNum < 41)) {
            noteColor = ZIELONA;
        }

        Note.nValue noteValue = CALANUTA;

        if ((randomNum == 1 || randomNum - 4 == 1 || randomNum - 8 == 1 || randomNum - 12 == 1)) {
            noteValue = CALANUTA;
        } else if ((randomNum == 2 || randomNum - 4 == 2 || randomNum - 8 == 2 || randomNum - 12 == 2)) {
            noteValue = POLNUTA;
        } else if ((randomNum == 3 || randomNum - 4 == 3 || randomNum - 8 == 3 || randomNum - 12 == 3)) {
            noteValue = CWIERCNUTA;
        } else if ((randomNum == 4 || randomNum - 4 == 4 || randomNum - 8 == 4 || randomNum - 12 == 4)) {
            noteValue = CALANUTA;
        } else if ((randomNum - 20 == 1 || randomNum - 24 == 1 || randomNum - 28 == 1 || randomNum - 32 == 1)) {
            noteValue = CALAPAUZA;
        } else if ((randomNum - 20 == 2 || randomNum - 24 == 2 || randomNum - 28 == 2 || randomNum - 32 == 2)) {
            noteValue = PAUZAPOLNUTOWA;
        } else if ((randomNum - 20 == 3 || randomNum - 24 == 3 || randomNum - 28 == 3 || randomNum - 32 == 3)) {
            noteValue = PAUZACWIERCNUTOWA;
        } else if ((randomNum - 20 == 4 || randomNum - 24 == 4 || randomNum - 28 == 4 || randomNum - 32 == 4)) {
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

    void start (){running = true;
    addNotify(); }

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

            g2d.setColor(Color.WHITE);
            g2d.drawImage(note.noteImg, getNotePositionX(note), getNotePositionY(note), null);

            Graphics2D g2s = (Graphics2D) g;

            //antyaliasing
            g2s.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2s.setFont(new Font("SansSerif", Font.BOLD, 25));
            g2s.setColor(Color.BLACK);
            g2s.drawString("WYNIK: "+(Integer.toString(result)),pWIDTH-100, 468);
            g2s.drawString((markedNote.noteColor.name() + " " + markedNote.noteValue.name()), 150, 468);
            g2s.drawRect(50,418,pWIDTH,84);

            if (note == markedNote) {
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
     *
     * Warto byloby dodac tutaj uzaleznienie od minietego czasu i na jego podstawie wyliczac przebyty dystans.
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

            if(result<0){
                result=0;
            }
            System.out.println("Buuu! Twój wynik: " + result);
            //TODO: Co jeżeli jest wynik < 0;
        }
    }

    /**
     * Wybieramy nowa losowa nutke - losujemy tak dlugo az bedzie wybrana inna niz dotychczas.
     */
    private void selectNewMarkedNote() {
        Note newMarkedNote = null;
        while (newMarkedNote == null) {
            newMarkedNote = notes.get(rnd.nextInt(notes.size()));
            if ((newMarkedNote == markedNote )) {
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
                System.out.println("Brawo! Twój wynik: " + result);
                result++;
                selectNewMarkedNote();
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
        return (int) (note.yCoords / 100 * pHEIGHT);
    }

    /**
     * Jak wyzej.
     */
    private int getNotePositionX(Note note) {
        return (int) (note.xCoords / 100 * pWIDTH);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
    

