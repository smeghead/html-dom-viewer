package com.starbug1.android.htmldomviewer;

import java.io.StringReader;

import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import pl.polidea.treeview.TreeBuilder;
import android.util.Log;

public class DomCreator implements ContentHandler {
	private TreeBuilder<Element> builder_ = null;

	public DomCreator(TreeBuilder<Element> builder) {
		this.builder_ = builder;
	}

	private int level_ = 0;
	private int maxLevel_ = 0;
	private long currentId_ = 0;
	private final StringBuffer sb_ = new StringBuffer();

	private static final String COLOR_TAG = "#efef8f";
	private static final String COLOR_TAGNAME = "#e3ceab";
	private static final String COLOR_STRING = "#cc9393";
	private static final String COLOR_ATTR = "#dfdfbf";

	public int parse(StringReader is) {
		level_ = 0;
		currentId_ = 0;
		Parser parser = new Parser();
		try {
			parser.setContentHandler(this);
			parser.setFeature(Parser.namespacesFeature, false);
			parser.parse(new InputSource(is));
			return maxLevel_;
		} catch (Exception e) {
			Log.e("DomCreator", e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		registerTestNodes();

		StringBuffer attrs = new StringBuffer();
		for (int i = 0; i < atts.getLength(); i++)
			attrs.append(String
					.format(" <font color=\"%s\"><b>%s</b></font><font color=\"%s\">=</font><font color=\"%s\">&quot;%s&quot;</font>",
							COLOR_ATTR, atts.getQName(i), COLOR_TAG,
							COLOR_STRING, atts.getValue(i)));
		builder_.sequentiallyAddNextNode(
				new Element(
						currentId_++,
						String.format(
								"<font color=\"%s\">&lt;</font><font color=\"%s\">%s</font>%s<font color=\"%s\">&gt;</font>",
								COLOR_TAG, COLOR_TAGNAME, qName,
								attrs.toString(), COLOR_TAG)), level_);
		level_++;
		maxLevel_ = Math.max(maxLevel_, level_);
	}

	private void registerTestNodes() {
		if (sb_.length() == 0) {
			return;
		}
		String s = sb_.toString();
		sb_.delete(0, sb_.length());

		String[] lines = s.split("\n");
		for (String line : lines) {
			builder_.sequentiallyAddNextNode(new Element(currentId_++, " "
					+ line), level_);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		registerTestNodes();

		level_--;
		builder_.sequentiallyAddNextNode(
				new Element(
						currentId_++,
						String.format(
								"<font color=\"%s\">&lt;/</font><font color=\"%s\">%s</font><font color=\"%s\">&gt;</font>",
								COLOR_TAG, COLOR_TAGNAME, qName, COLOR_TAG)),
				level_);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length).trim();
		if (s.length() == 0)
			return;

		sb_.append(s);

	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
	}

	@Override
	public void setDocumentLocator(Locator locator) {
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}

}
