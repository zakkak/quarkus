package io.quarkus.runtime.graal;

import com.oracle.svm.core.annotate.*;

import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

@Substitute //
@TargetClass(javax.imageio.ImageIO.class)
public final class Target_javax_imageio_ImageIO {

    @Substitute
    private static BufferedImage read(InputStream input) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static BufferedImage read(URL input) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static Iterator<ImageWriter> getImageWritersByMIMEType(String MIMEType) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static ImageOutputStream createImageOutputStream(Object output) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    // TODO remove
    @Substitute
    private static ImageReader getImageReader(ImageWriter writer) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }
}

@Substitute //
@TargetClass(java.awt.Component.class)
final class Target_java_awt_Component {

    @Substitute //
    @TargetElement(name = TargetElement.CONSTRUCTOR_NAME)
    void constructor() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

}

@Substitute //
@TargetClass(java.awt.Graphics.class)
final class Target_java_awt_Graphics {

    @Substitute
    private boolean drawImage(Image image, int x, int y, ImageObserver observer) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

}

@Substitute //
@TargetClass(java.awt.Image.class)
final class Target_java_awt_Image {

    @Substitute //
    @TargetElement(name = TargetElement.CONSTRUCTOR_NAME)
    void constructor() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private int getWidth(ImageObserver observer) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private int getHeight(ImageObserver observer) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

}

@Substitute //
@TargetClass(java.awt.MediaTracker.class)
final class Target_java_awt_MediaTracker {

    @Substitute //
    @TargetElement(name = TargetElement.CONSTRUCTOR_NAME)
    void constructor(Component comp) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private void addImage(Image image, int id) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private void waitForAll() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

}

// The following substitutions are only required because we can't substitute BufferedImage due to
// https://github.com/oracle/graal/blob/51fe078b16e61bc50d95bdbbee9b0b0886b4c98d/substratevm/src/com.oracle.svm.hosted/src/com/oracle/svm/hosted/substitute/AnnotationSubstitutionProcessor.java#L573-L575

//@Substitute //
//@TargetClass(java.awt.image.BufferedImage.class)
//final class Target_java_awt_image_BufferedImage {
//
//}

// For the same reason we can't substitute the inner class that causes the issues...

//@Substitute //
//@TargetClass(className = "com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl$10")
//final class Target_com_sun_xml_bind_v2_model_impl_RuntimeBuiltinLeafInfoImpl$10
//{
//
//}

@Substitute //
@TargetClass(java.awt.color.ICC_Profile.class)
final class Target_java_awt_color_ICC_Profile {

    @Substitute
    private static ICC_Profile getInstance(int cspace) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static ICC_Profile getStandardProfile(final String name) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

}

@TargetClass(java.awt.color.ColorSpace.class)
final class Target_java_awt_color_ColorSpace {

    @Substitute
    private static ColorSpace getInstance(int colorspace) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

}

@Substitute //
@TargetClass(java.awt.image.ColorModel.class)
final class Target_java_awt_image_ColorModel {

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    long pData;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    int pixel_bits;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    int nBits[];

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    int transparency;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    boolean supportsAlpha;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    boolean isAlphaPremultiplied;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    int numComponents;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    int numColorComponents;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    ColorSpace colorSpace;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    int colorSpaceType;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    int maxBits;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    boolean is_sRGB;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    int transferType;

    @Alias //
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.Reset)
    static boolean loaded;

    @Substitute //
    @TargetElement(name = TargetElement.CONSTRUCTOR_NAME)
    private ColorModel constructor(int bits) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute //
    @TargetElement(name = TargetElement.CONSTRUCTOR_NAME)
    private ColorModel constructor(int pixel_bits, int[] bits, ColorSpace cspace,
                                   boolean hasAlpha,
                                   boolean isAlphaPremultiplied,
                                   int transparency,
                                   int transferType) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static ColorModel getRGBdefault() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private ColorModel createCompatibleWritableRaster(int w, int h) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    static int getDefaultTransferType(int pixel_bits) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static boolean isLinearRGBspace(ColorSpace colorSpace) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static boolean isLinearGRAYspace(ColorSpace colorSpace) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static byte[] getLinearRGB8TosRGB8LUT() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static byte[] getsRGB8ToLinearRGB8LUT() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static byte[] getLinearRGB16TosRGB8LUT() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static short[] getsRGB8ToLinearRGB16LUT() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static byte[] getGray8TosRGB8LUT(ICC_ColorSpace grayCS) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static byte[] getLinearGray16ToOtherGray8LUT(ICC_ColorSpace grayCS) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static byte[] getGray16TosRGB8LUT(ICC_ColorSpace grayCS) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private static short[] getLinearGray16ToOtherGray16LUT(ICC_ColorSpace grayCS) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private boolean hasAlpha() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private final boolean isAlphaPremultiplied() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private final int getTransferType() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private int getPixelSize() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private int getComponentSize(int componentIdx) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private int[] getComponentSize() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private int getTransparency() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private int getNumComponents() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private int getNumColorComponents() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    protected void finalize() {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }
}
