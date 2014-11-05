package elfEngine.basic.node.nodeText.richLabel;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class RichTextHelper {

	private static final char TAG_START = '[';
	private static final char TAG_END = ']';

	public enum SpanType {
		UNKNOWN, COLOR,

		FONT, SIZE, BOLD, ITALIC,

		UNDERLINE, IMAGE, LINK;

		public boolean isFontType() {
			return this == FONT || this == SIZE || this == BOLD || this == ITALIC;
		}
	}

	// link meta info
	public final static class LinkMeta {
		int normalBgColor;
		int selectedBgColor;

		// the tag of link, multiple link can have same tag (in line break
		// situation)
		int tag;

		// link rect area
		float x;
		float y;
		float width;
		float height;
	}

	public final static class FontSpan {
		public String fontName = null;
		public float fontSize = -1;
		public int fontStyle = 0;

		public int pos;
		public int endPos;

		public FontSpan copy() {
			final FontSpan copy = new FontSpan();
			copy.fontName = this.fontName;
			copy.fontSize = this.fontSize;
			copy.fontStyle = this.fontStyle;
			return copy;
		}

		public String toString() {
			return "name=" + fontName + ", size=" + fontSize + ", style=" + fontStyle + ", pos=" + pos + "," + endPos;
		}

		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			} else if (obj instanceof FontSpan) {
				final FontSpan font = (FontSpan) obj;
				return this.fontName == font.fontName && Math.abs(this.fontSize - font.fontSize) < 0.01f && this.fontStyle == font.fontStyle;
			}
			return false;
		}

		public static FontSpan add(FontSpan fontSpan1, FontSpan fontSpan2) {
			final FontSpan fontSpan3 = new FontSpan();
			fontSpan3.fontName = fontSpan2.fontName != null ? fontSpan2.fontName : fontSpan1.fontName;
			fontSpan3.fontSize = fontSpan2.fontSize > 0 ? fontSpan2.fontSize : fontSpan1.fontSize;
			fontSpan3.fontStyle = fontSpan2.fontStyle | fontSpan1.fontStyle;

			return fontSpan3;
		}

		public static FontSpan create(Span span) {
			FontSpan ret = new FontSpan();
			if (span.type == SpanType.BOLD) {
				ret.fontStyle = Font.BOLD;
			} else if (span.type == SpanType.ITALIC) {
				ret.fontStyle = Font.ITALIC;
			} else if (span.type == SpanType.FONT) {
				ret.fontName = span.fontName;
			} else if (span.type == SpanType.SIZE) {
				ret.fontSize = span.fontSize;
			} else {
				ret = null;
			}
			return ret;
		}
	}

	// span info
	public final static class Span {
		public SpanType type;
		public boolean close;
		public int pos;
		public int endPos;

		// only for color
		public int color;

		// only for size
		public float fontSize;

		// only for font
		public String fontName;

		// only for image
		public String imageName;
		public float scaleX;
		public float scaleY;
		public float width;
		public float height;

		// for link tag
		public int normalBgColor;
		public int selectedBgColor;

	}

	// tag parse result
	public final static class TagParseResult {
		SpanType type;
		boolean close;
		int endTagPos;
		int dataStart;
		int dataEnd;
	}

	// tag parse state
	public enum TagParseState {
		READY, START_TAG, CLOSE_TAG, EQUAL, SUCCESS, FAIL
	}

	private static final void addNewFont(final FontSpan[] fontArray, Span newFont) {
		final int size = fontArray.length;
		for (int i = 0; i < size; i++) {
			if (i >= newFont.pos && i < newFont.endPos) {
				final FontSpan oldf = fontArray[i];
				final FontSpan newf = FontSpan.create(newFont);
				if (oldf == null) {
					fontArray[i] = newf;
				} else {
					fontArray[i] = FontSpan.add(oldf, newf);
				}
			}
		}
	}

	private final static void cpmpressFontArray(final FontSpan[] fontArray) {
		FontSpan lastFont = null;
		for (int i = 0; i < fontArray.length; i++) {
			final FontSpan currFont = fontArray[i];

			if (lastFont == null) {
				currFont.pos = i;
				currFont.endPos = i + 1;
				lastFont = currFont;
			} else {
				if (lastFont.equals(currFont)) {
					lastFont.endPos = lastFont.endPos + 1;
					// fontArray[i] = null;
				} else {
					currFont.pos = i;
					currFont.endPos = i + 1;

					lastFont = currFont;
				}
			}
		}

//		System.err.println("-------------");
//		for (int i = 0; i < fontArray.length; i++) {
//			System.err.println("i=" + i + ":" + fontArray[i].toString());
//		}
//		System.err.println("-------------");
	}

	public static ArrayList<Span> makeSpanPairs(List<Span> plist, final String string, final FontSpan[] fontArray) {
		final ArrayList<Span> list = new ArrayList<Span>();
		for (int i = 0; i < plist.size(); i++) {
			list.add(0, plist.get(i));
		}

		final ArrayList<Span> ret = new ArrayList<Span>();

		while (!list.isEmpty()) {
			final int size = list.size();
			final Span tail = list.get(0);
			list.remove(0);

			if (tail.close) {
				for (int i = 0; i < size - 1; i++) {
					final Span head = list.get(i);
					if (tail.type == head.type && !head.close) {
						head.endPos = tail.pos;
						list.remove(head);

						if (head.type.isFontType()) {
							addNewFont(fontArray, head);
							ret.add(head);
						} else {
							ret.add(head);
						}

						break;
					}
				}
			}
		}

		cpmpressFontArray(fontArray);
		/***
		 * make fonts font + size + style
		 */
		final ArrayList<Span> retArry = new ArrayList<Span>();
		for (Span span : ret) {
			retArry.add(0, span);
		}

		return retArry;
	}

	public static void main(final String[] args) {
		List<Span> spans = new ArrayList<Span>();
		String plain = buildSpan("[b]h[/b]el[image xx.png]abc[/image]lo[b][/b](", spans, true);
		final FontSpan defaultFontSpan = new FontSpan();
		defaultFontSpan.fontName = "";
		defaultFontSpan.fontSize = 40;
		defaultFontSpan.fontStyle = 0;
		defaultFontSpan.endPos = 1;

		final FontSpan[] fontArray = new FontSpan[plain.length()];
		for (int i = 0; i < fontArray.length; i++) {
			fontArray[i] = defaultFontSpan;
		}

		final List<Span> pairs = makeSpanPairs(spans, plain, fontArray);

		System.err.println(plain);
		for (Span s : spans) {
			System.err.println("[]=" + s.type.toString() + ", pos=" + s.pos + ", " + s.endPos + "," + s.close);
		}
		System.err.println("-------------------");
		for (Span s : pairs) {
			System.err.println("[]=" + s.type.toString() + ", pos=" + s.pos + ", " + s.endPos + "," + s.close);
		}
	}

	// if parse failed, endTagPos will be len, otherwise it is end tag position
	private static TagParseResult checkTag(String p, int start) {
		TagParseResult r = new TagParseResult();
		r.type = SpanType.UNKNOWN;
		TagParseState state = TagParseState.READY;
		int tagNameStart = 0, tagNameEnd = 0;
		r.close = false;
		int len = p.length();
		r.endTagPos = len;
		r.dataStart = -1;
		int i = start;
		while (i < len) {
			switch (state) {
			case READY:
				if (p.charAt(i) == TAG_START) {
					state = TagParseState.START_TAG;
					tagNameStart = ++i;
				} else {
					state = TagParseState.FAIL;
				}
				break;
			case START_TAG:
				if ((i == start + 1) && p.charAt(i) == '/') {
					state = TagParseState.CLOSE_TAG;
					r.close = true;
					tagNameStart = ++i;
				} else if (p.charAt(i) == '=') {
					state = TagParseState.EQUAL;
					tagNameEnd = i++;
					r.type = checkTagName(p, tagNameStart, tagNameEnd);
					r.dataStart = i;
				} else if (p.charAt(i) == TAG_END) {
					state = TagParseState.SUCCESS;
					r.endTagPos = i;
					r.dataEnd = i;
					tagNameEnd = i;
					if (r.type == SpanType.UNKNOWN) {
						r.type = checkTagName(p, tagNameStart, tagNameEnd);
					}
				} else if (p.charAt(i) == ' ') {
					state = TagParseState.EQUAL;
					tagNameEnd = i++;
					r.type = checkTagName(p, tagNameStart, tagNameEnd);
					r.dataStart = i;
				} else {
					i++;
				}
				break;
			case CLOSE_TAG:
				if (p.charAt(i) == TAG_END) {
					state = TagParseState.SUCCESS;
					r.endTagPos = i;
					tagNameEnd = i;
					r.type = checkTagName(p, tagNameStart, tagNameEnd);
				} else {
					i++;
				}
				break;
			case EQUAL:
				if (p.charAt(i) == TAG_END) {
					state = TagParseState.SUCCESS;
					r.endTagPos = i;
					r.dataEnd = i;
				} else {
					i++;
				}
				break;
			default:
				break;
			}

			if (state == TagParseState.FAIL || state == TagParseState.SUCCESS)
				break;
		}

		if (state != TagParseState.SUCCESS)
			r.type = SpanType.UNKNOWN;

		return r;
	}

	private static SpanType checkTagName(String p, int start, int end) {
		int len = end - start;
		switch (len) {
		case 1:
			if (p.charAt(start) == 'b') {
				return SpanType.BOLD;
			} else if (p.charAt(start) == 'i') {
				return SpanType.ITALIC;
			} else if (p.charAt(start) == 'u') {
				return SpanType.UNDERLINE;
			}
			break;
		case 4:
			if (p.charAt(start) == 'f' && p.charAt(start + 1) == 'o' && p.charAt(start + 2) == 'n' && p.charAt(start + 3) == 't') {
				return SpanType.FONT;
			} else if (p.charAt(start) == 's' && p.charAt(start + 1) == 'i' && p.charAt(start + 2) == 'z' && p.charAt(start + 3) == 'e') {
				return SpanType.SIZE;
			} else if (p.charAt(start) == 'l' && p.charAt(start + 1) == 'i' && p.charAt(start + 2) == 'n' && p.charAt(start + 3) == 'k') {
				return SpanType.LINK;
			}
		case 5:
			if (p.charAt(start) == 'c' && p.charAt(start + 1) == 'o' && p.charAt(start + 2) == 'l' && p.charAt(start + 3) == 'o' && p.charAt(start + 4) == 'r') {
				return SpanType.COLOR;
			} else if (p.charAt(start) == 'i' && p.charAt(start + 1) == 'm' && p.charAt(start + 2) == 'a' && p.charAt(start + 3) == 'g' && p.charAt(start + 4) == 'e') {
				return SpanType.IMAGE;
			}
			break;

		}

		return SpanType.UNKNOWN;
	}

	public static String buildSpan(String text, List<Span> spans, boolean richOrPool) {
		
		if(!richOrPool) {
			return text;
		}
		
		int uniLen = text.length();
		StringBuilder plain = new StringBuilder();
		
		try {
			for (int i = 0; i < uniLen; i++) {
				char c = text.charAt(i);
				switch (c) {
				case '\\':
					if (i < uniLen - 1) {
						char cc = text.charAt(i + 1);
						if (cc == '[' || cc == ']') {
							plain.append(cc);
							i++;
						}
					} else {
						plain.append(c);
					}
					break;
				case TAG_START: {
					// parse the tag
					Span span = new Span();
					TagParseResult r = checkTag(text, i);

					// fill span info
					do {
						// if type is unknown, discard
						if (r.type == SpanType.UNKNOWN)
							break;

						// parse span data
						span.type = r.type;
						span.close = r.close;
						span.pos = plain.length();
						if (!r.close) {
							switch (span.type) {
							case COLOR:
								span.color = parseColor(text, r.dataStart, r.dataEnd - r.dataStart);
								break;
							case FONT:
								span.fontName = text.substring(r.dataStart, r.dataEnd);
								break;
							case SIZE:
								try {
									span.fontSize = Integer.parseInt(text.substring(r.dataStart, r.dataEnd));
								} catch (NumberFormatException e) {
									span.fontSize = 16;
								}
								break;
							case IMAGE: {
								String name = text.substring(r.dataStart, r.dataEnd);
								String[] parts = name.split(" ");
								span.imageName = parts[0];
								span.scaleX = span.scaleY = 1;
								span.width = span.height = 0;

								// if parts more than one, parse attributes
								if (parts.length > 1) {
									for (int j = 1; j < parts.length; j++) {
										String[] pair = parts[j].split("=");
										if (pair.length == 2) {
											try {
												if ("scale".equals(pair[0])) {
													span.scaleX = span.scaleY = Float.parseFloat(pair[1]);
												} else if ("scalex".equals(pair[0])) {
													span.scaleX = Float.parseFloat(pair[1]);
												} else if ("scaley".equals(pair[0])) {
													span.scaleY = Float.parseFloat(pair[1]);
												} else if ("w".equals(pair[0])) {
													span.width = Float.parseFloat(pair[1]);
												} else if ("h".equals(pair[0])) {
													span.height = Float.parseFloat(pair[1]);
												}
											} catch (NumberFormatException e) {
											}
										}
									}
								}

								break;
							}
							case LINK: {
								String v = text.substring(r.dataStart, r.dataEnd);
								String[] parts = v.split(" ");
								for (String part : parts) {
									String[] pair = part.split("=");
									if (pair.length == 2) {
										if ("bg".equals(pair[0])) {
											span.normalBgColor = parseColor(pair[1], 0, pair[1].length());
										} else if ("bg_click".equals(pair[0])) {
											span.selectedBgColor = parseColor(pair[1], 0, pair[1].length());
										}
									}
								}
								break;
							}
							default:
								break;
							}
						}

						// add span
						spans.add(span);

						// set i pos
						i = r.endTagPos;
					} while (false);

					break;
				}
				case TAG_END:
					// just discard it
					break;
				default:
					plain.append(c);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// return plain str
		return plain.toString();
	}

	private static int parseColor(String text, int start, int len) {
		int color = 0;
		int end = start + len;
		for (int i = start; i < end; i++) {
			color <<= 4;
			char c = text.charAt(i);
			if (c >= '0' && c <= '9') {
				color |= c - '0';
			} else if (c >= 'a' && c <= 'f') {
				color |= c - 'a' + 10;
			} else if (c >= 'A' && c <= 'F') {
				color |= c - 'A' + 10;
			}
		}

		return color;
	}
}
