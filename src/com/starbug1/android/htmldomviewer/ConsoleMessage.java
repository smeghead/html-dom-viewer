package com.starbug1.android.htmldomviewer;

public class ConsoleMessage {
	private String sourceID_;
	private int lineNumber_;
	private String message_;
	
	public ConsoleMessage(String message, int lineNumber, String sourceID) {
		message_ = message;
		lineNumber_ = lineNumber;
		sourceID_ = sourceID;
	}

	@Override
	public String toString() {
		return sourceID_ + "(" + lineNumber_ + ") " + message_;
	}
	public String getSourceID() {
		return sourceID_;
	}

	public void setSourceID(String sourceID) {
		sourceID_ = sourceID;
	}

	public int getLineNumber() {
		return lineNumber_;
	}

	public void setLineNumber(int lineNumber) {
		lineNumber_ = lineNumber;
	}

	public String getMessage() {
		return message_;
	}

	public void setMessage(String message) {
		message_ = message;
	}
}
