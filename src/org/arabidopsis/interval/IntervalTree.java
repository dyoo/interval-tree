package org.arabidopsis.interval;

/** An implementation of an interval tree, following the explanation.
 * from CLR.
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;

public class IntervalTree {
    private final StatisticUpdate updater;
    private final RbTree tree;

	private final Map<RbNode, Interval> intervals;
	private final Map<RbNode, Integer> max;
	private final Map<RbNode, Integer> min;

    private final Logger logger;


    public IntervalTree() {
	this.updater = new IntervalTreeStatisticUpdate();
	this.tree = new RbTree(this.updater);

	this.logger = Logger.getLogger(this.getClass());

	this.intervals = new WeakHashMap<RbNode, Interval>();
	this.intervals.put(RbNode.NIL, null);

	this.max = new WeakHashMap<RbNode, Integer>();
	this.max.put(RbNode.NIL, new Integer(Integer.MIN_VALUE));
	this.min = new WeakHashMap<RbNode, Integer>();
	this.min.put(RbNode.NIL, new Integer(Integer.MAX_VALUE));
    }


    public void insert(Interval interval) {
	RbNode node = new RbNode(interval.getLow());
	this.intervals.put(node, interval);
	this.tree.insert(node);
    }



    public int size() {
	return this.tree.size();
    }



    // Returns the first matching interval that we can find.
    public Interval search(Interval interval) {

	RbNode node = tree.root();
	if (node.isNull())
	    return null;

	while ( (! node.isNull()) &&
		(! getInterval(node).overlaps(interval))) {
	    if (canOverlapOnLeftSide(interval, node)) {
		node = node.left;
	    } else if (canOverlapOnRightSide(interval, node)) {
		node = node.right;
	    } else {
		return null;
	    }
	}

	// Defensive coding.  node can be the NIL node, but it must
	// not be itself the null object.
	assert node != null;
	return getInterval(node);
    }


    private boolean canOverlapOnLeftSide(Interval interval,
					 RbNode node) {
	return (! node.left.isNull()) &&
	    getMax(node.left) >= interval.getLow();
    }


    private boolean canOverlapOnRightSide(Interval interval,
					 RbNode node) {
	return (! node.right.isNull()) &&
	    getMin(node.right) <= interval.getHigh();
    }



    // Returns all matches as a list of Intervals
    public List<Interval> searchAll(Interval interval) {
	logger.debug("Starting search for " + interval);

	if (tree.root().isNull()) {
	    return new ArrayList<Interval>();
	}
	return this._searchAll(interval, tree.root());
    }


    private List<Interval> _searchAll(Interval interval, RbNode node) {
	assert (! node.isNull());

	logger.debug("Looking at " + getInterval(node));

	List<Interval> results = new ArrayList<Interval>();
	if (getInterval(node).overlaps(interval)) {
	    results.add(getInterval(node));
	    logger.debug("match");
	} else {
	    logger.debug("mismatch");
	}

	if (canOverlapOnLeftSide(interval, node)) {
	    results.addAll(_searchAll(interval, node.left));
	}

	if (canOverlapOnRightSide(interval, node)) {
	    results.addAll(_searchAll(interval, node.right));
	}

	return results;
    }



    
    public Interval getInterval(RbNode node) {
	assert (node != null);
	assert (! node.isNull());

	assert (this.intervals.containsKey(node));

	return this.intervals.get(node);
    }


    public int getMax(RbNode node) {
	assert (node != null);
	assert (this.intervals.containsKey(node));

	return (this.max.get(node)).intValue();
    }


    private void setMax(RbNode node, int value) {
	this.max.put(node, new Integer(value));
    }


    public int getMin(RbNode node) {
	assert (node != null);
	assert (this.intervals.containsKey(node));

	return (this.min.get(node)).intValue();
    }


    private void setMin(RbNode node, int value) {
	this.min.put(node, new Integer(value));
    }



    private class IntervalTreeStatisticUpdate 
	implements StatisticUpdate {
	public void update(RbNode node) {
	    setMax(node, max(max(getMax(node.left),
				 getMax(node.right)),
			     getInterval(node).getHigh()));

	    setMin(node, min(min(getMin(node.left),
				 getMin(node.right)),
			     getInterval(node).getLow()));
	}


	private int max(int x, int y) {
	    if (x > y) { return x; }
	    return y;
	}

	private int min(int x, int y) {
	    if (x < y) { return x; }
	    return y;
	}


    }








    /**
     *
     * Test case code: check to see that the data structure follows
     * the right constraints of interval trees:
     *
     *     o.  They're valid red-black trees
     *     o.  getMax(node) is the maximum of any interval rooted at that node..
     *
     * This code is expensive, and only meant to be used for
     * assertions and testing.
     */
    public boolean isValid() {
	return (this.tree.isValid() && 
		hasCorrectMaxFields(this.tree.root) &&
		hasCorrectMinFields(this.tree.root));
    }


    private boolean hasCorrectMaxFields(RbNode node) {
	if (node.isNull())
	    return true;
	return (getRealMax(node) == getMax(node) &&
		hasCorrectMaxFields(node.left) &&
		hasCorrectMaxFields(node.right));
    }


    private boolean hasCorrectMinFields(RbNode node) {
	if (node.isNull())
	    return true;
	return (getRealMin(node) == getMin(node) &&
		hasCorrectMinFields(node.left) &&
		hasCorrectMinFields(node.right));
    }


    private int getRealMax(RbNode node) {
	if (node.isNull())
	    return Integer.MIN_VALUE;	
	int leftMax = getRealMax(node.left);
	int rightMax = getRealMax(node.right);
	int nodeHigh = getInterval(node).getHigh();
	
	int max1 = (leftMax > rightMax ? leftMax : rightMax);
	return (max1 > nodeHigh ? max1 : nodeHigh);
    }


    private int getRealMin(RbNode node) {
	if (node.isNull())
	    return Integer.MAX_VALUE;	

	int leftMin = getRealMin(node.left);
	int rightMin = getRealMin(node.right);
	int nodeLow = getInterval(node).getLow();
	
	int min1 = (leftMin < rightMin ? leftMin : rightMin);
	return (min1 < nodeLow ? min1 : nodeLow);
    }


}
