package io.quarkus.test.bootstrap;

import static io.quarkus.test.logging.Log.LOG_FILE_OUTPUT;
import static io.quarkus.test.logging.Log.LOG_SUFFIX;

import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import io.quarkus.test.bootstrap.TestContext.TestContextImpl;
import io.quarkus.test.bootstrap.TestContext.TestStore;

public final class ScenarioContext {

    private static final int SCENARIO_ID_MAX_SIZE = 60;

    private final String id;
    private TestContext testContext;
    private boolean failed;
    private boolean debug;

    ScenarioContext(TestContext testContext) {
        this.testContext = testContext;
        this.id = generateScenarioId(testContext);
    }

    private ScenarioContext(TestContext testContext, String id, boolean failed, boolean debug) {
        this.testContext = testContext;
        this.id = id;
        this.failed = failed;
        this.debug = debug;
    }

    public ScenarioContext toClassScenarioContext() {
        // drop test method name
        return new ScenarioContext(new TestContextImpl(testContext, null), id, failed, debug);
    }

    public String getId() {
        return id;
    }

    public boolean isFailed() {
        return failed;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getRunningTestClassName() {
        return getTestContext().getRequiredTestClass().getSimpleName();
    }

    public Optional<String> getRunningTestMethodName() {
        return getTestContext().getRunningTestMethodName();
    }

    public TestStore getTestStore() {
        return getTestContext().getTestStore();
    }

    public TestContext getTestContext() {
        return testContext;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return getTestContext().getRequiredTestClass().isAnnotationPresent(annotationClass);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return getTestContext().getRequiredTestClass().getAnnotation(annotationClass);
    }

    public void setTestMethodName(String methodName) {
        this.testContext = new TestContextImpl(testContext, methodName);
    }

    public Path getLogFolder() {
        return Paths.get(LOG_FILE_OUTPUT.get());
    }

    public Path getLogFile() {
        return getLogFolder().resolve(getRunningTestClassName() + LOG_SUFFIX);
    }

    void markScenarioAsFailed() {
        failed = true;
    }

    private static String generateScenarioId(TestContext context) {
        String fullId = context.getRequiredTestClass().getSimpleName() + "-" + System.currentTimeMillis();
        return fullId.substring(0, Math.min(SCENARIO_ID_MAX_SIZE, fullId.length()));
    }
}
