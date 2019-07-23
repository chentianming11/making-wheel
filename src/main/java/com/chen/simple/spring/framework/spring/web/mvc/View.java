package com.chen.simple.spring.framework.spring.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public class View {

    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

    private static Pattern pattern = Pattern.compile("#\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);

    File viewFile;

    public View(File viewFile) {
        this.viewFile = viewFile;
    }

    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }


    public void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) {
        try {

            Map<String, Object> model = mv.getModel();
            StringBuffer sb = new StringBuffer();
            RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");
            String line;
            while (null != (line = ra.readLine())) {
                line = new String(line.getBytes("ISO-8859-1"), "utf-8");

                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("#\\{|\\}", "");
                    Object paramValue = model.get(paramName);
                    if (null == paramValue) {
                        continue;
                    }
                    // 要把#{}中间的这个字符串给取出来
                    line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }
                sb.append(line);
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType(DEFAULT_CONTENT_TYPE);
            response.getWriter().write(sb.toString());
            if (mv.getStatus() != null) {
                response.setStatus(mv.getStatus().value());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String makeStringForRegExp(String str) {
        return str.replace("\\", "\\\\")
                .replace("*", "\\*")
                .replace("+", "\\+")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("?", "\\?")
                .replace(",", "\\,")
                .replace(".", "\\.")
                .replace("&", "\\&");
    }
}
