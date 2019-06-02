package cc.flycode.loader.system;

/**
 * Created by FlyCode on 02/06/2019 Package cc.flycode.loader.system
 */
public class ALAPI {
    public static String KEY = "YecoF0I6M05thxLeokoHuW8iUhTdIUInjkfF";

    public static String toBinary(String s) {
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    public static String xor(String s1, String s2) {
        String s0 = "";
        for (int i = 0; i < (s1.length() < s2.length() ? s1.length() : s2.length()); i++)
            s0 += Byte.valueOf("" + s1.charAt(i)) ^ Byte.valueOf("" + s2.charAt(i));
        return s0;
    }
}
