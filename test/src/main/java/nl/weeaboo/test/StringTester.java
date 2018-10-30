package nl.weeaboo.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;

public final class StringTester {

    private final Set<String> expectedSubStrings = new HashSet<String>();

    /**
     * Require that the tested string contains the given sub-string at least once.
     */
    public StringTester withSubString(String str) {
        expectedSubStrings.add(str);
        return this;
    }

    /**
     * Checks the contents of the given string according to the criteria added to this {@link StringTester}.
     */
    public void test(String stringToTest) {
        for (String subString : expectedSubStrings) {
            assertContains(stringToTest, subString);
        }
    }

    /**
     * Checks that a the string-to-test contains the desired sub-string.
     */
    public static void assertContains(String stringToTest, String subString) {
        Assert.assertTrue("String: " + stringToTest + "\nSub-string: " + subString,
                stringToTest.contains(subString));
    }

}
