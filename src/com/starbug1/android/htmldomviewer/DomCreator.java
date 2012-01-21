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
	private StringBuffer sb_ = new StringBuffer();
	
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
	
	private String getIndent() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < level_; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		registerTestNodes();

		StringBuffer attrs = new StringBuffer();
		for(int i = 0; i < atts.getLength(); i++)
	        attrs.append(" " + atts.getQName(i) + "=\"" + atts.getValue(i) + "\"");
		Log.d("DomCreator", getIndent() + "<" + qName + attrs.toString() + ">");
        builder_.sequentiallyAddNextNode(new Element(currentId_++, "<" + qName + attrs.toString() + ">"), level_);
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
	        builder_.sequentiallyAddNextNode(new Element(currentId_++, " " + line), level_);
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		registerTestNodes();
		
		level_--;
		Log.d("DomCreator", getIndent() + "</" + qName + ">");
        builder_.sequentiallyAddNextNode(new Element(currentId_++, "</" + qName + ">"), level_);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length).trim();
		if (s.length() == 0) return;
		
		Log.d("DomCreator", getIndent() + " " + s);
		sb_.append(s);
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

}
