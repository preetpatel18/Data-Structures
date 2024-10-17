import static org.junit.Assert.*;
import org.junit.*;

import election.*;

/*
* This is a Java Test Class, which uses the JUnit package to create
* and run tests
* 
* You can use this to evaluate your code. Examine these tests, as writing
* similar test cases will help you immensly on other Assignments/Labs, as
* well as moving forward in your CS career.
*/
public class ElectionAnalysisTest {

    // INPUT FILE FORMAT:
    // RaceID, State, OfficeID, Senate/House, YEAR, CANDIDATENAME, Party, VotesReceived, Winner (true/false)
    private static final String testFile = "testInput.csv";

    @Test
    public void testReadYears() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);

        // By Default, the test input csv contains three unique years (1, 2, and 6)
        // So we will traverse the years list and check they appear in the specified order
        YearNode ptr = test.years();
        assertTrue(ptr != null);
        assertTrue(ptr.getYear() == 2003);
        ptr = ptr.getNext();
        assertTrue(ptr.getYear() == 2011);
        ptr = ptr.getNext();
        assertTrue(ptr.getYear() == 2009);

        // We will also check that there are no extra nodes
        assertTrue(ptr.getNext() == null);
    }


    @Test
    public void testReadStates() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);

        int[] years = {2003, 2011, 2009};
        String[][] statesInYears = {{"A1"}, {"A2"}, {"B3", "B5"}};
        int year = 0;
        for (YearNode yr = test.years(); yr != null; yr = yr.getNext()) {
            assertTrue(yr.getYear() == years[year]);
            int state = 0;
            StateNode st = yr.getStates().getNext(); // Starts at front of list
            do {
                assertTrue(st.getStateName().equals(statesInYears[year][state]));
                state++;
                st = st.getNext();
            } while ( st != yr.getStates().getNext());
            assertEquals(state, statesInYears[year].length);
            year++;
        }
        
    }

    @Test
    public void testReadElections() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        test.readElections(testFile);

        // Once you complete this test, remove this fail() statement
        // You can manually check each value (as in testReadYears()) or you can use arrays to check
        // the values (as in testReadStates())
    }

    @Test
    public void testAvgVotes() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        test.readElections(testFile);
        
        // Once you complete this test, remove this fail() statement

        // You should test to see if the average votes for any given state/year from the test
        // input file is what it should be
    }

    @Test
    public void testTotalVotes() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        test.readElections(testFile);

        // Once you complete this test, remove this fail() statement

        // You should test to see if the total votes for any given state/year from the test
        // input file is what it should be
    }

    @Test
    public void testCandidatesParty() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        test.readElections(testFile);

        // Once you complete this test, remove this fail() statement

        // You should test to see if the party for any given candidate from the test
        // input file is what it should be
    }
}