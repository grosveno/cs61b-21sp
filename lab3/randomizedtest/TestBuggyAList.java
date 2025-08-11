package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> lst1 = new AListNoResizing<>();
        BuggyAList<Integer> lst2 = new BuggyAList<>();

        // Three Add
        lst1.addLast(4);
        lst1.addLast(5);;
        lst1.addLast(6);
        lst2.addLast(4);
        lst2.addLast(5);
        lst2.addLast(6);

        // Three remove
        assertEquals(lst1.removeLast(), lst2.removeLast());
        assertEquals(lst1.removeLast(), lst2.removeLast());
        assertEquals(lst1.removeLast(), lst2.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L1 = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();

        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L1.addLast(randVal);
                L2.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                assertEquals(L1.size(), L2.size());
            } else if(operationNumber == 2) {
                if (L1.size() <= 0) {
                    continue;
                }
                assertEquals(L1.removeLast(), L2.removeLast());
            } else {
                if (L1.size() <= 0) {
                    continue;
                }
                assertEquals(L1.getLast(), L2.getLast());
            }
        }
    }
}
