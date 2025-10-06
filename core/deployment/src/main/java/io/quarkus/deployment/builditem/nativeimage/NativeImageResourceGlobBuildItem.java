package io.quarkus.deployment.builditem.nativeimage;

import io.quarkus.builder.item.MultiBuildItem;
import io.quarkus.deployment.pkg.NativeConfig;

/**
 * A build item that indicates that a set of resource paths defined by globs should be
 * included in the native image.
 * <p>
 * Globs passed to the {@code includeGlob*()} methods of the {@link NativeImageResourcePatternsBuildItem.Builder} are transformed to regular expressions
 * internally. See {@link NativeConfig.ResourcesConfig#includes} for the supported glob syntax.
 * <p>
 * The patterns are passed to the native image builder using {@code resource-config.json}.
 * The same mechanism (and regular expression syntax) is used by {@code native-image}'s
 * {@code -H:ResourceConfigurationFiles}, {@code -H:IncludeResources} and {@code -H:ExcludeResources} (since
 * GraalVM 20.3.0) command line options.
 * <p>
 * Related build items:
 * <ul>
 * <li>Use {@link NativeImageResourceBuildItem} if you need to add a single resource
 * <li>Use {@link NativeImageResourceDirectoryBuildItem} if you need to add a directory of resources
 * </ul>
 */
public final class NativeImageResourceGlobBuildItem extends MultiBuildItem {
}
