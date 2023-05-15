package io.quarkus.deployment.pkg.steps;

import java.nio.file.Path;
import java.util.List;

public class NativeImageBuildRunnerDummy extends NativeImageBuildRunner {

    private static final String MESSAGE = "NativeImageBuildRunnerDummy is note meant to be used to perform an actual build.";
    private final boolean isContainer;

    public NativeImageBuildRunnerDummy(boolean isContainer) {
        this.isContainer = isContainer;
    }

    @Override
    public boolean isContainer() {
        return isContainer;
    }

    @Override
    protected String[] getGraalVMVersionCommand(List<String> args) {
        throw new RuntimeException(MESSAGE);
    }

    @Override
    protected String[] getBuildCommand(Path outputDir, List<String> args) {
        throw new RuntimeException(MESSAGE);
    }

    @Override
    protected void objcopy(Path outputDir, String... args) {
        throw new RuntimeException(MESSAGE);
    }
}
