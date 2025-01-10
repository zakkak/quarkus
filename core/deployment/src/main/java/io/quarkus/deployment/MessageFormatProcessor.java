package io.quarkus.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;

public class MessageFormatProcessor {

    /* Register service providers and resrouces accessed transitively through java.text.MessageFormat */
    @BuildStep
    void registerCommonResourceBundleProvider(BuildProducer<ServiceProviderBuildItem> services,
            BuildProducer<NativeImageResourceBuildItem> resources) {
        services.produce(new ServiceProviderBuildItem("sun.util.resources.LocaleData$CommonResourceBundleProvider"));
        resources.produce(new NativeImageResourceBuildItem("sun.util.resources.provider.LocaleDataProvider"));
    }
}
