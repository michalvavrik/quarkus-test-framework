package io.quarkus.test.plugin.command;

import static io.quarkus.test.plugin.debug.LastFailedTestLoaderUtil.USE_LAST_FAILED_TEST;
import static picocli.CommandLine.Help.Visibility.NEVER;

import java.util.Set;
import java.util.concurrent.Callable;

import io.quarkus.test.bootstrap.QuarkusScenarioBootstrap;
import io.quarkus.test.bootstrap.TestContext.TestContextImpl;
import io.quarkus.test.plugin.debug.TestClassConverter;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "debug", description = "Debug Quarkus application with specific test configuration")
public class Debug implements Callable<Integer> {

    @Option(order = 1, names = { "--native",
            "-n" }, description = "Build a native executable and run tests against it", defaultValue = "false")
    public boolean nativeMode;

    // FIXME: arity?
    // FIXME: paramLabel?
    // FIXME: description?
    // FIXME: interactive mode and prompt with preprocessed test classes???
    // FIXME: completion candidates ^^^?
    // FIXME: there is an option to set preprocessor!!!!!!!!!!!!!!
    // FIXME: prompt??
    // FIXME: echo???
    // FIXME: index?? type???
    // FIXME: mapFallbackValue??
    // FIXME: provide value converter from str to class
    // FIXME: it is fairly possible if we want to detect app classes then we need to have this as dep instead of
    // FIXME: external JAR, this needs to be tested
    @Parameters(arity = "1", paramLabel = "TEST_CLASS", description = "Defines test class whose configuration should "
            + "be used for starting Quarkus application. By default, first last failed test class is "
            + "used.", showDefaultValue = NEVER, defaultValue = USE_LAST_FAILED_TEST, converter = TestClassConverter.class)
    Class<?> testClass;

    @Override
    public Integer call() {
        // FIXME: rewrite it
        var bootstrap = new QuarkusScenarioBootstrap();
        Set<String> tags = Set.of(); // FIXME we need to get this from processor
        // FIXME: change it
        bootstrap.beforeAll(new TestContextImpl(testClass, tags));

        // FIXME: change it
        bootstrap.afterAll();
        // FIXME: remove me
        System.out.println("Result: " + "TODO");
        // FIXME: we always need to be able to sweep environment (close classes etc.) so there must be
        // FIXME: VERY VERY HIGHLIGHTED requirement to exit debug via key
        return ExitCode.OK;
    }
}
