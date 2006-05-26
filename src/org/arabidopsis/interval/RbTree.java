package org.arabidopsis.interval;


// Implementation of red-black trees.  We try to stay close
// to the pseudocode described in 'Introduction to Algorithms'
// by CLR.

// This implementation also can take in a StatisticUpdate instance to
// maintain auxillary information for each RbNode.  We call update()
// whenever an RbNode is inserted, in two phases:
//
// 1.  Insertion as a leaf.
// 2.  Rotation to maintain red-black invariants.
//
// update() is propagated up ancestors, although this might be
// overkill.

import org.apache.log4j.Logger;

public class RbTree {
    RbNode root;
    RbNode NIL = RbNode.NIL;
    StatisticUpdate updater;
    Logger logger;

    public RbTree(StatisticUpdate updater) {
	this.root = NIL;
	this.updater = updater;
	this.logger = Logger.getLogger(this.getClass());
    }


    public RbTree() {
	this(null);
    }


    public void insert(RbNode x) {
	assert (x != null);
	assert (! x.isNull());

	treeInsert(x);
	x.color = RbNode.RED;
	while (x != this.root && x.parent.color == RbNode.RED) {
	    if (x.parent == x.parent.parent.left) {
		RbNode y = x.parent.parent.right;
		if (y.color == RbNode.RED) {
		    x.parent.color = RbNode.BLACK;
		    y.color = RbNode.BLACK;
		    x.parent.parent.color = RbNode.RED;
		    x = x.parent.parent;
		} else {
		    if (x == x.parent.right) {
			x = x.parent;
			this.leftRotate(x);
		    }
		    x.parent.color = RbNode.BLACK;
		    x.parent.parent.color = RbNode.RED;
		    this.rightRotate(x.parent.parent);
		}
	    } else {
		RbNode y = x.parent.parent.left;
		if (y.color == RbNode.RED) {
		    x.parent.color = RbNode.BLACK;
		    y.color = RbNode.BLACK;
		    x.parent.parent.color = RbNode.RED;
		    x = x.parent.parent;
		} else {
		    if (x == x.parent.left) {
			x = x.parent;
			this.rightRotate(x);
		    }
		    x.parent.color = RbNode.BLACK;
		    x.parent.parent.color = RbNode.RED;
		    this.leftRotate(x.parent.parent);
		}
	    }
	}
	this.root.color = RbNode.BLACK;
    }



    public RbNode get(int key) {
	RbNode node = this.root;
	while (node != NIL) {
	    if (key == node.key) {
		return node; 
	    }
	    if (key < node.key)  {
		node = node.left; 
	    }
	    else {
		node = node.right; 
	    }
	}
	return NIL;
    }


    public RbNode root() {
	return this.root;
    }


    public RbNode minimum(RbNode node) {
	assert (node != null);
	assert (! node.isNull());
	while (! node.left.isNull()) {
	    node = node.left;
	}
	return node;
    }


    public RbNode maximum(RbNode node) {
	assert (node != null);
	assert (! node.isNull());
	while (! node.right.isNull()) {
	    node = node.right;
	}
	return node;
    }


    public RbNode successor(RbNode x) {
	assert (x != null);
	assert (! x.isNull());
	if (! x.right.isNull()) {
	    return this.minimum(x.right);
	}
	RbNode y = x.parent;
	while ( (! y.isNull()) && x == y.right ) {
	    x = y;
	    y = y.parent;
	}
	return y;
    }


    public RbNode predecessor(RbNode x) {
	assert (x != null);
	assert (! x.isNull());

	if (! x.left.isNull()) {
	    return this.maximum(x.left);
	}
	RbNode y = x.parent;
	while ( (! y.isNull()) && x == y.left ) {
	    x = y;
	    y = y.parent;
	}
	return y;
    }




    void leftRotate(RbNode x) {
	RbNode y = x.right;
	x.right = y.left;
	if (y.left != NIL) {
	    y.left.parent = x;
	}
	y.parent = x.parent;
	if (x.parent == NIL) {
	    this.root = y;
	} else {
	    if (x.parent.left == x) {
		x.parent.left = y;
	    } else {
		x.parent.right = y;
	    }
	}
	y.left = x;
	x.parent = y;

	this.applyUpdate(x);
	// no need to apply update on y, since it'll y is an ancestor
	// of x, and will be touched by applyUpdate().
    }


    void rightRotate(RbNode x) {
	RbNode y = x.left;
	x.left = y.right;
	if (y.right != NIL) {
	    y.right.parent = x;
	}
	y.parent = x.parent;
	if (x.parent == NIL) {
	    this.root = y;
	} else {
	    if (x.parent.right == x) {
		x.parent.right = y;
	    } else {
		x.parent.left = y;
	    }
	}
	y.right = x;
	x.parent = y;


	this.applyUpdate(x);
	// no need to apply update on y, since it'll y is an ancestor
	// of x, and will be touched by applyUpdate().
    }


    // Note: treeInsert is package protected because it does NOT
    // maintain RB constraints.
    void treeInsert(RbNode x) {
	RbNode node = this.root;
	RbNode y = NIL;
	while(node != NIL) {
	    y = node;
	    if (x.key <= node.key) {
		node = node.left;
	    } else {
		node = node.right;
	    }
	}
	x.parent = y;
	
	if (y == NIL) {
	    this.root = x;
	    x.left = x.right = NIL;
	} else {
	    if (x.key <= y.key) {
		y.left = x;
	    } else {
		y.right = x;
	    }
	}

	this.applyUpdate(x);
    }


    // Applies the statistic update on the node and its ancestors.
    private void applyUpdate(RbNode node) {
	if (this.updater == null)
	    return;
	while (! node.isNull()) {
	    this.updater.update(node);
	    node = node.parent;
	}
    }


    /**
     * Returns the number of nodes in the tree.
     */
    public int size() {
	return _size(this.root);
    }


    private int _size(RbNode node) {
	if (node.isNull())
	    return 0;
	return 1 + _size(node.left) + _size(node.right);
    }




    /**
     *
     * Test code: make sure that the tree has all the properties
     * defined by Red Black trees:
     *
     * o.  Root is black.
     *
     * o.  NIL is black.
     *
     * o.  Red nodes have black children.
     *
     * o.  Every path from root to leaves contains the same number of
     *     black nodes.
     * 
     * Calling this function will be expensive, as is meant for
     * assertion or test code.
     */
    public boolean isValid() {
	if (this.root.color != RbNode.BLACK) {
	    logger.warn("root color is wrong");
	    return false;
	}
	if (NIL.color != RbNode.BLACK) {
	    logger.warn("NIL color is wrong");
	    return false;
	}
	if (allRedNodesFollowConstraints(this.root) == false) {
	    logger.warn("red node doesn't follow constraints");
	    return false;
	}
	if (isBalancedBlackHeight(this.root) == false) {
	    logger.warn("black height unbalanced");
	    return false;
	}
	return true;
    }



    private boolean allRedNodesFollowConstraints(RbNode node) {
	if (node.isNull())
	    return true;

	if (node.color == RbNode.BLACK) {
	    return (allRedNodesFollowConstraints(node.left) &&
		    allRedNodesFollowConstraints(node.right));
	}

	// At this point, we know we're on a RED node.
	return (node.left.color == RbNode.BLACK &&
		node.right.color == RbNode.BLACK &&
		allRedNodesFollowConstraints(node.left) &&
		allRedNodesFollowConstraints(node.right));
    }


    // Check that both ends are equally balanced in terms of black height.
    private boolean isBalancedBlackHeight(RbNode node) {
	if (node.isNull())
	    return true;
	return (blackHeight(node.left) == blackHeight(node.right) &&
		isBalancedBlackHeight(node.left) &&
		isBalancedBlackHeight(node.right));
    }


    // The black height of a node should be left/right equal.
    private int blackHeight(RbNode node) {
	if (node.isNull())
	    return 0;
	int leftBlackHeight = blackHeight(node.left);
	if (node.color == RbNode.BLACK) {
	    return leftBlackHeight + 1;
	} else {
	    return leftBlackHeight;
	}
    }

}
