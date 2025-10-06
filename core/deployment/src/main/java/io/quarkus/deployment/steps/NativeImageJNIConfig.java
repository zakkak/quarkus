package io.quarkus.deployment.steps;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.quarkus.builder.Json;
import io.quarkus.builder.Json.JsonArrayBuilder;
import io.quarkus.builder.Json.JsonObjectBuilder;
import io.quarkus.deployment.builditem.nativeimage.JniRuntimeAccessBuildItem;
import io.quarkus.deployment.builditem.nativeimage.JniRuntimeAccessFieldBuildItem;
import io.quarkus.deployment.builditem.nativeimage.JniRuntimeAccessMethodBuildItem;

public class NativeImageJNIConfig {

    static JsonArrayBuilder generateJniConfig(List<JniRuntimeAccessBuildItem> jniRuntimeAccessibleClasses, List<JniRuntimeAccessFieldBuildItem> jniRuntimeAccessibleFields, List<JniRuntimeAccessMethodBuildItem> jniRuntimeAccessibleMethods) {
        final Map<String, JniInfo> jniClasses = new LinkedHashMap<>();

        jniRuntimeAccessibleClasses.forEach(c -> addJniClass(jniClasses, c));
        jniRuntimeAccessibleFields.forEach(f -> addJniField(jniClasses, f));
        jniRuntimeAccessibleMethods.forEach(m -> addJniMethod(jniClasses, m));

        JsonArrayBuilder root = Json.array();
        for (Map.Entry<String, JniInfo> entry : jniClasses.entrySet()) {
            JsonObjectBuilder json = Json.object();

            json.put("type", entry.getKey());

            JniInfo info = entry.getValue();
            JsonArrayBuilder methodsArray = Json.array();
            if (info.constructors) {
                json.put("allDeclaredConstructors", true);
            } else if (!info.ctorSet.isEmpty()) {
                for (JniRuntimeAccessMethodBuildItem ctor : info.ctorSet) {
                    methodsArray.add(createMethodObject(ctor));
                }
            }
            if (info.methods) {
                json.put("allDeclaredMethods", true);
            } else if (!info.methodSet.isEmpty()) {
                for (JniRuntimeAccessMethodBuildItem method : info.methodSet) {
                    methodsArray.add(createMethodObject(method));
                }
            }
            if (!methodsArray.isEmpty()) {
                json.put("methods", methodsArray);
            }

            if (info.fields) {
                json.put("allDeclaredFields", true);
            } else if (!info.fieldSet.isEmpty()) {
                JsonArrayBuilder fieldsArray = Json.array();
                for (String fieldName : info.fieldSet) {
                    fieldsArray.add(Json.object().put("name", fieldName));
                }
                json.put("fields", fieldsArray);
            }

            root.add(json);
        }
        return root;
    }

    private static JsonObjectBuilder createMethodObject(JniRuntimeAccessMethodBuildItem method) {
        JsonObjectBuilder methodObject = Json.object();
        methodObject.put("name", method.getName());
        JsonArrayBuilder paramsArray = Json.array();
        for (int i = 0; i < method.getParams().length; ++i) {
            paramsArray.add(method.getParams()[i]);
        }
        methodObject.put("parameterTypes", paramsArray);
        return methodObject;
    }

    private static void addJniClass(Map<String, JniInfo> jniClasses, JniRuntimeAccessBuildItem jniRuntimeAccessBuildItem) {
        for (String cl : jniRuntimeAccessBuildItem.getClassNames()) {
            JniInfo existing = jniClasses.get(cl);
            if (existing == null) {
                jniClasses.put(cl, new JniInfo(jniRuntimeAccessBuildItem));
            } else {
                if (jniRuntimeAccessBuildItem.isConstructors()) {
                    existing.constructors = true;
                }
                if (jniRuntimeAccessBuildItem.isMethods()) {
                    existing.methods = true;
                }
                if (jniRuntimeAccessBuildItem.isFields()) {
                    existing.fields = true;
                }
            }
        }
    }

    private static void addJniMethod(Map<String, JniInfo> reflectiveClasses, JniRuntimeAccessMethodBuildItem methodInfo) {
        String cl = methodInfo.getDeclaringClass();
        JniInfo existing = reflectiveClasses.get(cl);
        if (existing == null) {
            reflectiveClasses.put(cl, existing = new JniInfo());
        }
        if (methodInfo.getName().equals("<init>")) {
            existing.ctorSet.add(methodInfo);
        } else {
            existing.methodSet.add(methodInfo);
        }
    }

    private static void addJniField(Map<String, JniInfo> jniClasses, JniRuntimeAccessFieldBuildItem fieldInfo) {
        String cl = fieldInfo.getDeclaringClass();
        JniInfo existing = jniClasses.get(cl);
        if (existing == null) {
            jniClasses.put(cl, existing = new JniInfo());
        }
        existing.fieldSet.add(fieldInfo.getName());
    }

    static final class JniInfo {
        boolean constructors;
        boolean methods;
        boolean fields;
        Set<String> fieldSet = new HashSet<>();
        Set<JniRuntimeAccessMethodBuildItem> methodSet = new HashSet<>();
        Set<JniRuntimeAccessMethodBuildItem> ctorSet = new HashSet<>();

        private JniInfo(boolean methods, boolean fields, boolean constructors) {
            this.methods = methods;
            this.fields = fields;
            this.constructors = constructors;
        }

        private JniInfo() {
            this(false, false, false);
        }

        private JniInfo(JniRuntimeAccessBuildItem jniRuntimeAccessBuildItem) {
            this(jniRuntimeAccessBuildItem.isMethods(),
                    jniRuntimeAccessBuildItem.isFields(),
                    jniRuntimeAccessBuildItem.isConstructors());
        }
    }

}
