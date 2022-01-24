package io.quarkus.deployment.builditem.nativeimage;

import io.quarkus.builder.item.MultiBuildItem;
import io.quarkus.gizmo.TryBlock;

/**
 * A build item that can be used to augment the generated AutoFeature
 */
public final class NativeImageAutoFeatureAugmentorBuildItem extends MultiBuildItem {
    AutoFeatureAugmentor autoFeatureAugmentor;

    public NativeImageAutoFeatureAugmentorBuildItem(AutoFeatureAugmentor autoFeatureAugmentor) {
        this.autoFeatureAugmentor = autoFeatureAugmentor;
    }

    public void augment(TryBlock overallCatch) {
        autoFeatureAugmentor.augment(overallCatch);
    }
}
