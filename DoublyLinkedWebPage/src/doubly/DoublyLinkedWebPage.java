package doubly;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * Implement the forward and back functionality of a web browser
 * using a doubly linked list
 * 
 * @author Colin Sullivan
 */
public class DoublyLinkedWebPage {

    private WebPage current;

    /*
     * Adds a new WebPage with the given String "toAdd" to the list.
     * 
     * This node should be inserted at AFTER current (aka as currents next),
     * and should DELETE any nodes that were originally after current.
     * This should also move the current pointer to that newly added WebPage.
     * 
     * You still need to handle the special case when the list is empty (current == null)
     * 
     * You do NOT need to use any loops to complete this method.
     * 
     * EX:  A <=> B <=> C <=> D with "current" pointing to B
     *      Then we call addRecent("E")
     *      A <=> B <=> E with "current" pointing to E
     */
    public void addRecent(String toAdd) {
        // WRITE YOUR CODE HERE
        if(current == null){
            current = new WebPage();
            current.website = toAdd;
        }else{
            WebPage node = new WebPage();
            node.website = toAdd;
            node.prev = current;
            current.next = node;
            current = current.next;
        }
    }

    /*
     * Returns the number of sites in the list.
     * 
     * Remember, this list is doubly linked, and the "current" can
     * be anywhere in the list. So you must traverse in both directions
     * to count all nodes.
     * 
     * @returns int     Number of WebPage nodes in the DLL
     */
    public int numberSites() {
        // WRITE YOUR CODE HERE
        if(current == null){
            return 0;
        }
        WebPage test  = current;
        int i = 0;
        while(test.next != null){
            i++;
            test = test.next;
        }
        test = current;
        while(test.prev != null){
            i++;
            test = test.prev;
        }

        return i+1; // Replace this line, it is only provided so the program compiles
    }

    /*
     * Moves the currently selected webpage node to the previous
     * node, if one exists
     */
    public void moveLeft() {
        // DO NOT EDIT
        if (current != null && current.prev != null) {
            current = current.prev;
        }
    }

    /*
     * Moves the currently selected webpage node to the next
     * node, if one exists
     */
    public void moveRight() {
        // DO NOT EDIT
        if (current != null && current.next != null) {
            current = current.next;
        }
    }

    /*
     * Returns the currently selected WebPage node
     */
    public WebPage getCurrent() {
        // DO NOT EDIT
        return this.current;
    }

    // Everything past here runs the driver, no need to modify anything

    private class WebPageDisplay extends JPanel {
        private BufferedImage currentSite;
        private final Dimension siteSize = new Dimension(500, 281);

        private WebPageDisplay() {
            super();
            this.setPreferredSize(siteSize);
        }

        private void setSite(String siteName) {
            // Set Panel's Site Background
            try {
                currentSite = ImageIO.read(new File("./sites/" + siteName + ".jpg"));
            } catch (Exception io) {
                System.out.println("Error Reading Current Site Image. Make sure you've opened the correct project folder (the 'DoublyLinkedWebPage' folder which contains 'src', 'bin', and 'test')");
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(currentSite, 0, 0, this);
        }

    }

    private WebPageDisplay browser;
    private static String[] possibleSites = { "AutoLab", "CS112", "School", "VidSite", "Search", "Place" };

    public void Driver() {

        JFrame display = new JFrame("Doubly Linked Webpage Lab");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

        browser = new WebPageDisplay();

        JPanel controls = new JPanel();
        JLabel numSites = new JLabel("Site: 0/0");
        JButton newSite = new JButton("Visit New Site");
        newSite.addActionListener((ActionEvent e) -> {
            int rand = (int) (Math.random() * (possibleSites.length));
            String chosen = possibleSites[rand];
            while (this.getCurrent() != null && this.getCurrent().website.equals(chosen)) {
                rand = (int) (Math.random() * (possibleSites.length));
                chosen = possibleSites[rand];
            }
            this.addRecent(chosen);
            int sites = this.numberSites();
            WebPage ptr = this.getCurrent();
            int before = 0;
            while (ptr != null) {
                before++;
                ptr = ptr.prev;
            }
            int count = before;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    browser.setSite(current.website);
                    numSites.setText("Site: " + count + "/" + sites);
                    browser.repaint();
                }
            });
        });
        JButton forward = new JButton("-->");
        forward.addActionListener((ActionEvent e) -> {
            if (current != null && current.next != null) {
                current = this.getCurrent().next;
                int sites = this.numberSites();
                WebPage ptr = this.getCurrent();
                int before = (ptr == null) ? (1) : (0);
                while (ptr != null) {
                    before++;
                    ptr = ptr.prev;
                }
                int count = before;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        browser.setSite(current.website);
                        numSites.setText("Site: " + count + "/" + sites);
                        browser.repaint();
                    }
                });
            }
        });
        JButton back = new JButton("<--");
        back.addActionListener((ActionEvent e) -> {
            if (this.getCurrent() != null && this.getCurrent().prev != null) {
                current = this.getCurrent().prev;
                int sites = this.numberSites();
                WebPage ptr = current;
                int before = (ptr == null) ? (1) : (0);
                while (ptr != null) {
                    before++;
                    ptr = ptr.prev;
                }
                int count = before;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        browser.setSite(current.website);
                        numSites.setText("Site: " + count + "/" + sites);
                        browser.repaint();
                    }
                });
            }
        });
        controls.add(back);
        controls.add(forward);
        controls.add(newSite);
        // controls.add(currentSite);
        controls.add(numSites);
        controls.setPreferredSize(new Dimension(500, 37));

        window.add(controls);
        window.add(browser);

        display.add(window);
        display.pack();
        display.setResizable(false);
        display.setVisible(true);
    }

    public static void main(String args[]) {
        DoublyLinkedWebPage worldwideweb = new DoublyLinkedWebPage();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                worldwideweb.Driver();
            }
        });
    }

}
