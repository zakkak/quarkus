package io.quarkus.runtime.graal;

import com.oracle.svm.core.annotate.*;

import java.awt.*;
import java.awt.image.BufferedImage;

@TargetClass(className = "com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl$10")
final class Target_com_sun_xml_bind_v2_model_impl_RuntimeBuiltinLeafInfoImpl$10
{

    @Substitute
    private Image parse(CharSequence text) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private BufferedImage convertToBufferedImage(Image image) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }

    @Substitute
    private Target_Base64Data print(Image v) {
        throw new UnsupportedOperationException("Not implemented yet for GraalVM native images");
    }
}

@TargetClass(className = "com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data")
final class Target_Base64Data {

}