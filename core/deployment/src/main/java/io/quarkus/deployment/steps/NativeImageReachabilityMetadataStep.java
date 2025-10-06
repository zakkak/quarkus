package io.quarkus.deployment.steps;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.quarkus.builder.Json;
import io.quarkus.builder.Json.JsonArrayBuilder;
import io.quarkus.builder.Json.JsonObjectBuilder;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ForceNonWeakReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.JniRuntimeAccessBuildItem;
import io.quarkus.deployment.builditem.nativeimage.JniRuntimeAccessFieldBuildItem;
import io.quarkus.deployment.builditem.nativeimage.JniRuntimeAccessMethodBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBundleBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassConditionBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveFieldBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveMethodBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;
import io.quarkus.deployment.pkg.NativeConfig;
import io.quarkus.deployment.pkg.steps.NativeOrNativeSourcesBuild;

public class NativeImageReachabilityMetadataStep {

    @BuildStep(onlyIf = NativeOrNativeSourcesBuild.class)
    void generateReachabilityMetadataConfig(BuildProducer<GeneratedResourceBuildItem> reachabiltyMetadata,
            NativeConfig nativeConfig,
            List<ReflectiveMethodBuildItem> reflectiveMethods,
            List<ReflectiveFieldBuildItem> reflectiveFields,
            List<ReflectiveClassBuildItem> reflectiveClassBuildItems,
            List<ForceNonWeakReflectiveClassBuildItem> nonWeakReflectiveClassBuildItems,
            List<ServiceProviderBuildItem> serviceProviderBuildItems,
            List<ReflectiveClassConditionBuildItem> reflectiveClassConditionBuildItems,
            List<JniRuntimeAccessBuildItem> jniRuntimeAccessibleClasses,
            List<JniRuntimeAccessFieldBuildItem> jniRuntimeAccessibleFields,
            List<JniRuntimeAccessMethodBuildItem> jniRuntimeAccessibleMethods,
            List<NativeImageResourceBuildItem> resources,
            List<NativeImageResourceBundleBuildItem> resourceBundles) {
        JsonObjectBuilder root = Json.object();

        root.put("reflection",
                NativeImageReflectConfig.generateReflectionConfig(nativeConfig, reflectiveMethods, reflectiveFields,
                        reflectiveClassBuildItems, nonWeakReflectiveClassBuildItems, serviceProviderBuildItems,
                        reflectiveClassConditionBuildItems));
        root.put("jni", NativeImageJNIConfig.generateJniConfig(jniRuntimeAccessibleClasses, jniRuntimeAccessibleFields,
                jniRuntimeAccessibleMethods));
        root.put("serialization", generateSerializationConfig(reflectiveClassBuildItems));

        JsonArrayBuilder resourcesJs = Json.array();
        //        JsonArrayBuilder includes = Json.array();
        //        JsonArrayBuilder excludes = Json.array();
        //
        //        for (NativeImageResourceBuildItem i : resources) {
        //            for (String path : i.getResources()) {
        //                JsonObjectBuilder pat = Json.object();
        //                pat.put("glob", path);
        //                includes.add(pat);
        //            }
        //        }
        //
        //        for (ServiceProviderBuildItem i : serviceProviderBuildItems) {
        //            includes.add(Json.object().put("pattern", Pattern.quote(i.serviceDescriptorFile())));
        //        }
        //
        //        for (NativeImageResourcePatternsBuildItem resourcePatternsItem : resourcePatterns) {
        //            addListToJsonArray(includes, resourcePatternsItem.getIncludePatterns());
        //            addListToJsonArray(excludes, resourcePatternsItem.getExcludePatterns());
        //        }
        //        resourcesJs.put("includes", includes);
        //        resourcesJs.put("excludes", excludes);
        //

        // TODO uncomment when https://github.com/graalvm/graalvm-community-jdk21u/issues/217 gets resolved
        //        for (NativeImageResourceBundleBuildItem i : resourceBundles) {
        //            JsonObjectBuilder bundle = Json.object();
        //            bundle.put("bundle", i.getBundleName().replace("/", "."));
        //            String moduleName = i.getModuleName();
        //            if (moduleName != null) {
        //                bundle.put("module", moduleName);
        //            }
        //            resourcesJs.add(bundle);
        //        }

        //        root.put("resources", resourcesJs);

        JsonArrayBuilder bundles = Json.array();
        for (NativeImageResourceBundleBuildItem i : resourceBundles) {
            JsonObjectBuilder bundle = Json.object();
            String moduleName = i.getModuleName();
            StringBuilder sb = new StringBuilder();
            if (moduleName != null) {
                sb.append(moduleName).append(":");
            }
            sb.append(i.getBundleName().replace("/", "."));
            bundle.put("name", sb.toString());
            bundles.add(bundle);
        }

        root.put("bundles", bundles);

        // TODO Lambdas
        // TODO proxies

        try (StringWriter writer = new StringWriter()) {
            root.appendTo(writer);
            reachabiltyMetadata.produce(new GeneratedResourceBuildItem("META-INF/native-image/reachability-metadata.json",
                    writer.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonArrayBuilder generateSerializationConfig(List<ReflectiveClassBuildItem> reflectiveClassBuildItems) {
        final Set<String> serializableClasses = new HashSet<>();
        for (ReflectiveClassBuildItem i : reflectiveClassBuildItems) {
            if (i.isSerialization()) {
                String[] classNames = i.getClassNames().toArray(new String[0]);
                Collections.addAll(serializableClasses, classNames);
            }
        }

        JsonArrayBuilder types = Json.array();
        for (String serializableClass : serializableClasses) {
            types.add(Json.object().put("type", serializableClass));
        }

        return types;
    }
}
