package io.quarkus.netty.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.nativeimage.ExcludeConfigBuildItem;

public class NettyOverrideMetadata {

    static final String NETTY_JAR_MATCH_REGEX = "io\\.netty\\.netty-(codec|handler)";
    static final String NETTY_REFLECT_CONFIG_MATCH_REGEX = "/META-INF/native-image/io\\.netty/netty-(codec|handler)/generated/handlers/reflect-config\\.json";

    @BuildStep
    ExcludeConfigBuildItem excludeNettyDirectives() {
        return new ExcludeConfigBuildItem(NETTY_JAR_MATCH_REGEX, NETTY_REFLECT_CONFIG_MATCH_REGEX);
    }
}
