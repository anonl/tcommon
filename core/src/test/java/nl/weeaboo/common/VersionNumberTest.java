package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class VersionNumberTest {

    @Test
    public void testParse() {
        assertValid("1", 1);
        assertValid("1.2", 1, 2);
        assertValid("1.2.3", 1, 2, 3);
        assertValid("10.20.30", 10, 20, 30);
        assertValid("1.02", 1, 2); // Leading zeroes are allowed

        assertInvalid("1."); // No trailing '.' allowed
        assertInvalid(".1"); // No leading '.' allowed
        assertInvalid("1..2"); // Must have exactly one '.' between each part
        assertInvalid("1.0 beta"); // No non-integer parts
        assertInvalid("1.0beta"); // No non-integer parts
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(VersionNumber.parse("2.1"), new VersionNumber(2, 1));
    }

    @Test
    public void testCompare() {
        // Version numbers are equal to themselves
        assertOrder("1", "1", 0);
        assertOrder("1.2", "1.2", 0);
        assertOrder("1.2.3", "1.2.3", 0);

        // a.b.0 and a.b are considered equal
        assertOrder("1.0", "1", 0);
        assertOrder("1.0.0", "1", 0);
        assertOrder("1.2.0", "1.2", 0);

        // 1.2(.0) comes before 1.2.1
        assertOrder("1.2", "1.2.1", -1);
        assertOrder("1.2.0", "1.2.1", -1);
        assertOrder("1.3.0", "1.2.1", 1);
    }

    @Test
    public void testEquals() {
        // See testCompare

        Assert.assertNotEquals(new VersionNumber(0, 0), null);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("1.2", VersionNumber.parse("1.02").toString());
    }

    private void assertValid(String versionString, int... parts) {
        VersionNumber versionNumber = VersionNumber.parse(versionString);
        Assert.assertArrayEquals(parts, versionNumber.getParts());
    }

    private void assertInvalid(String versionString) {
        try {
            VersionNumber.parse(versionString);
            Assert.fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    private void assertOrder(String versionStringA, String versionStringB, int expectedOrder) {
        VersionNumber versionA = VersionNumber.parse(versionStringA);
        VersionNumber versionB = VersionNumber.parse(versionStringB);

        // Test both overloads of compareTo
        Assert.assertEquals(expectedOrder, versionA.compareTo(versionB));
        Assert.assertEquals(expectedOrder, versionA.compareTo(versionStringB));

        // Comparing the two values in reverse should return the opposite result
        Assert.assertEquals(-expectedOrder, versionB.compareTo(versionA));
        Assert.assertEquals(-expectedOrder, versionB.compareTo(versionStringA));

        // If the two versions are supposed compare equal, their hashCodes and equals() should also match
        if (expectedOrder == 0) {
            Assert.assertEquals(versionA.hashCode(), versionB.hashCode());
            Assert.assertEquals(versionA, versionB);
            Assert.assertEquals(versionB, versionA);
        } else {
            Assert.assertNotEquals(versionA, versionB);
            Assert.assertNotEquals(versionB, versionA);
        }
    }

}
