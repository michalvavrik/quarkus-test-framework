package io.quarkus.test.logging;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import io.quarkus.test.utils.AwaitilityUtils;

public abstract class LoggingHandler implements Closeable {

    private static final long TIMEOUT_IN_MILLIS = 4000;
    private static final String ANY = ".*";

    private final List<String> logs = new CopyOnWriteArrayList<>();
    private Timer timer = null;

    protected abstract void handle();

    public void startWatching() {
        if (timer != null) {
            return;
        }

        logs.clear();

        timer = new Timer();
        timer.schedule(new LoggingHandlerTask(), TIMEOUT_IN_MILLIS, TIMEOUT_IN_MILLIS);
    }

    public void stopWatching() {
        if (timer == null) {
            return;
        }
        timer.cancel();
        flush();
        logs.clear();
    }

    public List<String> logs() {
        return Collections.unmodifiableList(logs);
    }

    public boolean logsContains(String expected) {
        return logs().stream().anyMatch(line -> line.contains(expected) // simple contains
                || line.matches(ANY + expected + ANY)); // or by regular expression
    }

    public void flush() {
        AwaitilityUtils.untilAsserted(this::handle);
    }

    @Override
    public void close() {
        stopWatching();
    }

    protected void onLine(String line) {
        logs.add(line);
        if (isLogEnabled()) {
            logInfo(line);
        }
    }

    protected void logInfo(String line) {
        Log.info(line);
    }

    protected void onLines(String lines) {
        Stream.of(lines.split("\\r?\\n")).filter(LoggingHandler::isNotEmpty).forEach(this::onLine);
    }

    protected void onStringDifference(String newLines, String oldLines) {
        if (isNotEmpty(oldLines)) {
            onLines(replace(newLines, oldLines));
        } else {
            onLines(newLines);
        }
    }

    protected boolean isLogEnabled() {
        return true;
    }

    private static boolean isNotEmpty(String text) {
        return text != null && !text.isEmpty();
    }

    private static String replace(String text, String repl) {
        if (text != null && repl != null && !repl.isEmpty()) {
            StringBuilder buf = new StringBuilder(text.length());
            int start = 0;

            int end;
            while ((end = text.indexOf(repl, start)) != -1) {
                buf.append(text, start, end);
                start = end + repl.length();
            }

            buf.append(text, start, text.length());
            return buf.toString();
        } else {
            return text;
        }
    }

    private class LoggingHandlerTask extends TimerTask {

        @Override
        public void run() {
            try {
                handle();
            } catch (Exception exception) {
                Log.debug("Exception thrown by logging handler", exception.getMessage());
            }
        }
    }
}
