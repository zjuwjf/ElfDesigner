package com.ielfgame.stupidGame.data;

public class IsChineseOrNot {
	// GENERAL_PUNCTUATION �ж����ĵġ���
	// CJK_SYMBOLS_AND_PUNCTUATION �ж����ĵġ���
	// HALFWIDTH_AND_FULLWIDTH_FORMS �ж����ĵģ���
	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static final boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(isChinese("��"));
		System.out.println(isChinese("test,.?!%^&*(){}[]"));
		System.out.println(isChinese("����"));
		System.out.println(isChinese("�����ԡ���������%����&*��������{}������"));
		System.out.println(isChinese("����%����&*��������{}������"));
	}
	
//	public static final boolean isChineseCharacter(String chineseStr) {
//		char[] charArray = chineseStr.toCharArray();
//		for (int i = 0; i < charArray.length; i++) {
//			if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
//				return true;
//			}
//		}
//		return false;
//	}

//	/**
//	 * @deprecated; ���á��ͷ���isChineseCharacter��Ч��̫�͡�
//	 * */
//	@Deprecated
//	public static final boolean isChineseCharacter_f2() {
//		String str = "����";
//		for (int i = 0; i < str.length(); i++) {
//			if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fbb]+")) {
//				return true;
//			}
//		}
//		return false;
//	} 
}
