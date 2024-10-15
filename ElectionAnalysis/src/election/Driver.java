package election;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

/*
 * Data visualization class for RU Election Analysis assignment
 * 
 * Displays the countrymap, with information about which states conducted elections in
 * certain years, which parties won, how voter participation has changed, as well as
 * text information about candidates in those elections. 
 * 
 * Driver based on Rising Tides visualization by Vian Miranda, which was inspirted
 * by the NIFTY assignment by Keith Scharz
 * 
 * @author Colin Sullivan
 */
public class Driver implements MouseInputListener {

    // ElectionAnalysis object containing vote data
    private ElectionAnalysis election;

    // Main Driver window
    private JFrame window;
    // US Election map visualizer
    private MapVisualizer map;

    // Upper mode control panel
    private JPanel mapControls;

    // Input for years (only 1 is displayed for all but change in votes)
    private JComboBox<Integer> yearInput;
    private JComboBox<Integer> year2Input;
    // Selector for mode
    private JComboBox<String> modeSelector;
    // Mouse coords text
    private JLabel coords;

    // Left side driver state panel
    private JPanel statePanel;

    // State info panel
    private JPanel stateInfo;
    private JLabel electionVotesLabel;
    // Current map display text info line
    private JLabel mapInfoLine;
    // "Select year" line - to change to "years"
    private JLabel yearLine;
    // Go button which sets map state
    private JButton goButton;

    // Selected State information panel
    private JLabel selectedState;
    private JLabel numberVotes;
    private JLabel averageVotes;
    private JLabel stateElectionsHeader;
    private JComboBox<String> stateElections;
    private JLabel avgVotesHeader;
    private JLabel totalVotesHeader;
    private JLabel changeInVotes;
    private JLabel changeInVotesHeader;
    private JPanel changeInVotesPanel;
    private JLabel numElectionsLabel;

    // Election info panel
    private JPanel electionInfo;
    // Race ID Label
    private JLabel raceIDLabel;
    private JLabel officeIDLabel;
    private JLabel electionHouseLabel;
    // Election candaidate selector dropdown
    private JComboBox<String> electionCandidates;
    private JLabel electionWinnerLabel;
    private JLabel numberCandidates;
    // Current selected Candidate panel
    private JPanel selectedCandidatePanel;
    private JLabel selectedCandidate;
    private JLabel candidatesVotes;
    private JLabel candidatesParty;

    // Hashmaps which contains all of the current reps for each office for the
    // selected year
    private HashMap<String, HashMap<Integer, String>> senate;
    private HashMap<String, HashMap<Integer, String>> house;

    /*
     * Creates driver window and populates it with panels
     * Uses class methods to create certain aspects of the window, and their
     * functions
     * 
     * Also creates ElectionAnalysis object, which reads election data.
     * The map will display nothing by default, until the display button is pressed.
     */
    private Driver() {
        // Create election analysis object (constructor reads in data)
        election = new ElectionAnalysis();

        try {
            election.readYears("house.csv");
            election.readYears("senate.csv");

            election.readStates("house.csv");
            election.readStates("senate.csv");

            election.readElections("senate.csv");
            election.readElections("house.csv");
        } catch (Exception e) {

        }

        senate = new HashMap<String, HashMap<Integer, String>>();
        house = new HashMap<String, HashMap<Integer, String>>();

        // Create Driver window
        window = new JFrame("Election Analysis");
        window.setLayout(new BorderLayout()); // NSEW Layout
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create upper control panel
        mapControls = makeControls();
        window.add(mapControls, BorderLayout.NORTH);

        // Create map visualizer
        map = new MapVisualizer();
        map.addMouseListener(this);
        map.addMouseMotionListener(this);
        map.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        window.add(map, BorderLayout.CENTER);

        makeInfoPanel();

        // Add window and display
        window.pack();
        window.setVisible(true);
    }

    /*
     * Creates and returns the upper control panel for the driver window
     * 
     * Contains buttons for House/Senate or Change/Total votes.
     * One of the four can be selected at one time.
     * Also has a year input for
     * 
     * @return JLabel containing the above buttons/jlabels
     */
    private JPanel makeControls() {
        // Create the upper info panel
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new FlowLayout());

        // Label for rear selector(s)
        mapInfoLine = new JLabel("Select a Mode and Year        ");
        yearPanel.add(mapInfoLine);

        yearLine = new JLabel("Select Year:");
        yearPanel.add(yearLine);

        // Creates first year input
        yearInput = new JComboBox<Integer>();
        for (Integer i : this.getYears()) {
            yearInput.addItem(i);
        }
        yearPanel.add(yearInput);

        // Creates second year input and hides it by default
        year2Input = new JComboBox<Integer>();
        for (Integer i : this.getYears()) {
            year2Input.addItem(i);
        }
        year2Input.setVisible(false);
        yearPanel.add(year2Input);

        // Creates mode selector input
        modeSelector = new JComboBox<String>();
        String[] modes = { "Total Votes", "Change in Votes", "Senate Elections", "House Elections" };
        for (String m : modes) {
            modeSelector.addItem(m);
        }
        modeSelector.addActionListener((ActionEvent e) -> {
            if (((String) modeSelector.getSelectedItem()).equals("Change in Votes")) {
                year2Input.setVisible(true);
                changeInVotesPanel.setVisible(true);
                yearLine.setText("\t     Select Years:");
            } else {
                year2Input.setVisible(false);
                changeInVotesPanel.setVisible(false);
                yearLine.setText("\t     Select Year:");
            }
        });
        yearPanel.add(new JLabel("Display Mode:"));
        yearPanel.add(modeSelector);

        // Creates the go button which reads selected info and displays simulation
        goButton = this.makeGoButton();
        yearPanel.add(goButton);

        // Create coordinate tracker, and set it default at (0,0)
        coords = new JLabel("(0,0)");
        yearPanel.add(coords);

        // Return info panel to driver
        return yearPanel;
    }

    /*
     * Creates the left side information panel containing info about
     * the map, states, and elections
     * 
     * Creates the right information panel for searching candidates
     */
    private void makeInfoPanel() {

        statePanel = new JPanel();
        statePanel.setLayout(new GridLayout(2, 1));

        stateInfo = makeStatePanel("NJ", 1998);
        statePanel.add(stateInfo);

        electionInfo = makeElectionPanel("NJ", 1998);
        statePanel.add(electionInfo);

        window.add(statePanel, BorderLayout.EAST);

    }

    /*
     * Creates a state information panel for the given year and state
     * 
     * Calls the student methods to gather information about the selected state and
     * displays
     * in the Driver
     */
    public JPanel makeStatePanel(String state, int year) {
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));

        int yr2;
        try {
            yr2 = (int) year2Input.getSelectedItem();
        } catch (Throwable t) {
            yr2 = 0;
        }

        // Create labels for selected state name
        JPanel stateName = new JPanel();
        JLabel stte = new JLabel("State: ");
        stte.setFont(new Font("Serif", Font.PLAIN, 15));
        stateName.add(stte);
        selectedState = new JLabel((state == null) ? ("None") : (state));
        selectedState.setFont(new Font("Serif", Font.PLAIN, 15));
        stateName.add(selectedState);
        info.add(stateName);

        // Total votes line
        JPanel totalVotes = new JPanel();
        totalVotesHeader = new JLabel("Total votes in " + state + " in " + year + ": ");
        totalVotesHeader.setFont(new Font("Serif", Font.PLAIN, 13));
        numberVotes = new JLabel("" + election.totalVotes(year, state));
        numberVotes.setFont(new Font("Serif", Font.PLAIN, 13));
        totalVotes.add(totalVotesHeader);
        totalVotes.add(numberVotes);

        // Average votes line
        JPanel avgVotes = new JPanel();
        avgVotesHeader = new JLabel("Avg Votes in " + state + " in " + year + ": ");
        avgVotesHeader.setFont(new Font("Serif", Font.PLAIN, 13));
        averageVotes = new JLabel("" + election.averageVotes(year, state));
        averageVotes.setFont(new Font("Serif", Font.PLAIN, 13));
        avgVotes.add(avgVotesHeader);
        avgVotes.add(averageVotes);

        // Change in votes line
        changeInVotesPanel = new JPanel();
        int yr = year;
        changeInVotesHeader = new JLabel("Change in Total Votes in " + state + " from " + yr2 + " to " + yr + ": ");
        changeInVotesHeader.setFont(new Font("Serif", Font.PLAIN, 13));
        changeInVotes = new JLabel("" + election.changeInTurnout(yr, yr2, state));
        changeInVotes.setFont(new Font("Serif", Font.PLAIN, 13));
        changeInVotesPanel.add(changeInVotesHeader);
        changeInVotesPanel.add(changeInVotes);

        info.add(totalVotes);
        info.add(avgVotes);
        changeInVotesPanel.setVisible(false);
        info.add(changeInVotesPanel);

        JPanel numElections = new JPanel();
        numElectionsLabel = new JLabel("Number of Elections: " + this.getStateElections(state, year).size());
        numElectionsLabel.setFont(new Font("Serif", Font.PLAIN, 13));
        numElections.add(numElectionsLabel);
        info.add(numElections);

        JPanel electionPane = new JPanel();
        electionPane.setLayout(new BoxLayout(electionPane, BoxLayout.PAGE_AXIS));
        stateElectionsHeader = new JLabel("Elections in " + state + " in " + year + ":       ");
        stateElectionsHeader.setFont(new Font("Serif", Font.PLAIN, 13));
        electionPane.add(stateElectionsHeader);
        stateElections = new JComboBox<String>();
        stateElections.addItem("Select an Election");
        for (ElectionNode e : this.getStateElections(state, year)) {
            StringBuilder sb = new StringBuilder();
            if (e.isSenate()) {
                sb.append("Senate,");
            } else {
                sb.append("House,");
            }
            sb.append(e.getRaceID() + ",");
            sb.append(e.getOfficeID());
            stateElections.addItem(sb.toString());
        }
        stateElections.addActionListener((ActionEvent e) -> {
            setElectionInfo();
        });

        electionPane.add(stateElections);
        info.add(electionPane);

        return info;
    }

    /*
     * Creates a election info panel for the selected election for the selected
     * state
     * 
     * @return JPanel election info panel
     */
    private JPanel makeElectionPanel(String state, int year) {
        JPanel elec = new JPanel();
        elec.setLayout(new BoxLayout(elec, BoxLayout.PAGE_AXIS));

        ArrayList<ElectionNode> elections = this.getStateElections(state, year);
        String selectedElec = (String) stateElections.getSelectedItem();
        ElectionNode f = null;
        if (selectedElec != null) {
            // House, raceid, office id
            String[] split = selectedElec.split(",");
            for (ElectionNode e : elections) {
                if (split.length < 3) {
                    break;
                }
                if (e.getRaceID() == Integer.parseInt(split[1])) {
                    f = e;
                    break;
                }
            }
        }
        ElectionNode found = f;

        // race_id,state_abbrev,office_id,
        // office_seat_name,cycle,candidate_name,ballot_party,votes,winner
        JPanel senateOrHouse = new JPanel();
        electionHouseLabel = new JLabel();
        try {
            if (found.isSenate()) {
                electionHouseLabel.setText("Senate Elections");
            } else {
                electionHouseLabel.setText("House Elections");
            }
        } catch (Throwable t) {
            electionHouseLabel.setText("null");
        }
        senateOrHouse.add(electionHouseLabel);
        elec.add(senateOrHouse);

        JPanel raceIDPan = new JPanel();
        try {
            raceIDLabel = new JLabel("Race ID: " + found.getRaceID());
        } catch (Throwable t) {
            raceIDLabel = new JLabel("Race ID: null");
        }
        try {
            officeIDLabel = new JLabel("Office ID: " + found.getOfficeID());
        } catch (Throwable t) {
            officeIDLabel = new JLabel("Office ID: null");
        }
        raceIDPan.add(raceIDLabel);
        raceIDPan.add(officeIDLabel);
        elec.add(raceIDPan);

        JPanel electionWinnerPanel = new JPanel();
        try {
            electionWinnerLabel = new JLabel("Winner: " + found.getWinner());
        } catch (Throwable t) {
            electionWinnerLabel = new JLabel("Winner: null");
        }
        electionWinnerPanel.add(electionWinnerLabel);
        elec.add(electionWinnerPanel);

        JPanel numCanPanel = new JPanel();
        try {
            numberCandidates = new JLabel("Number of Candidates: " + found.getCandidates().size());
        } catch (Throwable t) {
            numberCandidates = new JLabel("Number of Candidates: null");
        }
        numCanPanel.add(numberCandidates);
        elec.add(numCanPanel);

        JPanel numVotesPanel = new JPanel();
        try {
            electionVotesLabel = new JLabel("Total Votes: " + found.getVotes());
        } catch (Throwable t) {
            electionVotesLabel = new JLabel("Total Votes: null");
        }
        numVotesPanel.add(electionVotesLabel);
        elec.add(numVotesPanel);

        electionCandidates = new JComboBox<String>();
        electionCandidates.addItem("Select a Candidate");
        try {
            for (String s : found.getCandidates()) {
                electionCandidates.addItem(s);
            }
            electionCandidates.setSelectedItem(found.getWinner());
        } catch (Throwable t) {
        }
        elec.add(electionCandidates);
        electionCandidates.addActionListener((ActionEvent e) -> {
            setCandidateInfo();
        });

        selectedCandidatePanel = this.makeCandidatePanel();
        elec.add(selectedCandidatePanel);

        return elec;
    }

    /*
     * Sets the election panel to currently selected
     * helper method for the election list action listener
     */
    private void setElectionInfo() {
        String state = selectedState.getText();
        int year = (int) ((yearInput.getSelectedItem() != null) ? (yearInput.getSelectedItem()) : (0));

        ArrayList<ElectionNode> elections = this.getStateElections(state, year);
        String selectedElec = (String) stateElections.getSelectedItem();
        ElectionNode f = null;
        if (selectedElec != null) {
            try {
                // House, raceid, office id
                String[] split = selectedElec.split(",");
                for (ElectionNode e : elections) {
                    if (e.getRaceID() == Integer.parseInt(split[1])) {
                        f = e;
                        break;
                    }
                }
            } catch (Throwable t) {
            }
        }
        ElectionNode found = f;

        // race_id,state_abbrev,office_id,office_seat_name,cycle,candidate_name,ballot_party,votes,winner
        SwingUtilities.invokeLater(() -> {
            try {
                raceIDLabel.setText("RaceID: " + found.getRaceID());
            } catch (Throwable t) {
                raceIDLabel.setText("RaceID: null");
            }
        });
        SwingUtilities.invokeLater(() -> {
            try {
                officeIDLabel.setText("Office ID: " + found.getOfficeID());
            } catch (Throwable t) {
                officeIDLabel.setText("Office ID: null");
            }
        });

        SwingUtilities.invokeLater(() -> {
            try {
                if (found.isSenate()) {
                    electionHouseLabel.setText("Senate Elections");
                } else {
                    electionHouseLabel.setText("House Elections");
                }
            } catch (Throwable t) {
                electionHouseLabel.setText("null");
            }
        });

        SwingUtilities.invokeLater(() -> {
            try {
                electionWinnerLabel.setText("Winner: " + found.getWinner());
            } catch (Throwable t) {
                electionWinnerLabel.setText("Winner: null");
            }
        });

        SwingUtilities.invokeLater(() -> {
            try {
                electionVotesLabel.setText("Total Votes: " + found.getVotes());
            } catch (Throwable t) {
                electionVotesLabel.setText("Total Votes: null");
            }
        });

        SwingUtilities.invokeLater(() -> {
            try {
                numberCandidates.setText("Number of Candidates: " + found.getCandidates().size());
            } catch (Throwable t) {
                numberCandidates.setText("Number of Candidates: null");
            }
        });

        try {
            electionCandidates.removeAllItems();
        } catch (Exception npe) {
            // if at first you dont succeed, try try again!
            electionCandidates.removeAllItems();
        }

        electionCandidates.addItem("Select a Candidate");
        try {
            for (String s : found.getCandidates()) {
                electionCandidates.addItem(s);
            }
            electionCandidates.setSelectedItem(found.getWinner());
        } catch (Throwable t) {
        }

        setCandidateInfo();
    }

    private JPanel makeCandidatePanel() {
        JPanel canPanel = new JPanel();
        canPanel.setLayout(new BoxLayout(canPanel, BoxLayout.PAGE_AXIS));

        ElectionNode f = new ElectionNode(0, false, 0, null, null, -1, null);
        try {
            for (ElectionNode e : this.getStateElections(selectedState.getText(), (int) yearInput.getSelectedItem())) {
                if (e.getRaceID() == Integer.parseInt(raceIDLabel.getText().substring(9))) {
                    f = e;
                }
            }
        } catch (Throwable t) {
        }
        ElectionNode found = f;

        JPanel nameLine = new JPanel();
        selectedCandidate = new JLabel((String) electionCandidates.getSelectedItem());
        nameLine.add(selectedCandidate);
        canPanel.add(nameLine);

        JPanel partyLine = new JPanel();
        candidatesParty = new JLabel();
        SwingUtilities.invokeLater(() -> {
            try {
                String party = found
                        .getParty((selectedCandidate.getText() == null) ? ("") : (selectedCandidate.getText()));
                candidatesParty.setText(
                        ((party.equals("DEM") ? ("Democrat") : ((party.equals("REP") ? "Republican" : party)))));
            } catch (Throwable t) {
                candidatesParty.setText("No Party Found");
            }
        });
        partyLine.add(candidatesParty);
        canPanel.add(partyLine);

        JPanel canVotes = new JPanel();
        try {
            int votes = found.getVotes(selectedCandidate.getText());
            candidatesVotes = new JLabel("Votes Received: " + votes);
        } catch (Throwable t) {
            candidatesVotes = new JLabel("No Votes Found");
        }
        canVotes.add(candidatesVotes);
        canPanel.add(canVotes);

        canPanel.add(new JLabel("          "));
        canPanel.add(new JLabel("          "));
        return canPanel;
    }

    private void setCandidateInfo() {
        int year = (int) ((yearInput.getSelectedItem() != null) ? (yearInput.getSelectedItem()) : (0));
        ElectionNode f = null;
        String selectedElec = (String) stateElections.getSelectedItem();
        if (selectedElec != null) {
            try {
                // House, raceid, office id
                String[] split = selectedElec.split(",");
                for (ElectionNode e : this.getStateElections(selectedState.getText(), year)) {
                    if (e.getRaceID() == Integer.parseInt(split[1])) {
                        f = e;
                        break;
                    }
                }
            } catch (Throwable t) {
            }
        }
        ElectionNode found = f;

        SwingUtilities.invokeLater(() -> {
            try {
                selectedCandidate.setText((String) electionCandidates.getSelectedItem());
            } catch (Throwable t) {
            }
        });

        SwingUtilities.invokeLater(() -> {
            try {
                String party = found
                        .getParty((selectedCandidate.getText() == null) ? ("") : (selectedCandidate.getText()));
                candidatesParty.setText(
                        ((party.equals("DEM") ? ("Democrat") : ((party.equals("REP") ? "Republican" : party)))));
            } catch (Throwable t) {
                candidatesParty.setText("No Party Found");
            }
        });

        SwingUtilities.invokeLater(() -> {
            try {
                int votes = found.getVotes(selectedCandidate.getText());
                candidatesVotes.setText("Votes Received: " + votes);
            } catch (Throwable t) {
                candidatesVotes.setText("Votes Received: null");
            }
        });
    }

    /*
     * Runs each time the mouse is moved.
     * 
     * Updates the map coordinate selector display.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        try {
            PointerInfo a = MouseInfo.getPointerInfo();
            Point point = a.getLocation();
            SwingUtilities.convertPointFromScreen(point, map);
            int xScreenLocation = (int) (point.getX());
            int yScreenLocation = (int) (point.getY());

            String crds = "(" + xScreenLocation + ", " + yScreenLocation + ")";
            coords.setText(crds);
        } catch (NullPointerException n) {
        }
    }

    /*
     * 
     */
    private void setStatePanel(String state) {
        if (state != null) {
            int year;
            try {
                year = (int) yearInput.getSelectedItem();
            } catch (Throwable t) {
                year = 0;
            }
            int yr = year;
            int year2;
            try {
                year2 = (int) year2Input.getSelectedItem();
            } catch (Throwable t) {
                year2 = 0;
            }
            int yr2 = year2;

            SwingUtilities.invokeLater(() -> {
                selectedState.setText(((state == null) ? ("None") : (state)));
            });
            SwingUtilities.invokeLater(() -> {
                try {
                    totalVotesHeader.setText(("Total Votes in " + state + " in " + yr + ": "));
                    numberVotes.setText("" + election.totalVotes(yr, state));
                } catch (Throwable t) {
                    totalVotesHeader.setText(("Total Votes in " + state + " in " + yr + ": "));
                    numberVotes.setText("" + 0);
                }
            });
            SwingUtilities.invokeLater(() -> {
                try {
                    avgVotesHeader.setText(("Avg Votes in " + state + " in " + yr + ": "));
                    averageVotes.setText("" + election.averageVotes(yr, state));
                } catch (Throwable t) {
                    avgVotesHeader.setText(("Avg Votes in " + state + " in " + yr + ": "));
                    averageVotes.setText("0");
                }
            });
            SwingUtilities.invokeLater(() -> {
                int yar = yr;
                int yar2 = yr2;
                try {
                    changeInVotesHeader.setText(("Change in Votes from " + yar2 + " to " + yar + ": "));
                    changeInVotes.setText(("" + election.changeInTurnout(yar, yar2, state)));
                } catch (Throwable t) {
                    changeInVotesHeader.setText(("Change in Votes from " + yar2 + " to " + yar + ": "));
                    changeInVotes.setText(("0"));
                }
            });

            SwingUtilities.invokeLater(() -> {
                stateElectionsHeader.setText("Elections in " + state + " in " + yr + ":       ");
            });
            SwingUtilities.invokeLater(() -> {
                numElectionsLabel.setText("Number of Elections: " + this.getStateElections(state, yr).size());
            });

            try {
                stateElections.removeAllItems();
            } catch (Exception removeError) { // If at first you don't succeed, try, try again.
                stateElections.removeAllItems();
            }

            stateElections.addItem("Select an Election");
            for (ElectionNode elec : this.getStateElections(state, yr)) {
                StringBuilder sb = new StringBuilder();
                if (elec.isSenate()) {
                    sb.append("Senate,");
                } else {
                    sb.append("House,");
                }
                sb.append(elec.getRaceID() + ",");
                sb.append(elec.getOfficeID());
                stateElections.addItem(sb.toString());
            }

            setElectionInfo();
            setCandidateInfo();
        }
    }

    /*
     * Runs each time the mouse is moved.
     * 
     * Updates the map coordinate selector display.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            PointerInfo a = MouseInfo.getPointerInfo();
            Point point = a.getLocation();
            SwingUtilities.convertPointFromScreen(point, map);
            int xScreenLocation = (int) (point.getX());
            int yScreenLocation = (int) (point.getY());

            String state = map.state(xScreenLocation, yScreenLocation);

            setStatePanel(state);
        } catch (NullPointerException n) {
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /*
     * Creates button which reads selected data and updates display visualization
     * 
     * @return JButton which says "Go!" which will update the map visualization
     */
    private JButton makeGoButton() {
        var result = new JButton("Go!");
        result.addActionListener((ActionEvent e) -> {
            String mode = (String) modeSelector.getSelectedItem();
            if (mode.equals("Senate Elections")) {
                map.setColor(this.getMajorityMap(true));
                SwingUtilities.invokeLater(() -> {
                    try {
                        mapInfoLine.setText("Map of Senate Elections in " + (Integer) yearInput.getSelectedItem());
                    } catch (Throwable t) {
                        mapInfoLine.setText("Map of Senate Elections in null");
                    }
                });
            } else if (mode.equals("House Elections")) {
                map.setColor(this.getMajorityMap(false));
                SwingUtilities.invokeLater(() -> {
                    try {
                        mapInfoLine.setText("Map of House Elections in " + (Integer) yearInput.getSelectedItem());
                    } catch (Throwable t) {
                        mapInfoLine.setText("Map of House Elections in null");
                    }
                });
            } else if (mode.equals("Change in Votes")) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        int year = (int) ((yearInput.getSelectedItem() != null) ? (yearInput.getSelectedItem()) : (0));
                        int year2 = (int) ((yearInput.getSelectedItem() != null) ? (year2Input.getSelectedItem())
                                : (0));
                        map.setColor(this.getVotes(false), false);
                        mapInfoLine.setText("Change in votes from " + year + " to " + year2);
                    } catch (Throwable t) {
                        mapInfoLine.setText("Change in votes from null to null");
                    }
                });
            } else if (mode.equals("Total Votes")) {
                map.setColor(this.getVotes(true), true);
                SwingUtilities.invokeLater(() -> {
                    try {
                        mapInfoLine.setText("Total votes in " + (Integer) yearInput.getSelectedItem());
                    } catch (Throwable t) {
                        mapInfoLine.setText("Total votes in null");
                    }
                });
            }
            setStatePanel(selectedState.getText());
            map.repaint();
        });
        return result;
    }

    /*
     * Government methods
     * 
     * Simulates senate/house members by reading election data
     * Reads the simulation to return certain subsets of representatives
     */

    /*
     * Returns an ArrayList of eleciton nodes for elections in that state in that
     * year
     */
    private ArrayList<ElectionNode> getStateElections(String state, int year) {
        ArrayList<ElectionNode> elections = new ArrayList<>();
        YearNode yr = election.years();
        while (yr != null && yr.getYear() != year) {
            yr = yr.getNext();
        }
        if (yr == null) {
            return elections;
        }
        StateNode st = yr.getStates();
        if (st != null) {
            do {
                if (st.getStateName().equals(state)) {
                    ElectionNode e = st.getElections();
                    while (e != null) {
                        elections.add(e);
                        e = e.getNext();
                    }
                    return elections;
                }
                st = st.getNext();
            } while (st != yr.getStates());
        }
        return elections;
    }

    /*
     * Returns a map of the party who has a majority for each of the states
     * in the given branch of government for the year selected in the driver
     */
    private HashMap<String, String> getMajorityMap(boolean sen) {
        HashMap<String, String> col = new HashMap<>();
        int yr = (int) ((yearInput.getSelectedItem() != null) ? (yearInput.getSelectedItem()) : (0));
        this.generateHouseAndSenate(yr);
        HashMap<String, HashMap<Integer, String>> gov;
        if (sen) {
            gov = senate;
        } else {
            gov = house;
        }
        for (String state : gov.keySet()) {
            ArrayList<String> dems = this.getStateReps(state, "DEM", sen);
            ArrayList<String> reps = this.getStateReps(state, "REP", sen);
            if (dems.size() == reps.size() && dems.size() == 0) {
                continue;
            } else if (dems.size() >= reps.size()) {
                col.put(state, "DEM");
            } else {
                col.put(state, "REP");
            }
        }
        return col;
    }

    private ArrayList<String> getStateReps(String state, String party, boolean sen) {
        ArrayList<String> senators = new ArrayList<String>();
        HashMap<String, HashMap<Integer, String>> government = (sen) ? this.senate : this.house;
        for (String key : government.keySet()) {
            if (!key.equals(state)) {
                continue;
            }
            HashMap<Integer, String> seats = government.get(key);
            for (Integer k : seats.keySet()) {
                if (election.candidatesParty(seats.get(k)).toLowerCase().equals(party.toLowerCase())) {
                    senators.add(seats.get(k));
                }
            }
        }
        return senators;
    }

    private HashMap<String, Integer> getVotes(boolean total) {

        HashMap<String, Integer> h = new HashMap<>();
        if (senate != null) {
            senate.clear();
            this.generateHouseAndSenate(2000);
        }

        if (!total) {
            int yr;
            int yr2;
            try {
                yr = (int) yearInput.getSelectedItem();
                yr2 = (int) year2Input.getSelectedItem();
            } catch (Throwable t) {
                yr = 0;
                yr2 = 0;
            }
            int year = yr;
            int year2 = yr2;

            if (year == year2) {
                total = true;
            } else {
                for (String s : senate.keySet()) {
                    try {
                        h.put(s, election.changeInTurnout(year, year2, s));
                    } catch (Throwable t) {
                    }
                }
            }
        }
        if (total) {
            YearNode y = election.years();
            if (y == null) {
                return h;
            }
            while (y != null && y.getYear() != ((int) yearInput.getSelectedItem())) {
                y = y.getNext();
            }
            StateNode s = y.getStates();
            do {
                h.put(s.getStateName(), election.totalVotes(y.getYear(), s.getStateName()));
                s = s.getNext();
            } while (!s.equals(y.getStates()));
        }
        return h;
    }

    private ArrayList<Integer> getYears() {
        ArrayList<Integer> yearList = new ArrayList<>();
        YearNode ptr = election.years();
        while (ptr != null) {
            yearList.add(ptr.getYear());
            ptr = ptr.getNext();
        }
        return yearList;
    }

    private void generateHouseAndSenate(int year) {
        senate.clear();
        house.clear();

        YearNode ptr = election.years();
        while (ptr != null) {
            if (ptr.getYear() > year) {
                break;
            }
            StateNode st = ptr.getStates();
            do {
                if (st.getStateName().equals("PR")) {
                    st = st.getNext();
                    continue;
                }

                ElectionNode elect = st.getElections();
                while (elect != null) {
                    // Adds hashmap for current state's reps if it doesn't exist
                    if (elect.isSenate() && !senate.containsKey(st.getStateName())) {
                        senate.put(st.getStateName(), new HashMap<>());
                    }
                    if (!elect.isSenate() && !house.containsKey(st.getStateName())) {
                        house.put(st.getStateName(), new HashMap<>());
                    }

                    // If election is senate and office ID has been added
                    if (elect.isSenate()) {
                        senate.get(st.getStateName()).put(elect.getOfficeID(), elect.getWinner());
                    }
                    // If election is house and office ID has been added
                    else if (!elect.isSenate()) {
                        house.get(st.getStateName()).put(elect.getOfficeID(), elect.getWinner());
                    }
                    elect = elect.getNext();
                }
                st = st.getNext();

            } while (st != ptr.getStates());
            ptr = ptr.getNext();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Cannot set look and feel; falling back on default.");
            }
            new Driver();
        });
    }
}
