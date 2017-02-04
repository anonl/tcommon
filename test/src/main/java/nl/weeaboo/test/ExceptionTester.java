package nl.weeaboo.test;

import java.util.concurrent.Callable;

/**
 * Convenience class for testing that certain method calls throw a particular kind of exception. The standard
 * ways to test for an exception in JUnit don't catch the exception, so only one exception per test may be
 * thrown.
 */
public final class ExceptionTester {

    /**
     * @param expected Expected exception type. The exception must be a {@link RuntimeException} because the code under
     *        test is a {@link Runnable} and that can't throw checked exceptions.
     */
    public void expect(Class<? extends RuntimeException> expected, final Runnable runnable) {
        expect(expected, new Callable<Void>() {
            @Override
            public Void call() {
                runnable.run();
                return null;
            }
        });
    }

    /**
     * @param expected Expected exception type. {@code Throwable} isn't supported because normal code shouldn't throw
     *        non-exception throwables.
     */
    public void expect(Class<? extends Exception> expected, Callable<?> callable) {
        try {
            callable.call();
            throw new AssertionError("Expected an exception of type: " + expected);
        } catch (Exception e) {
            if (!expected.isInstance(e)) {
                throw new AssertionError(e);
            }
        }
    }

}
