package singly;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class LinkedTrainCars {

    // Reference to front of the Singly Linked List 
    private TrainCar front;
    // TrainCar is a single node, containing a String name and a next field.  

    /*
     * Constructor which initialized the front of the train
     * list with the given string name
     * 
     * This is for the Driver, you do not need to use/modify this.
     */
    public LinkedTrainCars(String head) {
        front = new TrainCar();
        front.name = head;
    }

    /*
     * This method should traverse SLL starting at front and count
     * the number of TrainCar nodes in the list. Then return that number.
     * 
     * @returns the number of train cars (nodes) currently in the list
     */
    public int numCars() {
        // WRITE YOUR CODE HERE
        int numCar = 0;
        TrainCar ptr = front;
        while(ptr!=null){
            ptr = ptr.getNext();
            numCar++;
        }
        return numCar;
    }

    /*
     * Inserts the a train node with the given name at the given
     * index in the list, where the front node of the list is index 1.
     * i.e. The front node is index 1, second node is index 2.
     * 
     * @param add The string to add in a new node
     * @param index The index to add the node at
     */
    public void insertAt(String add, int index) {
        // WRITE YOUR CODE HERE
        TrainCar car = new TrainCar();
        car.setName(add);
        if(front==null){
            front = car;
        }else if(index == 1){
            TrainCar temp = front;
            front = car;
            front.next = temp;
        }else {
            TrainCar ptr1 = front;
            TrainCar prev = new TrainCar();
            int num = 1;
            while(num!=index && ptr1!=null){
                prev = ptr1;
                ptr1 = ptr1.getNext();
                num++;
            }
            prev.next=car;
            car.next = ptr1;
        }
    }

    /*
     * Removes and returns the name of the first found instance
     * of the target if it exists, or returns null if it doesn't
     * 
     * @param target String target to remove/return
     * @return target if it exists, null if it doesn't
     */
    public String remove(String target) {
        TrainCar ptr = front;
        if (ptr == null) {
            return null;
        } else if (ptr.name.equals(target)) {
            front = front.next;
            return target;
        }
        while (ptr.next != null) {
            if (ptr.next.name.equals(target)) {
                ptr.next = ptr.next.next;
                return target;
            }
            ptr = ptr.next;
        }
        return null;
    }

    /*
     * Removes and returns the item at the given index if it
     * exists, or returns null if it doesn't
     * 
     * @param index The index to remove
     * @return String in the node at that index, or null if none
     */
    public String remove(int index) {
        if (index == 1 && front != null) {
            String temp = front.name;
            front = front.next;
            return temp;
        }
        int i = 2;
        TrainCar ptr = front;
        while (ptr != null && ptr.next != null) {
            if (i == index) {
                String temp = ptr.next.name;
                ptr.next = ptr.next.next;
                return temp;
            }
            ptr = ptr.next;
            i++;
        }
        return null;
    }

    /*
     * @returns the front of the train car SLL
     */
    public TrainCar getFront() {
        return this.front;
    }

    public LinkedTrainCars() {
        
    }

    // Everything past here runs the driver, no need to modify anything

    private class TrainCarDisplay extends JPanel {
        private TrainCarDisplay() {
            super();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int[] carDim = { 120, 80 };
            int trainY = (this.getHeight() / 2) - 80;
            int currX = 15;
            int position = 1;
            for (TrainCar ptr = front; ptr != null; ptr = ptr.next) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(currX, trainY, carDim[0], carDim[1]);
                g.setColor(Color.WHITE);
                g.drawString(ptr.name, currX + 35, trainY + 45);
                g.drawString("" + position++, currX + 5, trainY + 15);
                int wheelX = currX;
                for (int j = 0; j < 4; j++) {
                    g.setColor(Color.BLACK);
                    g.fillOval(wheelX, trainY + carDim[1], carDim[0] / 4, carDim[1] / 3);
                    g.setColor(Color.WHITE);
                    g.fillOval(wheelX + 3, trainY + carDim[1] + 3, carDim[0] / 4 - 6, carDim[1] / 3 - 6);
                    wheelX += carDim[0] / 4;
                }
                if (ptr != front) {
                    g.setColor(Color.DARK_GRAY);
                    g.drawLine(currX, trainY + (3 * carDim[1]) / 4, currX - 15, trainY + (3 * carDim[1]) / 4);
                    g.drawLine(currX, trainY + (3 * carDim[1]) / 4 - 5, currX - 15, trainY + (3 * carDim[1]) / 4 - 5);

                    g.drawLine(currX, trainY + (3 * carDim[1]) / 4, currX - 6, trainY + (3 * carDim[1]) / 4 + 5);
                    g.drawLine(currX, trainY + (3 * carDim[1]) / 4 - 5, currX - 6, trainY + (3 * carDim[1]) / 4 - 10);
                }
                currX += carDim[0] + 15;
            }
            int panelWidth = (15 + (position * (carDim[0] + 25)) < 450) ? (450) : (15 + (position * (carDim[0] + 25)));
            this.setPreferredSize(new Dimension(panelWidth, 175));
        }
    }

    private TrainCarDisplay trainDisplay;
    private static String[] possibleCars = { "Engine", "Coal", "Passenger", "Flat", "Box", "Tank", };

    public void Driver() {

        JFrame display = new JFrame("Linked Train List Lab");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

        trainDisplay = new TrainCarDisplay();
        JScrollPane scroll = new JScrollPane(trainDisplay, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));

        JPanel text = new JPanel();
        JLabel numberCars = new JLabel("Number Cars: " + this.numCars());
        JLabel lastRemoved = new JLabel("Last Removed: null");
        text.add(numberCars);
        text.add(lastRemoved);

        JPanel buttons = new JPanel();
        JComboBox<String> carsBox = new JComboBox<>();
        for (int i = 0; i < possibleCars.length; i++) {
            carsBox.addItem(possibleCars[i]);
        }

        JSpinner indexBox = new JSpinner(new SpinnerNumberModel(this.numCars() + 1, 1, this.numCars() + 1, 1));
        JButton addCar = new JButton("Add Car");
        addCar.addActionListener((ActionEvent e) -> {
            int index = (int) indexBox.getValue();
            String chosen = (String) (carsBox.getSelectedItem());
            this.insertAt(chosen, index);

            int cars = this.numCars();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numberCars.setText("Number Cars: " + cars);
                    indexBox.setModel(new SpinnerNumberModel(cars + 1, 1, cars + 1, 1));
                    trainDisplay.revalidate();
                    trainDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });

        JButton removeCarIndex = new JButton("Remove Index");
        removeCarIndex.addActionListener((ActionEvent e) -> {
            int index = (int) indexBox.getValue();
            String removed = this.remove(index);
            int cars = this.numCars();
            int selected = (cars - 1 > 0) ? ((removed != null) ? (cars) : (cars + 1)) : (1);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    indexBox.setModel(new SpinnerNumberModel(selected, 1, cars + 1, 1));
                    numberCars.setText("Number Cars: " + cars);
                    lastRemoved.setText("Last Removed: " + removed);
                    trainDisplay.revalidate();
                    trainDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });

        JButton removeCarName = new JButton("Remove Car");
        removeCarName.addActionListener((ActionEvent e) -> {
            String chosen = (String) (carsBox.getSelectedItem());
            String removed = this.remove(chosen);
            int cars = this.numCars();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    indexBox.setModel(new SpinnerNumberModel(cars + 1, 1, cars + 1, 1));
                    numberCars.setText("Number Cars: " + cars);
                    lastRemoved.setText("Last Removed: " + removed);
                    trainDisplay.revalidate();
                    trainDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });

        buttons.add(addCar);
        buttons.add(carsBox);
        buttons.add(indexBox);
        buttons.add(removeCarIndex);
        buttons.add(removeCarName);
        buttons.setPreferredSize(new Dimension(525, 40));

        controls.add(buttons);
        controls.add(text);
        controls.setPreferredSize(new Dimension(525, 60));

        window.add(controls);
        window.add(scroll);

        display.add(window);
        display.setPreferredSize(new Dimension(525, 200));
        display.setResizable(false);
        display.pack();
        display.setVisible(true);
    }

    public static void main(String args[]) {
        LinkedTrainCars list = new LinkedTrainCars("Engine");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                list.Driver();
            }
        });
    }

}