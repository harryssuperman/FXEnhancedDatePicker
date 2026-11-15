package com.github.harryssuperman.fxdatepicker.themes;

public enum DatePickerTheme {
    DEFAULT("enhanced-date-picker.css", false),
    WITH_WEEKDAY("enhanced-date-picker-weekday.css", true);

    private final String stylesheet;
    private final boolean showWeekday;

    DatePickerTheme(String stylesheet, boolean showWeekday) {
        this.stylesheet = stylesheet;
        this.showWeekday = showWeekday;
    }

    public String stylesheet() {
        return DatePickerTheme.class.getResource(stylesheet).toExternalForm();
    }

    public boolean showsWeekday() {
        return showWeekday;
    }

    public static String defaultStylesheet() {
        return DEFAULT.stylesheet();
    }
}
