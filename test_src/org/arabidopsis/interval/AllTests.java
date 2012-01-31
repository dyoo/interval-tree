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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.runner.LoadingTestCollector;
import junit.runner.TestCollector;


public class AllTests extends TestCase {
    public AllTests(String name) {
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

	List<Class<? extends TestCase>> allTestClasses = getAllTestClasses();
	for(int i = 0 ; i < allTestClasses.size(); i++) {
	    suite.addTestSuite(allTestClasses.get(i));
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
    private static List<Class<? extends TestCase>> getAllTestClasses() throws ClassNotFoundException {
	final TestCollector collector = new LoadingTestCollector();
	@SuppressWarnings("unchecked")
	final java.util.Enumeration<String> e = collector.collectTests();
	final List<Class<? extends TestCase>> testClasses = new ArrayList<Class<? extends TestCase>>();
	while (e.hasMoreElements()) {
	    final String className = e.nextElement();
	    final Class<? extends TestCase> c = Class.forName(className).asSubclass(TestCase.class);
	    // don't add ourself
	    if (isSelfClass(c) || isClassNotInMyPackage(c)) {
		continue;
	    }
	    testClasses.add(c);
	}
	return testClasses;
    }


    // Returns true if the given class is ourself.
    private static boolean isSelfClass(Class<?> c) {
	return c.equals(AllTests.class);
    }


    // Returns true if the class c is not within the same package
    // hierarchy as ourselves.
    private static boolean isClassNotInMyPackage(Class<?> c) {
	return (! c.getPackage().getName().startsWith
		(AllTests.class.getPackage().getName()));
    }

}
