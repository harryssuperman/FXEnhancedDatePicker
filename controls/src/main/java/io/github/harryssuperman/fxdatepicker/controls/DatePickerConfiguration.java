package io.github.harryssuperman.fxdatepicker.controls;

import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public final class DatePickerConfiguration {
    private final Locale locale;
    private final String fontFamily;
    private final double fontSize;
    private final Color textColor;
    private final Color accentColor;
    private final DateTimeFormatter dateFormatter;
    private final ResourceBundle messages;
    private final double selectedDatePanelWidth;
    private final double selectedDatePanelHeight;
    private final double selectionPanelWidth;
    private final double selectionPanelHeight;

    private DatePickerConfiguration(Builder builder) {
        locale = builder.locale;
        fontFamily = builder.fontFamily;
        fontSize = builder.fontSize;
        textColor = builder.textColor;
        accentColor = builder.accentColor;
        dateFormatter = builder.dateFormatter;
        messages = builder.messages;
        selectedDatePanelWidth = builder.selectedDatePanelWidth;
        selectedDatePanelHeight = builder.selectedDatePanelHeight;
        selectionPanelWidth = builder.selectionPanelWidth;
        selectionPanelHeight = builder.selectionPanelHeight;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Locale locale() {
        return locale;
    }

    public String fontFamily() {
        return fontFamily;
    }

    public double fontSize() {
        return fontSize;
    }

    public Color textColor() {
        return textColor;
    }

    public Color accentColor() {
        return accentColor;
    }

    public DateTimeFormatter dateFormatter() {
        return dateFormatter;
    }

    public ResourceBundle messages() {
        return messages;
    }

    public double selectedDatePanelWidth() {
        return selectedDatePanelWidth;
    }

    public double selectedDatePanelHeight() {
        return selectedDatePanelHeight;
    }

    public double selectionPanelWidth() {
        return selectionPanelWidth;
    }

    public double selectionPanelHeight() {
        return selectionPanelHeight;
    }

    public static final class Builder {
        private Locale locale = Locale.getDefault();
        private String fontFamily = "System";
        private double fontSize = 14;
        private Color textColor = Color.web("#1d2b45");
        private Color accentColor = Color.web("#3158c8");
        private DateTimeFormatter dateFormatter;
        private ResourceBundle messages;
        private double selectedDatePanelWidth = 270;
        private double selectedDatePanelHeight;
        private double selectionPanelWidth = 620;
        private double selectionPanelHeight;

        private Builder() {
        }

        public Builder locale(Locale locale) {
            this.locale = Objects.requireNonNull(locale, "locale");
            return this;
        }

        public Builder fontFamily(String fontFamily) {
            Objects.requireNonNull(fontFamily, "fontFamily");
            if (fontFamily.isBlank() || fontFamily.matches(".*[;{}\\r\\n\"\\\\].*")) {
                throw new IllegalArgumentException("fontFamily must be a non-empty CSS font family name");
            }
            this.fontFamily = fontFamily;
            return this;
        }

        public Builder fontSize(double fontSize) {
            if (!Double.isFinite(fontSize) || fontSize <= 0) {
                throw new IllegalArgumentException("fontSize must be a finite positive number");
            }
            this.fontSize = fontSize;
            return this;
        }

        public Builder textColor(Color textColor) {
            this.textColor = Objects.requireNonNull(textColor, "textColor");
            return this;
        }

        public Builder accentColor(Color accentColor) {
            this.accentColor = Objects.requireNonNull(accentColor, "accentColor");
            return this;
        }

        public Builder dateFormatter(DateTimeFormatter dateFormatter) {
            this.dateFormatter = Objects.requireNonNull(dateFormatter, "dateFormatter");
            return this;
        }

        public Builder messages(ResourceBundle messages) {
            this.messages = Objects.requireNonNull(messages, "messages");
            return this;
        }

        public Builder selectedDatePanelWidth(double width) {
            selectedDatePanelWidth = positiveDimension(width, "selectedDatePanelWidth");
            return this;
        }

        public Builder selectedDatePanelHeight(double height) {
            selectedDatePanelHeight = positiveDimension(height, "selectedDatePanelHeight");
            return this;
        }

        public Builder selectionPanelWidth(double width) {
            selectionPanelWidth = positiveDimension(width, "selectionPanelWidth");
            return this;
        }

        public Builder selectionPanelHeight(double height) {
            selectionPanelHeight = positiveDimension(height, "selectionPanelHeight");
            return this;
        }

        public DatePickerConfiguration build() {
            return new DatePickerConfiguration(this);
        }

        private double positiveDimension(double value, String property) {
            if (!Double.isFinite(value) || value <= 0) {
                throw new IllegalArgumentException(property + " must be a finite positive number");
            }
            return value;
        }
    }
}
