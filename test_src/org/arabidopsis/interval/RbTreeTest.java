package org.arabidopsis.interval;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class RbTreeTest {
    private RbTree tree;

    @Before
    public void setUp() {
	this.tree = new RbTree();
    }


    @After
    public void tearDown() {
	this.tree = null;
    }


    @Test
    public void testEmptyTreeIsValid() {
	assertTrue(this.tree.isValid());
    }


    @Test
    public void testTreeWithSingleNodeIsValid() {
	this.tree.insert(new RbNode(5));
	assertTrue(this.tree.isValid());
    }



    @Test
    public void testTreeWithRootColorDamageIsntValid() {
	this.tree.insert(new RbNode(5));
	RbNode node = this.tree.get(5);
	node.color = RbNode.RED;
	assertFalse(this.tree.isValid());
    }


    // Utility function to do significant damage to a red black tree
    private void intentionallyColorAllNodesBlack(RbNode node) {
	if (node.isNull())
	    return;
	node.color = RbNode.BLACK;
	intentionallyColorAllNodesBlack(node.left);
	intentionallyColorAllNodesBlack(node.right);
    }


    @Test
    public void testDamageCanBeDetected() {
	for (int i = 0; i < 8; i++) {
	    tree.insert(new RbNode(i));
	}
	assertTrue(this.tree.isValid());
	intentionallyColorAllNodesBlack(this.tree.root);
	assertFalse(this.tree.isValid());
    }






    @Test
    public void testConstruction() {
	RbNode n1 = new RbNode(42);
	RbNode n2 = new RbNode(43);
	tree.insert(n1);
	tree.insert(n2);
	assertEquals(n1, tree.get(42));
	assertEquals(n2, tree.get(43));
	assertEquals(2, height(tree.root()));
	assertTrue(this.tree.isValid());

    }


    @Test
    public void testEmpty() {
	assertTrue(tree.root().isNull());
	assertTrue(tree.get(42).isNull());
	assertEquals(0, height(tree.root));

	assertTrue(tree.root().parent.isNull());
	assertTrue(tree.root().left.isNull());
	assertTrue(tree.root().right.isNull());
	assertTrue(this.tree.isValid());
    }




    @Test
    public void testTreeInsert() {
	RbNode n1, n2, n3;
	n1 = new RbNode(1);
	n2 = new RbNode(2);
	n3 = new RbNode(3);
	tree.treeInsert(n1);
	tree.treeInsert(n2);
	tree.treeInsert(n3);
	assertEquals(n1, tree.root);
	assertEquals(n2, tree.root.right);
	assertEquals(n3, tree.root.right.right);
	assertTrue(tree.root.left.isNull());
	
	assertEquals(3, height(tree.root()));
	assertEquals(3, size(tree.root()));
    }


    @Test
    public void testRotationLeft() {
	RbNode n1, n2, n3;
	n1 = new RbNode(1);
	n2 = new RbNode(2);
	n3 = new RbNode(3);
	tree.treeInsert(n1);
	tree.treeInsert(n2);
	tree.treeInsert(n3);
	tree.leftRotate(n1);
	assertEquals(n2, tree.root);
	assertEquals(n1, tree.root.left);
	assertEquals(n3, tree.root.right);
	assertEquals(2, height(tree.root()));
    }


    @Test
    public void testRotationAgain() {
	for (int i = 0; i < 10; i++) {
	    tree.treeInsert(new RbNode(i));
	}

	RbNode node;

	for (int i = 0; i < 10 - 1; i++) {
	    tree.leftRotate(tree.root);
	}
	node = tree.root;
	for (int i = 9; i >= 0; i--) {
	    assertEquals(i, node.key);
	    assertTrue(node.right.isNull());
	    node = node.left;
	}


	for (int i = 0; i < 10 - 1; i++) {
	    tree.rightRotate(tree.root);
	}
	node = tree.root;
	for (int i = 0; i < 10; i++) {
	    assertEquals(i, node.key);
	    assertTrue(node.left.isNull());
	    node = node.right;
	}
    }


    @Test
    public void testSuccessorTraversal() {
	int BIGNUMBER = 1000;
	for (int i = 0; i < BIGNUMBER; i++) {
	    tree.insert(new RbNode(i));
	}

	RbNode node = tree.get(0);
	int j = 0;
	while (! node.isNull()) {
	    assertEquals(j, node.key);
	    node = tree.successor(node);
	    j++;
	}
	assertEquals(j, BIGNUMBER);
    }


    @Test
    public void testPredecessorTraversal() {
	int BIGNUMBER = 1000;
	for (int i = 0; i < BIGNUMBER; i++) {
	    tree.insert(new RbNode(i));
	}
	RbNode node = tree.get(BIGNUMBER-1);
	int j = BIGNUMBER-1;
	while (! node.isNull()) {
	    assertEquals(j, node.key);
	    node = tree.predecessor(node);
	    j--;
	}
	assertEquals(-1, j);
    }


    @Test
    public void testNullPredecessor() {
	int BIGNUMBER = 1000;
	for (int i = 0; i < BIGNUMBER; i++) {
	    tree.insert(new RbNode(i));
	}

	RbNode node = tree.get(0);
	assertTrue(tree.predecessor(node).isNull());
	assertTrue(this.tree.isValid());
    }


    @Test
    public void testNullSuccessor() {
	int BIGNUMBER = 1000;
	for (int i = 0; i < BIGNUMBER; i++) {
	    tree.insert(new RbNode(i));
	}

	RbNode node = tree.get(BIGNUMBER-1);
	assertTrue(tree.successor(node).isNull());
	assertTrue(this.tree.isValid());
    }


    @Test
    public void testRotationRight() {
	RbNode n1, n2, n3;
	n1 = new RbNode(1);
	n2 = new RbNode(2);
	n3 = new RbNode(3);
	tree.treeInsert(n3);
	tree.treeInsert(n2);
	tree.treeInsert(n1);
	tree.rightRotate(tree.root);
	assertEquals(n2, tree.root);
	assertEquals(n1, tree.root.left);
	assertEquals(n3, tree.root.right);
	assertEquals(2, height(tree.root()));
    }



    @Test
    public void testSizeUpTo100() {
	for (int n = 1; n <= 100; n++) {
	    setUp();
	    // need to reset the tree to avoid interference!
	    testSizeAndHeight(n);
	    tearDown();

	}
    }


    @Test
    public void testSizeUpTo100WithDuplicates() {
	for (int n = 1; n <= 100; n++) {
	    setUp();
	    // need to reset the tree to avoid interference!
	    testSizeAndHeightWithDuplicateKey(n);
	    tearDown();
	}
    }



    private void testSizeAndHeight(int n) {
	for (int i = 1; i <= n; i++) {
	    tree.insert(new RbNode(i));
	}
	assertEquals(tree.root.toString(),  n, size(tree.root));
	for (int i = 1; i <= n; i++) {
	    assertEquals(i, tree.get(i).key);
	}
	assertTrue(tree.get(n+1).isNull());
	assertTrue(tree.get(0).isNull());
	assertTrue("test for n=" + n + ": " + 
		   height(tree.root) + " greater than " + 2 * ln(n+1) + " " + tree.root,
		   height(tree.root) <= 2 * ln(n+1));

	assertTrue(this.tree.isValid());
    }



    private void testSizeAndHeightWithDuplicateKey(int n) {
	int arbitraryKey = 42;
	for (int i = 1; i <= n; i++) {
	    tree.insert(new RbNode(arbitraryKey));
	}
	assertEquals("iteration " + n, n, size(tree.root));

	assertTrue("test for n=" + n + ": " + 
		   height(tree.root) + " greater than " + 2 * ln(n+1) + " " + tree.root,
		   height(tree.root) <= 2 * ln(n+1));


	assertTrue(this.tree.isValid());
    }




    private double ln(double a) {
	return Math.log(a) / Math.log(2);
    }



    private int size(RbNode node) {
	if (node.isNull()) { return 0; }
	return 1 + size(node.left) + size(node.right);
    }

    private int height(RbNode node) {
	if (node.isNull()) { return 0; }
	int h1 = height(node.left) + 1;
	int h2 = height(node.right) + 1;
	if (h1 > h2) { return h1; }
	return h2;
    }
}
