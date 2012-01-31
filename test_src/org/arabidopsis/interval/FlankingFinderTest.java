package org.arabidopsis.interval;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class FlankingFinderTest {
    private FlankingFinder<String> finder;

    @Before
    public void setUp() {
	this.finder = new FlankingFinder<String>();
    }


    @Test
    public void testEmptyCase() {
	assertEquals(new ArrayList<String>(),
		     this.finder.flankingLeft(42, 1));
	assertEquals(new ArrayList<String>(),
		     this.finder.flankingRight(42, 1));
    }


    @Test
    public void testNoFinding() {
	this.finder.add("hello", 10, 20);
	assertEquals(new ArrayList<String>(),
		     this.finder.flankingLeft(9, 1));
	assertEquals(new ArrayList<String>(),
		     this.finder.flankingRight(21, 1));
    }


    @Test
    public void testSimpleFinding() {
	this.finder.add("hello", 10, 20);
	List<String> expected = new ArrayList<String>();
	expected.add("hello");
	assertEquals(expected,
		     this.finder.flankingLeft(21, 1));
	assertEquals(expected,
		     this.finder.flankingRight(9, 1));
    }


    @Test
    public void testSomeSimpleCases() {
	this.finder.add("At1g01030.1", 11649, 13611);
	this.finder.add("At1g01040.1", 23146, 31164);
	this.finder.add("At1g01050.1", 31181, 33148);
	this.finder.add("At1g01060.1", 33666, 37840);
	this.finder.add("At1g01060.2", 33666, 37840);
	this.finder.add("At1g01070.1", 38753, 40944);
	this.finder.add("At1g01070.2", 38753, 40944);
	this.finder.add("At1g01080.1", 45309, 47019);
	this.finder.add("At1g01090.1", 47485, 49279);

	assertEquals(Arrays.asList(new String[] {}),
		     this.finder.flankingLeft(10000, 3));
	assertEquals(Arrays.asList(new String[] {}),
		     this.finder.flankingRight(50000, 3));

	assertEquals(Arrays.asList(new String[] {
	    "At1g01070.1", 
	    "At1g01070.2",
	    "At1g01060.1"}),
		     this.finder.flankingLeft(45000, 3));

	assertEquals(Arrays.asList(new String[] {
	    "At1g01040.1", 
	    "At1g01050.1",
	    "At1g01060.2",
	}), this.finder.flankingRight(20000, 3));
    }



    @Test
    public void testFlankingLeftWithDuplicates() {
	int bignumber = 1000;
	for (int i = 0; i < bignumber; i++) {
	    this.finder.add("foo", i, i);
	    this.finder.add("foo", i, i);
	    this.finder.add("foo", i, i);
	}
	for (int i = 0; i < bignumber; i++) {
	    assertEquals(i * 3,
			 this.finder.flankingLeft(i, Integer.MAX_VALUE).size());
	}
    }



    @Test
    public void testFlankingRightWithDuplicates() {
	int bignumber = 1000;
	for (int i = 0; i < bignumber; i++) {
	    this.finder.add("foo", i, i);
	    this.finder.add("foo", i, i);
	    this.finder.add("foo", i, i);
	}
	for (int i = 0; i < bignumber; i++) {
	    assertEquals((bignumber - i - 1) * 3, 
			 this.finder.flankingRight(i, Integer.MAX_VALUE).size());
	}
    }


}
