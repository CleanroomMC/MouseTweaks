package yalter.mousetweaks.reflect;

import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

@Deprecated
public class ReflectionCache {

    private final HashMap<String, Method> methods = new HashMap<>();
    private final HashMap<String, Field> fields = new HashMap<>();

    public Object invokeMethod(Object obj, String name, Object... args) throws InvocationTargetException {
        Method method = methods.get(name);

        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            CrashReport crashreport = CrashReport.makeCrashReport(e, "Invoking method in MouseTweaks' reflection");
            throw new ReportedException(crashreport);
        }
    }

    public Object getFieldValue(Object obj, String name) {
        Field field = fields.get(name);

        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            CrashReport crashreport = CrashReport.makeCrashReport(e, "Getting field value in MouseTweaks' reflection");
            throw new ReportedException(crashreport);
        }
    }

    public void setFieldValue(Object obj, String name, Object value) {
        Field field = fields.get(name);

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            CrashReport crashreport = CrashReport.makeCrashReport(e, "Setting field value in MouseTweaks' reflection");
            throw new ReportedException(crashreport);
        }
    }

    void storeMethod(String name, Method method) {
        methods.put(name, method);
    }

    void storeField(String name, Field field) {
        fields.put(name, field);
    }
}
