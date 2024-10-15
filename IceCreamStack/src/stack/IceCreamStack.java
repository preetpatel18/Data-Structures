package stack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.*;

/*
 * CS112 Stack Lab
 * 
 * Implement push() and pop() using an array based 
 * stack of strings to implement the ice cream display
 * 
 * @author Colin Sullivan 
 */
public class IceCreamStack {

    // Array representing a stack. Index 0 is the bottom of the stack.
    private String[] iceCreamScoops;
    // First open spot in the stack array
    private int next;

    /*
     * Default constructor for an empty stack with a capacity of 3
     */
    public IceCreamStack() {
        iceCreamScoops = new String[3];
        next = 0;
    }

    /*
     * Resize method which will double the capacity of the stack
     * and Copy all elements
     * 
     * 1) Create a new String array, with its size double the current  
     * iceCreamScoops array size
     * 
     * 2) For each index in iceCreamScoops, copy the item at that index 
     * to the same index in the new doubled array
     * 
     * 3) Set the class attribute (this.iceCreamScoops) equal to your 
     * new doubled array
     */
    public void resize() {
        // WRITE YOUR CODE HERE
        String[] newArrayString = new String[iceCreamScoops.length*2];
        for (int i = 0; i < iceCreamScoops.length; i++){
            newArrayString[i] = iceCreamScoops[i];
        }
        iceCreamScoops = newArrayString;
    }

    /*
     * Given a String flavor, push it onto the Stack
     * 
     * The class variable "next" is equivalent to the first
     * empty spot in the stack array.
     * 
     * Therefore, next-1 is the top of the stack
     * 
     * 1) If the stack is full (next == array length), call resize() to double it
     * 2) Place the string at 'next' index in the array, and increment next by one.
     */
    public void push(String flavor) {
        // WRITE YOUR CODE HERE
        if(next == iceCreamScoops.length){
            resize();
        }
        iceCreamScoops[next++] = flavor;
    }

    /*
     * Pop and return the top item of the stack
     * 
     * 1) If the stack is empty (next <= 0), return null
     * 2) Else, decrement next and remove the item at that index, then
     * return that item
     */
    public String pop() {
        if (next <= 0) {
            return null;
        }
        String flavor = iceCreamScoops[--next];
        return flavor;
    }

    /*
     * Since the array is zero-indexed, next == numScoops
     * 
     * @returns number of scoops
     */
    public int getScoops() {
        return next;
    }

    /*
     * ////////////////////////////////////////////////////////////////
     * Everything past here runs the driver, no need to modify anything
     * ////////////////////////////////////////////////////////////////
     */

    private class IceCreamPanel extends JPanel {
        private IceCreamPanel() {
            super();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int coneX = iceCreamDisplay.getWidth() / 2;
            int coneY = 280;
            for (int scoop = next - 1; scoop >= 0; scoop--) {
                Color c = (iceCreamScoops[scoop].equals("chocolate")) ? (new Color(168, 125, 70))
                        : ((iceCreamScoops[scoop].equals("strawberry")) ? (new Color(255, 153, 255))
                                : (new Color(250, 250, 232)));
                g.setColor(c);
                g.fillOval(coneX - 50, (coneY - 110) - 100, 100, 100);
                coneY += 50;
            }
            g.setColor(new Color(124, 74, 9));
            int coneXPoints[] = { coneX, coneX - 50, coneX + 50 };
            int coneYPoints[] = { coneY, coneY - 190, coneY - 190 };
            g.fillPolygon(coneXPoints, coneYPoints, coneXPoints.length);
            this.setPreferredSize(new Dimension(300, (next * 50) + 400));
        }
    }

    private IceCreamPanel iceCreamDisplay;

    public String[] getScoopArray() {
        return this.iceCreamScoops;
    }

    public void Driver() {

        JFrame display = new JFrame("Ice Cream Stack Lab");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

        iceCreamDisplay = new IceCreamPanel();
        JScrollPane scroll = new JScrollPane(iceCreamDisplay, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
        JPanel button = new JPanel();
        JPanel line = new JPanel();
        JLabel numberScoops = new JLabel("Number of Scoops: " + this.getScoops());
        JButton addVanilla = new JButton("Add Vanilla");
        addVanilla.addActionListener((ActionEvent e) -> {
            this.push("vanilla");
            int scoop = this.getScoops();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numberScoops.setText("Number of Scoops: " + scoop);
                    iceCreamDisplay.revalidate();
                    iceCreamDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });
        JButton addChocolate = new JButton("Add Chocolate");
        addChocolate.addActionListener((ActionEvent e) -> {
            this.push("chocolate");
            int scoop = this.getScoops();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numberScoops.setText("Number of Scoops: " + scoop);
                    iceCreamDisplay.revalidate();
                    iceCreamDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });
        JButton addStrawberry = new JButton("Add Strawberry");
        addStrawberry.addActionListener((ActionEvent e) -> {
            this.push("strawberry");
            int scoop = this.getScoops();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numberScoops.setText("Number of Scoops: " + scoop);
                    iceCreamDisplay.revalidate();
                    iceCreamDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });
        JButton eatScoop = new JButton("Eat Scoop");
        eatScoop.addActionListener((ActionEvent e) -> {
            System.out.println(this.pop());
            int scoop = this.getScoops();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numberScoops.setText("Number of Scoops: " + scoop);
                    iceCreamDisplay.revalidate();
                    iceCreamDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });
        line.add(numberScoops);
        button.add(addVanilla);
        button.add(addChocolate);
        button.add(addStrawberry);
        button.add(eatScoop);

        button.setPreferredSize(new Dimension(300, 50));
        controls.add(button);
        line.setPreferredSize(new Dimension(300, 10));
        controls.add(line);
        controls.setPreferredSize(new Dimension(300, 100));
        window.add(controls);

        scroll.setPreferredSize(new Dimension(300, (next * 50) + 410));
        window.add(scroll);

        display.add(window);
        display.pack();
        display.setVisible(true);
    }

    public static void main(String args[]) {
        IceCreamStack stack = new IceCreamStack();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                stack.Driver();
            }
        });
    }

}
