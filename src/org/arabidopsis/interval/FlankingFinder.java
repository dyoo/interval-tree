package org.arabidopsis.interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;




/**
 *
 * Utility class to find objects that flank a certain position.
 *
 *
 */

public class FlankingFinder<T> {
    private final RbTree lows;
    private final RbTree highs;

    private final Map<RbNode, T> lowRbNodeToObj;
    private final Map<RbNode, T> highRbNodeToObj;

    public FlankingFinder() {
	this.lows = new RbTree();
	this.highs = new RbTree();
	this.lowRbNodeToObj = new WeakHashMap<RbNode, T>();
	this.highRbNodeToObj = new WeakHashMap<RbNode, T>();
    }


    public void add(T obj, int low, int high) {
	RbNode lowNode = new RbNode(low);
	RbNode highNode = new RbNode(high);
	this.lows.insert(lowNode);
	this.highs.insert(highNode);

	this.lowRbNodeToObj.put(lowNode, obj);
	this.highRbNodeToObj.put(highNode, obj);
    }



    public List<T> flankingLeft(int pos, int n) {
	if (this.highs.root.isNull())
	    return new ArrayList<T>();

	RbNode node = this.highs.root;
	RbNode lastNode = node;
	while (node != RbNode.NIL) {
	    if (pos <= node.key)  {
		lastNode = node;
		node = node.left; 
	    } else {
		lastNode = node;
		node = node.right; 
	    }
	}

	while (lastNode != RbNode.NIL && lastNode.key >= pos) {
	    lastNode = this.highs.predecessor(lastNode);
	}

	List<T> results = new ArrayList<T>();
	for (int i = 0; i < n && lastNode != RbNode.NIL; i++) {
	    results.add(highRbNodeToObj.get(lastNode));
	    lastNode = this.highs.predecessor(lastNode);
	}
	return results;
    }




    public List<T> flankingRight(int pos, int n) {
	if (this.lows.root.isNull())
	    return new ArrayList<T>();

	RbNode node = this.lows.root;
	RbNode lastNode = node;
	while (node != RbNode.NIL) {
	    if (pos <= node.key)  {
		lastNode = node;
		node = node.left; 
	    } else {
		lastNode = node;
		node = node.right; 
	    }
	}

	while (lastNode != RbNode.NIL && lastNode.key <= pos) {
	    lastNode = this.lows.successor(lastNode);
	}

	List<T> results = new ArrayList<T>();
	for (int i = 0; i < n && lastNode != RbNode.NIL; i++) {
	    results.add(lowRbNodeToObj.get(lastNode));
	    lastNode = this.lows.successor(lastNode);
	}
	return results;
    }
}
