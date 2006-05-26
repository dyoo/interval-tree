package org.arabidopsis.interval;

import java.util.Map;
import java.util.WeakHashMap;


public class OrderStatisticTree {
    private RbTree tree;
    private Map sizes;		// RbNode -> size
    private OrderStatisticUpdate updater;

    public OrderStatisticTree() {
	this.sizes = new WeakHashMap();
	this.sizes.put(RbNode.NIL, new Integer(0));
	this.updater = new OrderStatisticUpdate();
	this.tree = new RbTree(this.updater);
    }


    public void insert(RbNode x) {
	this.tree.insert(x);
    }


    public RbNode select(int n) {
	return _select(this.tree.root(), n);
    }


    // helper function for doing selection
    private RbNode _select(RbNode node, int i) {
	if (node.isNull())
	    return RbNode.NIL;
	int r = this.size(node.left) + 1;
	if (i == r)
	    return node;
	if (i < r)
	    return this._select(node.left, i);
	return this._select(node.right, i - r);
    }


    // Returns the size of a node.
    public int size(RbNode node) {
	assert this.sizes.containsKey(node);
	return ((Integer) this.sizes.get(node)).intValue();
    }

    
    class OrderStatisticUpdate implements StatisticUpdate {
	public void update(RbNode node) {
	    int n = size(node.left) + size(node.right) + 1;
	    sizes.put(node, new Integer(n));
	}
    }
}
