package io.quarkus.deployment.pkg.steps;

import io.smallrye.common.process.ProcessBuilder;
import org.jboss.logging.Logger;

final class LinuxIDUtil {

    private LinuxIDUtil() {
    }

    static String getLinuxID(String option) {
        try {
            return ProcessBuilder.execToString("id", option).trim();
        } catch (Exception e) {
            Logger.getLogger(LinuxIDUtil.class.getPackageName()).warn("Failed to read ID", e);
            //swallow and return null id
            return null;
        }
    }
}
