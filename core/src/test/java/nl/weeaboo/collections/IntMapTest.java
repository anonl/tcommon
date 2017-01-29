package nl.weeaboo.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.weeaboo.collections.IntMap;

public class IntMapTest {

    private static final Value A = new Value();
    private static final Value B = new Value();
    private static final Value C = new Value();

    private IntMap<Value> intMap;

    @Before
    public void before() {
        intMap = new IntMap<Value>();
    }

    /** Add/remove a bunch of values randomly and check that the behavior matches {@link HashMap} */
    @Test
    public void addRemoveRandom() {
        Random random = new Random(12345);

        Map<Integer, Value> hashMap = new HashMap<Integer, Value>();
        for (int n = 0; n < 10000; n++) {
            int key = random.nextInt(200) - 100;
            Value val = new Value();

            Object oldIntMapVal;
            Object oldHashMapVal;
            if (random.nextDouble() <= 0.25) {
                oldIntMapVal = intMap.remove(key);
                oldHashMapVal = hashMap.remove(key);
            } else {
                oldIntMapVal = intMap.put(key, val);
                oldHashMapVal = hashMap.put(key, val);
            }

            // Check that intmap behavior matches hashmap
            Assert.assertEquals(hashMap.size(), intMap.size());
            Assert.assertEquals("Incorrect value for key " + key, hashMap.get(key), intMap.get(key));
            Assert.assertEquals(oldHashMapVal, oldIntMapVal);
        }
    }

    /** Test iterating over values */
    @Test
    public void iterateValues() {
        intMap.put(3, B);
        intMap.put(-1, A);
        intMap.put(200, C);

        List<Value> values = new ArrayList<Value>();
        for (Value value : intMap.values()) {
            values.add(value);
        }

        Assert.assertArrayEquals(new Object[] { A, B, C }, values.toArray());
    }

    /** Access slots (key/value) by index */
    @Test
    public void accessByIndex() {
        intMap.put(3, B);
        intMap.put(-1, A);
        intMap.put(200, C);

        Assert.assertEquals(3, intMap.size());

        Assert.assertEquals(-1, intMap.keyAt(0));
        Assert.assertEquals(A, intMap.valueAt(0));

        Assert.assertEquals(3, intMap.keyAt(1));
        Assert.assertEquals(B, intMap.valueAt(1));

        Assert.assertEquals(200, intMap.keyAt(2));
        Assert.assertEquals(C, intMap.valueAt(2));

        // Remove the middle slot
        Assert.assertEquals(B, intMap.removeAt(1));
        Assert.assertEquals(2, intMap.size());

        Assert.assertEquals(-1, intMap.keyAt(0));
        Assert.assertEquals(A, intMap.valueAt(0));

        Assert.assertEquals(200, intMap.keyAt(1));
        Assert.assertEquals(C, intMap.valueAt(1));

        // Overwrite a slot's value
        Assert.assertEquals(C, intMap.putAtIndex(1, B));
        Assert.assertEquals(2, intMap.size());

        Assert.assertEquals(200, intMap.keyAt(1));
        Assert.assertEquals(B, intMap.valueAt(1));
    }

    private static final class Value {
    }

}
