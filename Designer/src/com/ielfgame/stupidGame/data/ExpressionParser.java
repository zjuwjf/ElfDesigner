package com.ielfgame.stupidGame.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
	// ��������...
	public static String parseCurves(String expression) {
		// ����matcher
		final String regex = "\\([^\\(\\)]+\\)";// ƥ��()
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(expression);
		String finalRes = expression;
		while (matcher.find()) {
			String group = matcher.group();
			String result = thenCalculate(priorCalculate(group.substring(1, group.length() - 1)));
			finalRes = matcher.replaceFirst(result);
			matcher.reset(finalRes);
		} 
		return finalRes;
	} 

	public static String priorCalculate(String expression) {
		// ��һ������ļ��㣬�ȼ���*/
		String m_dOperate = "([^\\+\\-\\*/]+)([\\*/])([^\\+\\-\\*/]+)";
		Pattern pattern1 = Pattern.compile(m_dOperate);
		Matcher matcher1 = pattern1.matcher(expression);
		String finalExp = expression;
		while (matcher1.find()) {
			double d1 = Double.parseDouble(matcher1.group(1).trim());
			double d2 = Double.parseDouble(matcher1.group(3).trim());
			String operator = matcher1.group(2);
			String result = "";
			if (operator.equals("*"))
				result = String.valueOf(d1 * d2);

			if (operator.equals("/"))
				result = String.valueOf(d1 / d2);

			finalExp = matcher1.replaceFirst(result);
			matcher1.reset(finalExp);
		}

		return finalExp;

	}
	
	public static String thenCalculate(String expression) {
		// ��һ������ļ��㣬����+-

		String a_sOperate = "([^\\+\\-\\*/]+)([\\+\\-])([^\\+\\-\\*/]+)";
		Pattern pattern2 = Pattern.compile(a_sOperate);
		Matcher matcher2 = pattern2.matcher(expression);
		String finalExp = expression;
		while (matcher2.find()) {
			double d1 = Double.parseDouble(matcher2.group(1).trim());
			double d2 = Double.parseDouble(matcher2.group(3).trim());
			String operator = matcher2.group(2);
			String result = "";
			if (operator.equals("+"))
				result = String.valueOf(d1 + d2);

			if (operator.equals("-"))
				result = String.valueOf(d1 - d2);

			finalExp = matcher2.replaceFirst(result);
			matcher2.reset(finalExp);

		}

		return finalExp;
	}

	public static void main(String [] args) {
		final String expression = "(1+2)*3+(4/2+5*(1+3/2*(2-1)))";
		System.err.println( parseCurves(expression) );
		System.err.println( priorCalculate(parseCurves(expression)) );
		System.err.println( thenCalculate(priorCalculate(parseCurves(expression))) );
	} 
}
