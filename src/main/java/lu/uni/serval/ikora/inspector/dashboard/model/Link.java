package lu.uni.serval.ikora.inspector.dashboard.model;

import lu.uni.serval.ikora.core.utils.StringUtils;

import java.io.UnsupportedEncodingException;

public class Link {
    private final String text;
    private final String url;

    public Link(String name) throws UnsupportedEncodingException {
        text = StringUtils.lineTruncate(StringUtils.toBeautifulName(name), 20);
        url = StringUtils.toBeautifulUrl(name, "html");
    }

    public String getText(){
        return text;
    }

    public String getUrl(){
        return url;
    }
}
