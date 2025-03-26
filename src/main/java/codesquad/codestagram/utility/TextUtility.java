package codesquad.codestagram.utility;

import org.springframework.web.util.HtmlUtils;

public class TextUtility {
    public static String escapeAndConvertNewlines(String content) {
        return HtmlUtils.htmlEscape(content).replace("\n", "<br>");
    }
}
