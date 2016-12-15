package itutorgroup.h2h.utils;

public class StringUtil {

	/**
	 * 用符号隔开
	 * @param source 
	 * @param mark 符号
	 * @param num 间隔数
	 * @return
	 */
	public static String space(String source, String mark, int num) {
		source = source.replace(mark, "");
		StringBuilder sb = new StringBuilder();
		for(int i = 0, length = source.length(); i < length; i += num) {
			if (i > 0) {
				sb.append(mark);
			}
			if (i + num >= length) {
				sb.append(source.subSequence(i, length));
			} else {
				sb.append(source.subSequence(i, i + num));
			}
		}
		return sb.toString();
	}
	public static String formatMeetingId(String reverseStr) {
        String resultStr = "";
        if (reverseStr == null) return resultStr;
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                resultStr += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            resultStr += reverseStr.substring(i * 3, i * 3 + 3) + "-";
        }
        if (resultStr.endsWith("-")) {
            resultStr = resultStr.substring(0, resultStr.length() - 1);
        }
        return resultStr;
	}
}
