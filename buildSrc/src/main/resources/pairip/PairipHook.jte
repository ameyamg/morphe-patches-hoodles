@import java.util.Map
@import java.util.List
@import hoodles.morphe.codegen.pairip.*

@param String appName
@param JsonData data

package hoodles.morphe.extension.${appName}.pairip;

import android.util.Log;
import java.lang.reflect.Method;

@for(String clazz : data.getStrings().keySet())
import ${clazz};
@endfor
@for(String clazz : data.getMethods().keySet())
import ${clazz};
@endfor


public class PairipHook {
    public static void inject() {
        injectString();
        injectMethods();
    }

    private static void injectMethods() {
        @for(Map.Entry<String, List<Field>> entry : data.getMethods().entrySet())
            @for(Field field : entry.getValue())
                ${UtilsKt.getClassFromFqcn(entry.getKey())}.${field.getName()} = getMethod("${UtilsKt.getOwningClass(field.getValue())}", "${field.getValue()}");
            @endfor
        @endfor
    }

    private static void injectString() {
        @for(Map.Entry<String, List<Field>> entry : data.getStrings().entrySet())
            @for(Field field : entry.getValue())
                ${UtilsKt.getClassFromFqcn(entry.getKey())}.${field.getName()} = "${UtilsKt.toLiteral(field.getValue())}";
            @endfor
        @endfor
    }

    private static Method getMethod(String className, String signature) {
        try {
            for (Method method : Class.forName(className, false, PairipHook.class.getClassLoader()).getDeclaredMethods()) {
                if (method.toGenericString().equals(signature)) {
                    return method;
                }
            }
        } catch (ClassNotFoundException e) {
            Log.e("MORPHE - PairIp", "Could not find class: " + className, e);
        }

        return null;
    }
}