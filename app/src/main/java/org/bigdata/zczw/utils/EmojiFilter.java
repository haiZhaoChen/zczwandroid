package org.bigdata.zczw.utils;

public class EmojiFilter {

    // 转义时标识
    private static final char unicode_separator = '&';
    private static final char unicode_prefix = 'u';
    private static final char separator = ':';

	/**
	 * 检测是否有emoji字符
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
		if (source.isEmpty()||source == null) {
			return false;
		}

		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				//do nothing，判断到了这里表明，确认有表情字符
				return true;
			}
		}

		return false;
	}

	public static String getIngeger(String s) {
		int a = 0;
		String aa = "";
		String sss = "";
		int b = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			//判断是否为表情
			if (isEmojiCharacter(c)) {
				b++;
				int i1 = c - 48;//转为ascill
				a += i1;

				if (b % 2 == 0) {

					aa =(a + 16419) + ";";
					a = 0;
					b = 0;
					sss = sss + aa;
				}
			} else {
				sss = sss + new String(Character.toString(c));
			}
		}
		return sss;
	}

    /**
     * 将带有emoji字符的字符串转换成可见字符标识
     */
    public static String escape(String src) {
        if (src == null) {
            return null;
        }
        int cpCount = src.codePointCount(0, src.length());
        int firCodeIndex = src.offsetByCodePoints(0, 0);
        int lstCodeIndex = src.offsetByCodePoints(0, cpCount - 1);
        StringBuilder sb = new StringBuilder(src.length());
        for (int index = firCodeIndex; index <= lstCodeIndex;) {
            int codepoint = src.codePointAt(index);
            if (isEmojiCharacter(codepoint)) {
                String hash = Integer.toHexString(codepoint);
                sb.append(unicode_separator).append(hash.length()).append(unicode_prefix).append(separator).append(hash);
            } else {
                sb.append((char) codepoint);
            }
        }
        return sb.toString();
    }

	private static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) ||
				(codePoint == 0x9) ||
				(codePoint == 0xA) ||
				(codePoint == 0xD) ||
				((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
				((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
				((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
	}

    private static boolean isEmojiCharacter(int codePoint) {
        return (codePoint >= 0x2600 && codePoint <= 0x27BF) // 杂项符号与符号字体
                || codePoint == 0x303D
                || codePoint == 0x2049
                || codePoint == 0x203C
                || (codePoint >= 0x2000 && codePoint <= 0x200F)//
                || (codePoint >= 0x2028 && codePoint <= 0x202F)//
                || codePoint == 0x205F //
                || (codePoint >= 0x2065 && codePoint <= 0x206F)//
                /* 标点符号占用区域 */
                || (codePoint >= 0x2100 && codePoint <= 0x214F)// 字母符号
                || (codePoint >= 0x2300 && codePoint <= 0x23FF)// 各种技术符号
                || (codePoint >= 0x2B00 && codePoint <= 0x2BFF)// 箭头A
                || (codePoint >= 0x2900 && codePoint <= 0x297F)// 箭头B
                || (codePoint >= 0x3200 && codePoint <= 0x32FF)// 中文符号
                || (codePoint >= 0xD800 && codePoint <= 0xDFFF)// 高低位替代符保留区域
                || (codePoint >= 0xE000 && codePoint <= 0xF8FF)// 私有保留区域
                || (codePoint >= 0xFE00 && codePoint <= 0xFE0F)// 变异选择器
                || codePoint >= 0x10000; // Plane在第二平面以上的，char都不可以存，全部都转
    }

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {

		source += " "; // 在传入的source后面加上一个空字符。返回的时候trim掉就OK了

		if (!containsEmoji(source)) {

			return source.trim();	// 如果不包含，直接返回

		}

		// 到这里铁定包含

		StringBuilder buf = null;

		int len = source.length();

		for (int i = 0; i < len; i++) {

			char codePoint = source.charAt(i);

			if (!isEmojiCharacter(codePoint)) {

				if (buf == null) {

					buf = new StringBuilder(source.length());

				}

				buf.append(codePoint);

			} else {

			}

		}

		if (buf == null) {

			return source.trim();	// 如果没有找到 emoji表情，则返回源字符串

		} else {

			if (buf.length() == len) {	// 这里的意义在于尽可能少的toString，因为会重新生成字符串

				buf = null;

				return source.trim();

			} else {

				return buf.toString().trim();

			}

		}

	}
}