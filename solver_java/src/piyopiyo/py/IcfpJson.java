package piyopiyo.py;

import java.lang.reflect.Type;
import java.util.List;

import net.arnx.jsonic.JSON;

public class IcfpJson extends JSON {
    public static IcfpJson ICFPJSON = new IcfpJson();

    private IcfpJson() {}

    protected Object preformat(Context context, Object value) throws Exception {
        if (value instanceof long[]) {
            long[] longArray = (long[]) value;
            String[] stringArray = new String[longArray.length];
            for (int i = 0; i < longArray.length; i++) {
                stringArray[i] = "0x" + Long.toHexString(longArray[i]).toUpperCase();
            }
            return stringArray;
        }
        return super.preformat(context, value);
    }

    protected <T> T postparse(Context context, Object value,
                              Class<? extends T> c, Type t) throws Exception {
        if (long[].class.isAssignableFrom(c) && value instanceof List) {
            List<String> list = (List<String>) value;
            long[] array = new long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String elem = list.get(i);
                if (elem.startsWith("0x") && elem.length() == 18) {
                    array[i] = (Long.parseLong(elem.substring(2, 10), 16) << 32) |
                        Long.parseLong(elem.substring(10), 16);
                } else {
                    array[i] = Long.decode(elem);
                }
            }
            return c.cast(array);
        }
        return super.postparse(context, value, c, t);
    }
}
