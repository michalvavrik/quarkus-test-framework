package io.quarkus.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.condition.OS;

public final class ProcessBuilderProvider {
    private ProcessBuilderProvider() {

    }

    public static ProcessBuilder command(List<String> command) {
        List<String> effectiveCommand = new ArrayList<>(command);
        if (OS.WINDOWS.isCurrentOs()) {
            // blah
        }

        return new ProcessBuilder(effectiveCommand);
    }
}
