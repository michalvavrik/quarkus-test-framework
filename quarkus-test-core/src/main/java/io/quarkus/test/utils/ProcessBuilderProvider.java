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
        effectiveCommand.add(command.get(0));
        effectiveCommand.addAll(command.stream().skip(1).map(smth -> "\"" + smth + "\"").collect(Collectors.toList()));
        System.out.println("using command " + Arrays.toString(effectiveCommand.toArray()));
        return new ProcessBuilder();
    }
}
