package io.quarkus.test.plugin.debug;

import static io.quarkus.test.plugin.debug.LastFailedTestLoaderUtil.USE_LAST_FAILED_TEST;
import static io.quarkus.test.plugin.debug.LastFailedTestLoaderUtil.findLastFailedTestOrFail;

import picocli.CommandLine;

public final class TestClassConverter implements CommandLine.ITypeConverter<Class<?>> {

    @Override
    public Class<?> convert(String testClass) throws Exception {
        if (USE_LAST_FAILED_TEST.equals(testClass)) {
            return findLastFailedTestOrFail();
        }
        // FIXME: impl. me!
        // FIXME: check if valid class (signature) ? dots as smoke test?
        // FIXME: if valid class then load it, otherwise try to autocomplete pattern from short class name and write it to docs!
        return null;
    }
}
