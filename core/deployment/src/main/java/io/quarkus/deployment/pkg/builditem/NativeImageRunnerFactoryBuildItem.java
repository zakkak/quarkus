package io.quarkus.deployment.pkg.builditem;

import io.quarkus.builder.item.SimpleBuildItem;
import io.quarkus.deployment.pkg.steps.NativeImageBuildRunner;

/**
 * The resolved factory for the native image runner.
 */
public final class NativeImageRunnerFactoryBuildItem extends SimpleBuildItem {
    private final NativeImageBuildRunner.Factory buildRunnerFactory;

    public NativeImageRunnerFactoryBuildItem(NativeImageBuildRunner.Factory buildRunnerFactory) {
        this.buildRunnerFactory = buildRunnerFactory;
    }

    public NativeImageBuildRunner.Factory getBuildRunnerFactory() {
        return buildRunnerFactory;
    }

    public boolean isContainerBuild() {
        return buildRunnerFactory.isContainerBuild();
    }
}
