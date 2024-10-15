package wqu;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * CS112 Weighted Quick Union Lab
 * 
 * Implement the WQU constructor in order to enable the color 
 * memorization game display
 * 
 * @author Colin Sullivan
 */
public class ColorGameWQU {

    // Parent array of coordinate values
    public Coordinate[][] parent;
    // Size array for each corresponding parent
    public int[][] size;

    /*
     * Constructor which initializes a Weighted Quick Union object
     * 
     * First, Instantiate the parent and size arrays to [row][col] size of
     * their corresponding types.
     * 
     * Then, set every [row][col] cell index in parent[][] equal to a new Coordinate
     * object which holds that cell's row and column value. And set every [row][col]
     * cell index in size[][] equal to 1.
     * 
     * Then, the weighted quick union class will enable the game display to work,
     * using the union and find methods to power the color selection on click
     */
    public ColorGameWQU(int rows, int cols) {
        // WRITE YOUR CODE HERE
        this.parent = new Coordinate[rows][cols];
        this.size = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                parent[i][j] = new Coordinate(i, j);
                size[i][j] = 1;
            }
        }
    }

    public void union(Coordinate n1, Coordinate n2) {
        // DO NOT MODIFY
        Coordinate r2 = find(n1);
        Coordinate r1 = find(n2);
        if (r1.equals(r2)) {
            return;
        } else {
            if (size[r2.row][r2.col] < size[r1.row][r1.col]) {
                Coordinate swap = r1;
                r1 = r2;
                r2 = swap;
            }
            parent[r1.row][r1.col] = r2;
            size[r2.row][r2.col] += size[r1.row][r1.col];
        }
    }

    public Coordinate find(Coordinate search) {
        // DO NOT MODIFY
        if (search.col < 0 || search.row < 0) {
            if (search.row >= parent.length || search.col >= parent[0].length) {
                return null;
            }
        }
        if (parent[search.row][search.col].equals(search)) {
            return search;
        } else {
            parent[search.row][search.col] = find(parent[search.row][search.col]);
        }
        return parent[search.row][search.col];
    }

    // Getter for parent array
    public Coordinate[][] getParent() {
        return parent;
    }

    // Getter for size array
    public int[][] getSize() {
        return size;
    }

    // Everything past here runs the driver, no need to modify anything

    private class ColorGameDisplay extends JPanel {
        private BufferedImage background;
        // 336 Rows 321 Cols

        private Color gameRed;
        private Color gameGreen;
        private Color gameBlue;
        private Color gameYellow;

        private ColorGameDisplay() {
            super();
            try {
                background = ImageIO.read(new File("./src/wqu/ColorGame.jpg"));
                gameRed = new Color(background.getRGB(RED.col, RED.row));
                gameGreen = new Color(background.getRGB(GREEN.col, GREEN.row));
                gameBlue = new Color(background.getRGB(BLUE.col, BLUE.row));
                gameYellow = new Color(background.getRGB(YELLOW.col, YELLOW.row));
            } catch (Exception io) {
                System.out.println(io.getMessage());
                System.out.println(
                        "Error Reading ColorGame.jpg image.");
            }
            setupDisplay();
            this.setPreferredSize(new Dimension(background.getWidth(), background.getHeight()));
        }

        private void setupDisplay() {

            for (int r = 0; r < background.getHeight(); r++) {
                for (int c = 0; c < background.getWidth(); c++) {
                    Color temp = new Color(background.getRGB(c, r));
                    if (temp.equals(gameRed)) {
                        union(RED, new Coordinate(r, c));
                    } else if (temp.equals(gameBlue)) {
                        union(BLUE, new Coordinate(r, c));
                    } else if (temp.equals(gameGreen)) {
                        union(GREEN, new Coordinate(r, c));
                    } else if (temp.equals(gameYellow)) {
                        union(YELLOW, new Coordinate(r, c));
                    }
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, this);
            String selectedname = (displayedColor != null) ? (displayedColor) : ("");
            Color selectedColor = (selectedname.equals("RED") ? (gameRed)
                    : ((selectedname.equals("BLUE")) ? (gameBlue)
                            : (selectedname.equals("GREEN")) ? (gameGreen)
                                    : (selectedname.equals("YELLOW")) ? (gameYellow) : (null)));
            g.setColor(selectedColor);
            g.fillOval(-1 + (background.getWidth() / 3), 2 + (background.getHeight() / 3), 105, 105);
        }
    }

    private ColorGameDisplay gameDisplay;
    private JButton play;
    private JLabel textLine;
    private JLabel highScore;
    private String displayedColor;
    volatile private int inputSelect;
    volatile private int read = -1;
    final Coordinate RED = new Coordinate(97, 235);
    final Coordinate GREEN = new Coordinate(86, 92);
    final Coordinate BLUE = new Coordinate(250, 231);
    final Coordinate YELLOW = new Coordinate(232, 93);
    final String[] colors = { "RED", "GREEN", "BLUE", "YELLOW" };
    private String[] answered;

    public void playGame() {
        String[] correct = new String[15];
        answered = new String[15];
        for (int index = 0; index < 15; index++) {
            int rand = (int) (Math.random() * 4);
            correct[index] = colors[rand];
            while (index > 0 && correct[index].equals(correct[index - 1])) {
                rand = (int) (Math.random() * 3);
                correct[index] = colors[rand];
            }
        }
        for (int max = 1; max < 20; max++) {
            for (int round = 0; round < max; round++) {
                int select = round;
                displayedColor = correct[select];
                gameDisplay.revalidate();
                gameDisplay.repaint();
                try {
                    Thread.sleep(800);
                    displayedColor = null;
                    gameDisplay.revalidate();
                    gameDisplay.repaint();
                } catch (InterruptedException ie) {
                    System.out.println(ie.getMessage());
                }
            }

            inputSelect = 0;
            read = -1;
            long startWaitTime = System.currentTimeMillis();
            while (inputSelect < max) {
                long currTime = System.currentTimeMillis();
                long seconds = currTime - startWaitTime;
                if (read == inputSelect) {
                    if (!correct[inputSelect].equals(answered[inputSelect])) {
                        inputSelect = 0;
                        read = -1;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                textLine.setText("You Lose!");
                                play.setVisible(true);
                            }
                        });
                        return;
                    }
                    startWaitTime = System.currentTimeMillis();
                    inputSelect++;
                } else if (seconds > 2500) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            textLine.setText("You Lose!");
                            play.setVisible(true);
                        }
                    });
                    return;
                }
            }
            answered = new String[15];
            if (max > Integer.parseInt(highScore.getText())) {
                int high = max;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        highScore.setText("" + high);
                    }
                });
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                textLine.setText("You Win!");
                play.setVisible(true);
            }

        });
    }

    public void Driver() {

        JFrame display = new JFrame("Color Display WQU Lab");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

        JPanel controls = new JPanel();
        gameDisplay = new ColorGameDisplay();

        play = new JButton("Play!");
        textLine = new JLabel("");
        highScore = new JLabel("0");

        play.addActionListener((ActionEvent e) -> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    textLine.setText("");
                    play.setVisible(false);
                }
            });
            new Thread(new Runnable() {
                public void run() {
                    playGame();
                }
            }).start();
        });
        gameDisplay.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    PointerInfo a = MouseInfo.getPointerInfo();
                    Point point = a.getLocation();
                    SwingUtilities.convertPointFromScreen(point, gameDisplay);
                    int xScreenLocation = (int) (point.getX());
                    int yScreenLocation = (int) (point.getY());

                    Coordinate found;
                    try {
                        found = find(new Coordinate(yScreenLocation, xScreenLocation));
                    } catch (Exception er) {
                        found = null;
                    }

                    String selectedColor = (found.equals(RED) ? ("RED")
                            : ((found.equals(BLUE)) ? ("BLUE")
                                    : (found.equals(GREEN)) ? ("GREEN")
                                            : (found.equals(YELLOW)) ? ("YELLOW") : (null)));
                    if (selectedColor != null && inputSelect - 1 == read) {
                        answered[read + 1] = selectedColor;
                        read++;
                    }

                } catch (NullPointerException n) {
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

        });

        controls.add(new JLabel("High Score: "));
        controls.add(highScore);
        controls.add(play);
        controls.add(textLine);

        window.add(controls);
        window.add(gameDisplay);

        display.add(window);
        display.pack();
        display.setResizable(false);
        display.setVisible(true);
    }

    public static void main(String args[]) {
        ColorGameWQU simon = new ColorGameWQU(331, 326);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                simon.Driver();
            }
        });
    }

}
