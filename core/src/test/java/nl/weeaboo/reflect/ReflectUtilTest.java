package nl.weeaboo.reflect;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ReflectUtilTest {

    @Test
    public void findConstants() {
        assertConstants(Integer.TYPE, "INT_VALUE=1", "nonFinalValue=0");
        assertConstants(Integer.class, "INTEGER_VALUE=2");
        assertConstants(String.class, "STRING_VALUE=abc");
    }

    private void assertConstants(Class<?> fieldType, String... expected) {
        try {
            Map<String, ?> map = ReflectUtil.getConstants(TestConstants.class, fieldType);
            for (String exp : expected) {
                String name = exp.substring(0, exp.indexOf('='));
                String value = exp.substring(exp.indexOf('=')+1);
                Assert.assertEquals(value, String.valueOf(map.get(name)));
            }
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    public static class TestConstants {

        public static final int INT_VALUE = 1;
        public static final Integer INTEGER_VALUE = 2;
        public static final String STRING_VALUE = "abc";
        public static final Integer NULL_VALUE = null;
        public static int nonFinalValue;

        @SuppressWarnings("unused")
        private static String PRIVATE_VALUE = "def";

        public String nonStaticValue = "ghi";

    }
}
