package com.github.harryssuperman.fxdatepicker.themes;

public final class DatePickerTheme {
    private DatePickerTheme() {
    }

    public static String defaultStylesheet() {
        return DatePickerTheme.class.getResource("enhanced-date-picker.css").toExternalForm();
    }
}
