package io.quarkus.deployment.builditem.nativeimage;

import io.quarkus.gizmo.TryBlock;

public interface AutoFeatureAugmentor {
    void augment(TryBlock overallCatch);
}
