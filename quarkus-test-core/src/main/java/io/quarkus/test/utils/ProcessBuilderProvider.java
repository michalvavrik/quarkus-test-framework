package io.quarkus.test.utils;

import static java.lang.String.format;
import static java.lang.String.join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.condition.OS;

public final class ProcessBuilderProvider {
    private ProcessBuilderProvider() {

    }

    public static ProcessBuilder command(List<String> command) {
        List<String> effectiveCommand = new ArrayList<>(command);
        if (OS.WINDOWS.isCurrentOs()) {
            effectiveCommand = List.of("cmd.exe", "/c", format("\"%s\"", join(" ", effectiveCommand)));
            System.out.println("effective commands are " + Arrays.toString(effectiveCommand.toArray()));
        }

        return new ProcessBuilder(effectiveCommand);
    }
}
