package org.arabidopsis.interval;

// Quick and dirty interval class
public class Interval implements Comparable<Interval> {
    private final int low;
    private final int high;

    public Interval(int low, int high) {
	assert low <= high;
	this.low = low;
	this.high = high;
    }


    public boolean equals(Object other) {
	if (this == other)
	    return true;
	if (this.getClass().equals(other.getClass())) {
	    Interval otherInterval = (Interval) other;
	    return (this.low == otherInterval.low &&
		    this.high == otherInterval.high);
	}
	return false;
    }


    public int hashCode() {
	return low;
    }


    public int compareTo(final Interval other) {
	if (this.low < other.low)
	    return -1;
	if (this.low > other.low)
	    return 1;

	if (this.high < other.high)
	    return -1;
	if (this.high > other.high)
	    return 1;

	return 0;
    }

    public String toString() {
	return "Interval[" + this.low + ", " + this.high + "]";
    }


    /**
     * Returns true if this interval overlaps the other.
     */
    public boolean overlaps(Interval other) {
	return (this.low <= other.high &&
		other.low <= this.high);
    }


    public int getLow() {
	return this.low;
    }

    public int getHigh() {
	return this.high;
    }

    
}
