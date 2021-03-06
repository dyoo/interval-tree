package org.arabidopsis.interval;

import junit.framework.TestCase;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class TestIntervalTree extends TestCase {
    private IntervalTree tree;

    public void setUp() {
	this.tree = new IntervalTree();
    }

    public void tearDown() {
	this.tree = null;
    }


    public void testEmptyCase() {
	assertEquals(null, this.tree.search(new Interval(0, 42)));
    }


    public void testSimpleOverlapCaseOnEqual() {
	tree.insert(new Interval(1, 5));
	assertEquals(new Interval(1, 5), this.searchInterval(1, 5));
    }


    public void testSimpleOverlapCaseOnLeft() {
	tree.insert(new Interval(1, 5));
	assertEquals(new Interval(1, 5), this.searchInterval(0, 3));
    }


    public void testSimpleOverlapCaseOnRight() {
	tree.insert(new Interval(1, 5));
	assertEquals(new Interval(1, 5), this.searchInterval(3, 6));
    }

    public void testSimpleOverlapCaseOnEnclosed() {
	tree.insert(new Interval(1, 5));
	assertEquals(new Interval(1, 5), this.searchInterval(2, 3));
    }


    public void testSimpleOverlapCaseOnSurrounding() {
	tree.insert(new Interval(1, 5));
	assertEquals(new Interval(1, 5), this.searchInterval(0, 6));
    }


    public void testSimpleUnsuccessfulMatchOnRight() {
	tree.insert(new Interval(1, 5));
	assertEquals(null, this.searchInterval(6, 10));
    }


    public void testSimpleUnsuccessfulMatchOnLeft() {
	tree.insert(new Interval(2, 5));
	assertEquals(null, this.searchInterval(0, 1));
    }


    private Interval searchInterval(int low, int high) {
	return tree.search(new Interval(low, high));
    }



    private List searchAllIntervals(int low, int high) {
	List intervals = tree.searchAll(new Interval(low, high));
	java.util.Collections.sort(intervals);
	return intervals;
    }



    // The following test data comes from CLR's "Introduction to
    // Algorithms", on the chapter of Red-Black trees.
    private void prepareTestCaseTree() {
	tree.insert(new Interval(0, 3));
	tree.insert(new Interval(5, 8));
	tree.insert(new Interval(6, 10));
	tree.insert(new Interval(8, 9));
	tree.insert(new Interval(15, 23));
	tree.insert(new Interval(16, 21));
	tree.insert(new Interval(17, 19));
	tree.insert(new Interval(19, 20));
	tree.insert(new Interval(25, 30));
	tree.insert(new Interval(26, 26));
    }


    // Just a quick function to make sure that the checkValidity
    // function is up and running.  isValid() should ALWAYS be true,
    // unless I screwed up with the implementation somehow.
    public void testValidityOfTestCaseTree() {
	assertTrue(tree.isValid());
	prepareTestCaseTree();
	assertTrue(tree.isValid());
    }



    // check to see that duplicate intervals work out ok structurally.
    public void testExpectedSizeWithDuplicates() {
	prepareTestCaseTree();
	int n = tree.size();

	int someNumber = 20;
	for (int i = 0; i < someNumber; i++) {
	    prepareTestCaseTree();
	}
	assertEquals(n * (1 + someNumber),
		     tree.size());
    }



    public void testWithPreparedTree() {
	prepareTestCaseTree();
	assertEquals(new Interval(0, 3), searchInterval(-1, 4));
	assertEquals(new Interval(15, 23), searchInterval(15, 15));
	assertEquals(null, searchInterval(11, 14));
	assertEquals(new Interval(25, 30), searchInterval(24, 25));
    }


    public void testMultipleSearch() {
	prepareTestCaseTree();
	List expected = new ArrayList();
	expected.add(new Interval(0, 3));
	expected.add(new Interval(5, 8));
	expected.add(new Interval(6, 10));
	assertEquals(expected, searchAllIntervals(1, 7));
    }


    public void testUnsuccessfulMultipleSearch() {
	prepareTestCaseTree();
	List expected = new ArrayList();
	assertEquals(expected, searchAllIntervals(11, 14));
    }


    public void testSingleMatchWithMultipleSearch() {
	prepareTestCaseTree();
	List expected = new ArrayList();
	expected.add(new Interval(25, 30));
	assertEquals(expected, searchAllIntervals(24, 25));
    }



    // If we double up each interval, we expect to see the same elements
    public void testSingleMatchWithMultipleSearchAndDuplicates() {
	prepareTestCaseTree();
	prepareTestCaseTree();
	prepareTestCaseTree();
	List expected = new ArrayList();
	expected.add(new Interval(25, 30));
	expected.add(new Interval(25, 30));
	expected.add(new Interval(25, 30));
	assertEquals(expected, searchAllIntervals(24, 25));
    }




    public void testSearchAllOnWholeResults() {
	List expected = new ArrayList();
	int bignumber = 30000;
	for(int i = 0; i < bignumber; i++) {
	    tree.insert(new Interval(i, i));
	    expected.add(new Interval(i, i));
	}

	assertTrue(tree.isValid());

	assertEquals("unexpected unequality", 
		     expected, searchAllIntervals(0, bignumber-1));

	assertEquals("unexpected unequality", 
		     expected, searchAllIntervals(0, 2*bignumber));

	assertEquals("unexpected unequality", 
		     expected, searchAllIntervals(0, 3*bignumber));

	assertEquals("unexpected unequality", 
		     new ArrayList(), searchAllIntervals(-1, -1));
    }



    public void testWithLotsOfOverlapping() {
	int bignumber = 1000;
	for(int i = 0; i < bignumber; i++) {
	    tree.insert(new Interval(0, i));
	}
	assertTrue(tree.isValid());

	/* At this point, the tree has something that looks like
         -
         --
         ---
         ----
         ...
	 */

	for(int i = 0; i < bignumber; i++) {
	    assertEquals(i, 
			 searchAllIntervals(bignumber - i, bignumber).size());
	}
    }
}
