package io.quarkus.deployment.pkg.steps;

import static io.quarkus.deployment.pkg.steps.LinuxIDUtil.getLinuxID;
import static io.quarkus.runtime.util.ContainerRuntimeUtil.ContainerRuntime.DOCKER;
import static io.quarkus.runtime.util.ContainerRuntimeUtil.ContainerRuntime.PODMAN;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import io.quarkus.deployment.pkg.NativeConfig;
import io.quarkus.deployment.util.FileUtil;

public class NativeImageBuildLocalContainerRunner extends NativeImageBuildContainerRunner {

    public static class Factory extends NativeImageBuildContainerRunner.Factory {
        public Factory(NativeConfig nativeConfig) {
            super(nativeConfig);
        }

        @Override
        public NativeImageBuildLocalContainerRunner create(Path outputDir, String nativeImageName) {
            return new NativeImageBuildLocalContainerRunner(this, outputDir);
        }
    }

    private NativeImageBuildLocalContainerRunner(Factory factory, Path outputDir) {
        super(factory, outputDir);
        if (SystemUtils.IS_OS_LINUX) {
            final ArrayList<String> containerRuntimeArgs = new ArrayList<>(Arrays.asList(baseContainerRuntimeArgs));
            if (containerRuntime == DOCKER && containerRuntime.isRootless()) {
                Collections.addAll(containerRuntimeArgs, "--user", String.valueOf(0));
            } else {
                String uid = getLinuxID("-ur");
                String gid = getLinuxID("-gr");
                if (uid != null && gid != null && !uid.isEmpty() && !gid.isEmpty()) {
                    Collections.addAll(containerRuntimeArgs, "--user", uid + ":" + gid);
                    if (containerRuntime == PODMAN && containerRuntime.isRootless()) {
                        // Needed to avoid AccessDeniedExceptions
                        containerRuntimeArgs.add("--userns=keep-id");
                    }
                }
            }
            baseContainerRuntimeArgs = containerRuntimeArgs.toArray(baseContainerRuntimeArgs);
        }
    }

    @Override
    protected List<String> getContainerRuntimeBuildArgs() {
        final List<String> containerRuntimeArgs = super.getContainerRuntimeBuildArgs();
        final String volumeOutputPath;
        if (SystemUtils.IS_OS_WINDOWS) {
            volumeOutputPath = FileUtil.translateToVolumePath(outputPath);
        } else {
            volumeOutputPath = outputPath;
        }

        final String selinuxBindOption;
        if (SystemUtils.IS_OS_MAC && containerRuntime == PODMAN) {
            selinuxBindOption = "";
        } else {
            selinuxBindOption = ":z";
        }

        Collections.addAll(containerRuntimeArgs, "-v",
                volumeOutputPath + ":" + NativeImageBuildStep.CONTAINER_BUILD_VOLUME_PATH + selinuxBindOption);
        return containerRuntimeArgs;
    }

}
