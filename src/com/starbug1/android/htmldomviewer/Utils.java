package com.starbug1.android.htmldomviewer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static String mobileUrl(String url) {
		String ret = url;
		ret = ret.replaceAll("/dqnplus/", "/dqnplus/lite/");
		ret = ret.replaceAll("/labaq.com/", "/labaq.com/lite/");
		return ret;
	}

	private static Pattern domainPattern_ = Pattern.compile("https?://([^/]*)");
	private static String findDomain(String url) {
		if (url == null) {
			return "";
		}
		Matcher m = domainPattern_.matcher(url);
		if (!m.find()) {
			return "";
		}
		return m.group(1);
	}
	public static boolean isSameDomain(String originalUrl, String url) {
		return findDomain(originalUrl).equals(findDomain(url));
	}
}
