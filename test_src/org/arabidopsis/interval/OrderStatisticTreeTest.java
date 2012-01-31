package org.arabidopsis.interval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OrderStatisticTreeTest {

    @Test
    public void testNilSize() {
	OrderStatisticTree tree = new OrderStatisticTree();
	assertEquals(0, tree.size(RbNode.NIL));
    }


    @Test
    public void testSingleElement() {
	OrderStatisticTree tree = new OrderStatisticTree();
	RbNode node = new RbNode(3);
	tree.insert(node);

	assertEquals(0, tree.size(RbNode.NIL));
	assertEquals(1, tree.size(node));
    }


    @Test
    public void testAFewElements() {
	OrderStatisticTree tree = new OrderStatisticTree();
	RbNode two = new RbNode(2);
	RbNode seven = new RbNode(7);
	RbNode one = new RbNode(1);
	RbNode eight = new RbNode(8);
	tree.insert(two);
	tree.insert(seven);
	tree.insert(one);
	tree.insert(eight);

	assertEquals(one, tree.select(1));
	assertEquals(two, tree.select(2));
	assertEquals(seven, tree.select(3));
	assertEquals(eight, tree.select(4));
    }

    @Test
    public void testBulkCase() {
	int BIG = 10000;
	OrderStatisticTree tree = new OrderStatisticTree();
	for(int i = 1; i < BIG; i++)
	    tree.insert(new RbNode(i));

	for(int i = 1; i < BIG; i++)
	    assertEquals(i, tree.select(i).key);
    }


    @Test
    public void testBulkCaseWithGaps() {
	int BIG = 20000;
	OrderStatisticTree tree = new OrderStatisticTree();
	for(int i = 1; i < BIG; i+= 2)
	    tree.insert(new RbNode(i));

	for(int i = 1; i < BIG; i++) {
	    if (i <= 10000)
		assertEquals(2*i - 1, tree.select(i).key);
	    else
		assertEquals(RbNode.NIL, tree.select(i));
	}
    }



}
