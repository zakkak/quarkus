package io.quarkus.netty.deployment;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.netty.util.internal.CleanableDirectBuffer;
import io.quarkus.test.QuarkusExtensionTest;

/**
 * Run with {@code -Dmaven.compiler.release=25} to cover augmentation of applications targeting Java 25,
 * where the former cleaner transformations referenced fields removed in Netty 4.2.20.
 */
@EnabledForJreRange(min = JRE.JAVA_25)
class NettyForeignMemoryTest {

    @RegisterExtension
    static final QuarkusExtensionTest TEST = new QuarkusExtensionTest()
            .withEmptyApplication();

    @ParameterizedTest
    @ValueSource(strings = { "CleanerJava24Linker", "CleanerJava25" })
    void allocateAndClean(String cleanerName) throws Exception {
        Class<?> cleanerClass = Class.forName("io.netty.util.internal." + cleanerName);
        Method isSupported = cleanerClass.getDeclaredMethod("isSupported");
        isSupported.setAccessible(true);
        assertThat(isSupported.invoke(null)).as("%s is supported", cleanerName).isEqualTo(true);

        Constructor<?> constructor = cleanerClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object cleaner = constructor.newInstance();
        Method allocate = cleanerClass.getDeclaredMethod("allocate", int.class);
        allocate.setAccessible(true);

        for (int capacity : new int[] { 0, 64 }) {
            CleanableDirectBuffer allocation = (CleanableDirectBuffer) allocate.invoke(cleaner, capacity);
            try {
                ByteBuffer buffer = allocation.buffer();
                assertThat(buffer.isDirect()).isTrue();
                assertThat(buffer.capacity()).isEqualTo(capacity);
                assertThat(allocation.hasMemoryAddress()).isTrue();
                assertThat(allocation.memoryAddress()).isNotZero();
                if (capacity > 0) {
                    buffer.putInt(0, 42);
                    assertThat(buffer.getInt(0)).isEqualTo(42);
                }
            } finally {
                allocation.clean();
            }
        }
    }
}
