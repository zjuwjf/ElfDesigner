package elfEngine.extend;

public class StringUtil {
	public static final String removeSubfix(String filePath) {
		return filePath.substring(0, filePath.lastIndexOf("."));
	}

	public static final String replaceSubfix(String filePath, String newSubfix) {
		return new StringBuilder(removeSubfix(filePath)).append(".")
				.append(newSubfix).toString();
	}
}
