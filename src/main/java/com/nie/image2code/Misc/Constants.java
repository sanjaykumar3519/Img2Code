package com.nie.image2code.Misc;

public class Constants {
    public static final String CODE_FORMATTER_REQUEST_BODY = "{\"language\": \"java\",\"codeSrc\": \"{{code}}\",\"style\": \"Google\",\"indentWidth\": \"2\",\"columnLimit\": \"80\"}";
    public static final String CODE_FORMATTER_URL = "https://formatter.org/admin/format";
    public static final String CV_DESTINATION = "D://grey.jpg";
    public static final String CV_SOURCE = "D://testImg2.jpg";
    public static final String OUTPUT_PATH = "D://img2code.jpg";
    public static final String RESPONSE_BODY = "{\"code\":\"$code\"}";
    public static final String WELCOME = "==========WELCOME TO IMAGE TO CODE CONVERTER=========";
}
