package io.quarkus.deployment.builditem.nativeimage;

import java.util.Arrays;
import java.util.List;

import io.quarkus.builder.item.MultiBuildItem;

/**
 * Used to register a class for JNI runtime access
 * with a fine-grained granularity.
 */
public final class JniRuntimeAccessJSONBuildItem extends MultiBuildItem {

    public final List<String> jsonNodes;

    public JniRuntimeAccessJSONBuildItem(String... jsonNode) {
        for (String i : jsonNode) {
            if (i == null) {
                throw new NullPointerException();
            }
        }
        this.jsonNodes = Arrays.asList(jsonNode);
    }
}
