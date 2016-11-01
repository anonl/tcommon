package nl.weeaboo.common;

import org.junit.Test;

import com.google.common.testing.EqualsTester;

public class AbstractIdTest {

    private static final AlphaId A1 = new AlphaId("1");
    private static final AlphaId A1_COPY = new AlphaId("1");
    private static final AlphaId A2 = new AlphaId("2");
    private static final BetaId B1 = new BetaId("1");

    @Test
    public void testEquals() {
        new EqualsTester()
            .addEqualityGroup(A1, A1_COPY)
            .addEqualityGroup(A2)
            .addEqualityGroup(B1) // Same id string, but different type
            .testEquals();
    }

    private static class AlphaId extends AbstractId {

        private static final long serialVersionUID = 1L;

        public AlphaId(String id) {
            super(id);
        }
    }

    private static class BetaId extends AbstractId {

        private static final long serialVersionUID = 1L;

        public BetaId(String id) {
            super(id);
        }
    }

}
