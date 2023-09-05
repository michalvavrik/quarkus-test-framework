package io.quarkus.test.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ProcessBuilderProvider {
    private ProcessBuilderProvider() {

    }

    public static ProcessBuilder command(List<String> command) {
        List<String> effectiveCommand = new ArrayList<>();
        if (command.get(0).equals("quarkus")) {
            effectiveCommand.add("c://ProgramData/chocolatey/bin/quarkus.exe");
        } else {
            effectiveCommand.addAll(command.stream().limit(2).collect(Collectors.toList()));
        }
        System.out.println("using command " + Arrays.toString(effectiveCommand.toArray()));
        return new ProcessBuilder(effectiveCommand);
    }
}
