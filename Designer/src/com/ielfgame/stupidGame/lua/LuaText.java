//package com.ielfgame.stupidGame.lua;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Vector;
//
//import org.eclipse.jface.text.Document;
//import org.eclipse.jface.text.IDocument;
//import org.eclipse.jface.text.ITextViewer;
//import org.eclipse.jface.text.contentassist.CompletionProposal;
//import org.eclipse.jface.text.contentassist.ContentAssistant;
//import org.eclipse.jface.text.contentassist.ICompletionProposal;
//import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
//import org.eclipse.jface.text.contentassist.IContentAssistant;
//import org.eclipse.jface.text.contentassist.IContextInformation;
//import org.eclipse.jface.text.contentassist.IContextInformationValidator;
//import org.eclipse.jface.text.source.ISourceViewer;
//import org.eclipse.jface.text.source.SourceViewer;
//import org.eclipse.jface.text.source.SourceViewerConfiguration;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.LineStyleEvent;
//import org.eclipse.swt.custom.LineStyleListener;
//import org.eclipse.swt.custom.StyleRange;
//import org.eclipse.swt.custom.StyledText;
//import org.eclipse.swt.custom.VerifyKeyListener;
//import org.eclipse.swt.events.VerifyEvent;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//
///**
// * @author zju_wjf
// * 
// */
//public class LuaText extends Composite {
//
//	private final static ArrayList<String> sTipTexts = new ArrayList<String>();
//
//	private final static ArrayList<ArrayList<String>> sKeyWordSets = new ArrayList<ArrayList<String>>();
//	private final static ArrayList<Color> sKeyColors = new ArrayList<Color>();
//	
//	public final static void replaceKeyWorlds(ArrayList<String> keyWords) {
//		sTipTexts.removeAll(sKeyWordSets.get(2));
//		
//		sKeyWordSets.get(2).clear();
//		sKeyWordSets.get(2).addAll(keyWords);
//		
//		sTipTexts.addAll(keyWords);
//	}
//
//	static {
//		final ArrayList<String> set0 = new ArrayList<String>();
//		set0.add("int");
//		set0.add("float");
//		set0.add("String");
//		set0.add("IntervalAction");
//		set0.add("FiniteTimeAction");
//		set0.add("Action");
//		set0.add("Object");
//		set0.add("CCBezierConfig");
//		
//		sKeyColors.add(new Color(null, 63, 127, 95));
//		sKeyWordSets.add(set0);
//
//		//
//		final ArrayList<String> set1 = new ArrayList<String>();
//		set1.add("CCHashElement");
//		set1.add("CCActionManager");
//		set1.add("CCCocosActionTag");
//		set1.add("CCAction");
//		set1.add("CCFiniteTimeAction");
//		set1.add("CCRepeatForever");
//		set1.add("CCSpeed");
//		set1.add("CCCameraAction");
//		set1.add("CCOrbitCamera");
//		set1.add("CCEaseAction");
//		set1.add("CCEaseBackIn");
//		set1.add("CCEaseBackInOut");
//		set1.add("CCEaseBackOut");
//		set1.add("CCEaseBounce");
//		set1.add("CCEaseBounceIn");
//		set1.add("CCEaseBounceInOut");
//		set1.add("CCEaseBounceOut");
//		set1.add("CCEaseElastic");
//		set1.add("CCEaseElasticIn");
//		set1.add("CCEaseElasticInOut");
//		set1.add("CCEaseElasticOut");
//		set1.add("CCEaseExponentialIn");
//		set1.add("CCEaseExponentialInOut");
//		set1.add("CCEaseExponentialOut");
//		set1.add("CCEaseIn");
//		set1.add("CCEaseInOut");
//		set1.add("CCEaseOut");
//		set1.add("CCEaseRateAction");
//		set1.add("CCEaseSineIn");
//		set1.add("CCEaseSineInOut");
//		set1.add("CCEaseSineOut");
//		set1.add("CCCallFunc");
//		set1.add("CCCallFuncN");
//		set1.add("CCCallFuncND");
//		set1.add("CCHide");
//		set1.add("CCInstantAction");
//		set1.add("CCPlace");
//		set1.add("CCShow");
//		set1.add("CCToggleVisibility");
//		set1.add("CCBezierBy");
//		set1.add("CCBlink");
//		set1.add("CCDelayTime");
//		set1.add("CCFadeIn");
//		set1.add("CCFadeOut");
//		set1.add("CCFadeTo");
//		set1.add("CCReverseActionNotImplementedException");
//		set1.add("CCIntervalAction");
//		set1.add("CCJumpBy");
//		set1.add("CCJumpTo");
//		set1.add("CCMoveBy");
//		set1.add("CCMoveTo");
//		set1.add("CCRepeat");
//		set1.add("CCReverseTime");
//		set1.add("CCRotateBy");
//		set1.add("CCRotateTo");
//		set1.add("CCScaleBy");
//		set1.add("CCScaleTo");
//		set1.add("CCSequence");
//		set1.add("CCSpawn");
//		set1.add("CCTintBy");
//		set1.add("CCTintTo");
//		set1.add("CCSchedulerTimerAlreadyScheduled");
//		set1.add("CCSchedulerTimerNotFound");
//		set1.add("CCTimer");
//		set1.add("CCScheduler");
//
//		sKeyColors.add(new Color(null, 135, 49, 184));
//		sKeyWordSets.add(set1);
//		
//		sTipTexts.add("CCActionManager:sharedManager()");
//		sTipTexts.add("CCAction:action()");
//		sTipTexts.add("CCFiniteTimeAction:action(float d)");
//		sTipTexts.add("CCRepeatForever:action(IntervalAction action)");
//		sTipTexts.add("CCSpeed:action(Action action, float r)");
//		sTipTexts.add("CCOrbitCamera:action(float t, float r, float dr, float z, float dz, float x, float dx)");
//		sTipTexts.add("CCEaseBackIn:action(IntervalAction action)");
//		sTipTexts.add("CCEaseBackInOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseBackOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseBounceIn:action(IntervalAction action)");
//		sTipTexts.add("CCEaseBounceInOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseBounceOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseElasticIn:action(IntervalAction action)");
//		sTipTexts.add("CCEaseElasticInOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseElasticOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseExponentialIn:action(IntervalAction action)");
//		sTipTexts.add("CCEaseExponentialInOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseExponentialOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseIn:action(IntervalAction action, float rate)");
//		sTipTexts.add("CCEaseInOut:action(IntervalAction action, float rate)");
//		sTipTexts.add("CCEaseOut:action(IntervalAction action, float rate)");
//		sTipTexts.add("CCEaseSineIn:action(IntervalAction action)");
//		sTipTexts.add("CCEaseSineInOut:action(IntervalAction action)");
//		sTipTexts.add("CCEaseSineOut:action(IntervalAction action)");
//		sTipTexts.add("CCCallFunc:action(Object target, String selector)");
//		sTipTexts.add("CCCallFuncN:action(Object t, String s)");
//		sTipTexts.add("CCCallFuncND:action(Object t, String s, Object d)");
//		sTipTexts.add("CCHide:action()");
//		sTipTexts.add("CCPlace:action(float x, float y)");
//		sTipTexts.add("CCShow:action()");
//		sTipTexts.add("CCToggleVisibility:action()");
//		sTipTexts.add("CCBezierBy:action(float t, CCBezierConfig c)");
//		sTipTexts.add("CCBlink:action(float t, int b)");
//		sTipTexts.add("CCDelayTime:action(float t)");
//		sTipTexts.add("CCFadeIn:action(float t)");
//		sTipTexts.add("CCFadeOut:action(float t)");
//		sTipTexts.add("CCFadeTo:action(float t, float a)");
//		sTipTexts.add("CCJumpBy:action(float time, float x, float y, float height, int jumps)");
//		sTipTexts.add("CCJumpTo:action(float time, float x, float y, float height, int jumps)");
//		sTipTexts.add("CCMoveBy:action(float t, float x, float y)");
//		sTipTexts.add("CCMoveTo:action(float t, float x, float y)");
//		sTipTexts.add("CCRepeat:action(FiniteTimeAction action, int t)");
//		sTipTexts.add("CCReverseTime:action(FiniteTimeAction action)");
//		sTipTexts.add("CCRotateBy:action(float t, float a)");
//		sTipTexts.add("CCRotateTo:action(float t, float a)");
//		sTipTexts.add("CCScaleBy:action(float t, float s)");
//		sTipTexts.add("CCScaleTo:action(float t, float s)");
//		sTipTexts.add("CCSequence:actions(FiniteTimeAction action1, FiniteTimeAction... actions)");
//		sTipTexts.add("CCSpawn:actions(FiniteTimeAction action1, FiniteTimeAction... params)");
//		sTipTexts.add("CCTintBy:action(float t, int r, int g, int b)");
//		sTipTexts.add("CCTintTo:action(float t, int r, int g, int b)");
//		sTipTexts.add("CCScheduler:sharedScheduler()");
//		
//		sTipTexts.add("copy()");
//		sTipTexts.add("reverse()");
//		
//		final ArrayList<String> set2 = new ArrayList<String>();
//		sKeyWordSets.add(set2);
//		sKeyColors.add(new Color(null, 127, 0, 116));
//		
//		final ArrayList<String> set3 = new ArrayList<String>();
//		set3.add("function");
//		set3.add("end");
//		set3.add("then");
//		set3.add("if");
//		set3.add("ifelse");
//		set3.add("else");
//		set3.add("return");
//		set3.add("local");
//		sTipTexts.addAll(set3);
//		sKeyWordSets.add(set3);
//		sKeyColors.add(new Color(null, 127, 90, 116));
//	}
//
//	public LuaText(Composite parent) {
//		super(parent, SWT.NONE);
//
//		final SourceViewer sourceViewer_source = new SourceViewer(this, null, SWT.V_SCROLL | SWT.BORDER);
//		final StyledText styledText = sourceViewer_source.getTextWidget();
//		styledText.setLayout(new GridLayout());
//		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//
//		sourceViewer_source.setDocument(new Document(""));
//		sourceViewer_source.setEditable(true);
//
//		sourceViewer_source.getTextWidget().addLineStyleListener(new StyleListener());
//
//		sourceViewer_source.configure(new SourceViewerConfiguration() {
//			public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
//				ContentAssistant assistant = new ContentAssistant();
//				IContentAssistProcessor cap = new MyContentAssistProcessor();
//				assistant.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
//				assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
//				assistant.enableAutoActivation(true);
//				return assistant;
//			}
//		});
//
//		sourceViewer_source.appendVerifyKeyListener(new VerifyKeyListener() {
//			public void verifyKey(VerifyEvent event) {
//				if (event.stateMask == SWT.ALT && event.character == '/') {
//					if (sourceViewer_source.canDoOperation(SourceViewer.CONTENTASSIST_PROPOSALS))
//						sourceViewer_source.doOperation(SourceViewer.CONTENTASSIST_PROPOSALS);
//				}
//			}
//		});
//
//	}
//
//	class StyleListener implements LineStyleListener {
//
//		public StyleListener() {
//			super();
//
//		}
//
//		@Override
//		public void lineGetStyle(LineStyleEvent event) {
//			List<StyleRange> styles = new ArrayList<StyleRange>();
//			int start = 0;
//			int length = event.lineText.length();
//			while (start < length) {
//				if (Character.isLetter(event.lineText.charAt(start))) {
//					StringBuffer buf = new StringBuffer();
//					int i = start;
//					for (; i < length && 
//							( Character.isLetter(event.lineText.charAt(i)) || 
//									Character.isDigit(event.lineText.charAt(i)) ||
//									event.lineText.charAt(i) == '_' ||
//									event.lineText.charAt(i) == '@' ||
//									event.lineText.charAt(i) == '#'); i++) {
//						buf.append(event.lineText.charAt(i));
//					}
//
//					for (int k = 0; k < sKeyWordSets.size(); k++) {
//						final ArrayList<String> set = sKeyWordSets.get(k);
//						final Color color = sKeyColors.get(k);
//
//						if (set.contains(buf.toString())) {
//							styles.add(new StyleRange(event.lineOffset + start, i - start, color, null, SWT.BOLD));
//							break;
//						}
//					}
//
//					start = i;
//				} else {
//					start++;
//				}
//			}
//			event.styles = (StyleRange[]) styles.toArray(new StyleRange[0]);
//		}
//
//	}
//
//	class MyContentAssistProcessor implements IContentAssistProcessor {
//		public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
//			try {
//
//				String[] options = new String[sTipTexts.size()];
//				sTipTexts.toArray(options);
//
//				Arrays.sort(options);
//
//				String text = viewer.getTextWidget().getText();
//				Point point = viewer.getTextWidget().getSelection();
//				try {
//					text = text.substring(0, (point.x + point.y) / 2);
//				} catch (Exception e) {
//				}
//
//				if (text != null && text.length() > 0) {
//					int p = text.lastIndexOf(" ");
//					p = Math.max(p, text.lastIndexOf("\n"));
//					p = Math.max(p, text.lastIndexOf("."));
//					p = Math.max(p, text.lastIndexOf(":"));
//					if (p != -1) {
//						text = text.substring(p + 1);
//					}
//					// List<String> s = new ArrayList<String>();
//					// HashMap<String, String> h = new HashMap<String,
//					// String>();
//					Vector<String> v = new Vector<String>(1);
//					for (String option : options) {
//						if (text.length() < option.length() && option.substring(0, text.length()).equalsIgnoreCase(text)) {
//							// v.add(option.substring(text.length()));
//							v.add(option);
//						}
//					}
//					if (v.size() > 0) {
//						options = v.toArray(new String[v.size()]);
//					} else {
//						return null;
//					}
//				}
//
//				// Dynamically generate proposal
//				ArrayList<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
//				for (int i = 0; i < options.length; i++) {
//					// int len = options[i].length() - text.length();
//					CompletionProposal proposal = new CompletionProposal(options[i], offset - text.length(), text.length(), options[i].length());
//					result.add(proposal);
//				}
//
//				return (ICompletionProposal[]) result.toArray(new ICompletionProposal[result.size()]);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return null;
//		}
//
//		public char[] getCompletionProposalAutoActivationCharacters() {
//			return new char[] { 46 };// ��"."
//		}
//
//		public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
//			return null;
//		}
//
//		public char[] getContextInformationAutoActivationCharacters() {
//			// return new char [] {','};
//			return null;
//		}
//
//		public IContextInformationValidator getContextInformationValidator() {
//			return null;
//		}
//
//		public String getErrorMessage() {
//			return "error";
//		}
//	}
//}