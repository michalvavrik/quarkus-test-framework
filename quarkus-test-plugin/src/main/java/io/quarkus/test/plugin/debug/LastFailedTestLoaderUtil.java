package io.quarkus.test.plugin.debug;

public final class LastFailedTestLoaderUtil {

    /**
     * Optional signalling that last failed test executed by Surefire plugin or Failsafe plugin should be used.
     */
    public static final String USE_LAST_FAILED_TEST = "<<use-last-failed-test>>";

    private LastFailedTestLoaderUtil() {
        // UTIL CLASS
    }

    static Class<?> findLastFailedTestOrFail() {
        // FIXME: impl. me!
        return null;
    }

}
