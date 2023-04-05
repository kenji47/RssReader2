package com.kenji1947.rssreader.ui.converter;

import java.util.Date;

public final class ConverterTestData {

    public static final long TEST_LONG_ID_1 = 101L;
    public static final long TEST_LONG_ID_2 = 102L;

    public static final long TEST_LONG = 123L;
    public static final long TEST_DATE_LONG = 1024L;

    public static final Date TEST_DATE = new Date(TEST_DATE_LONG);

    public static final String TEST_STRING_TITLE_1 = "this is first title";
    public static final String TEST_STRING_TITLE_2 = "this is second title";
    public static final String TEST_IMAGE_URL = "http://image.url/picture";
    public static final String TEST_IMAGE_URL_SLASH_AT_THE_END = "http://image.url/picture/";
    public static final String TEST_DESCRIPTION_STRING = "this is a description string";
    public static final String TEST_BASIC_URL_STRING = "https://xkcd.com/";
    public static final String TEST_SMALL_STRING = "small string";
    public static final String TEST_COMPLEX_URL_STRING_1 = "https://xkcd.com/rss.xml";
    public static final String TEST_COMPLEX_URL_STRING_2 = "https://xkcd.com/rss2.xml";
}
