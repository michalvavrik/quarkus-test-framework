package io.quarkus.test.utils;

import java.util.ArrayList;
import java.util.List;

public final class ProcessBuilderProvider {
    private ProcessBuilderProvider() {

    }

    public static ProcessBuilder command(List<String> command) {
        List<String> effectiveCommand = new ArrayList<>(command);
        return new ProcessBuilder(effectiveCommand);
    }
}
