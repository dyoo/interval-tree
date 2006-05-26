package org.arabidopsis.interval;


/**
 *
 * Suite of all tests.  Uses some Junit magic.
 *
 *
 * @param
 * @return
 * @exception
 * @see
 */

import junit.runner.TestCollector;
import junit.runner.LoadingTestCollector;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;


public class TestAll extends TestCase {
    public TestAll(String name) {
	super(name);
    }


    /**
     *
     * Returns a suite of all the tests we can find.
     *
     *
     */
    public static TestSuite suite() throws Exception {
        TestSuite suite = new TestSuite();

	List allTestClasses = getAllTestClasses();
	for(int i = 0 ; i < allTestClasses.size(); i++) {
	    suite.addTestSuite((Class)allTestClasses.get(i));
	}

        return suite;
    }



    /**
     *
     * Adds all the tests we can find (excluding ourselves, of course) in
     * the CLASSPATH.
     *
     *
     * @param
     * @return
     * @exception
     * @see
     */
    private static List getAllTestClasses() throws ClassNotFoundException {
	TestCollector collector = new LoadingTestCollector();
	java.util.Enumeration e = collector.collectTests();
	List testClasses = new ArrayList();
	while (e.hasMoreElements()) {
	    String className = (String) e.nextElement();
	    Class c = Class.forName(className);
	    // don't add ourself
	    if (isSelfClass(c) || isClassNotInMyPackage(c)) {
		continue;
	    }
	    testClasses.add(c);
	}
	return testClasses;
    }


    // Returns true if the given class is ourself.
    private static boolean isSelfClass(Class c) {
	return c.equals(TestAll.class);
    }


    // Returns true if the class c is not within the same package
    // hierarchy as ourselves.
    private static boolean isClassNotInMyPackage(Class c) {
	return (! c.getPackage().getName().startsWith
		(TestAll.class.getPackage().getName()));
    }

}
