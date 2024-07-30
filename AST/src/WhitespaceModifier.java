import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

public class WhitespaceModifier {
    public static String removeSpaces(String code) {
        StringBuilder modifiedCode = new StringBuilder();
        String[] lines = code.split("\n");

        Pattern stringLiteralPattern = Pattern.compile("\"[^\"]*\"");
        Pattern operatorPattern = Pattern.compile("\\s*([=+\\-*/])\\s*");

        int placeholderCounter = 0;
        Map<String, String> placeholders = new HashMap<>();

        for (String line : lines) {
            // ʹ��Matcher�������ַ���������
            Matcher matcher = stringLiteralPattern.matcher(line);
            StringBuffer buffer = new StringBuffer();

            while (matcher.find()) {
                String matchedString = matcher.group();
                // Ϊÿ���ַ�������������һ��Ψһ��ռλ��
                String placeholder = "%%PLACEHOLDER_" + (placeholderCounter++) + "%%";
                placeholders.put(placeholder, matchedString);
                matcher.appendReplacement(buffer, placeholder);
            }
            matcher.appendTail(buffer);
            String modifiedLine = buffer.toString();

            // �滻�ȺźͼӼ��˳�������Ŀո�
            modifiedLine = operatorPattern.matcher(modifiedLine).replaceAll("$1");

            // ��ռλ���滻���ַ���������
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                modifiedLine = modifiedLine.replace(entry.getKey(), entry.getValue());
            }

            modifiedCode.append(modifiedLine).append("\n");
        }

        return modifiedCode.toString();
    }
}
