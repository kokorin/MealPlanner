package mealplanner.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class IdAdapter extends XmlAdapter<String, Long> {
    private final String className;

    public IdAdapter(Class clazz) {
        this.className = clazz.getSimpleName();
    }

    @Override
    public Long unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }

        v = v.substring(className.length());
        try {
            return Long.valueOf(v);
        } catch (NumberFormatException e) {

        }
        return null;
    }

    @Override
    public String marshal(Long v) throws Exception {
        if (v == null) {
            return null;
        }
        return className + v;
    }
}
