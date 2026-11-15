# FX Enhanced Date Picker

A JavaFX date picker that keeps the selected date visible and opens a direct-navigation panel when the date is clicked. Navigate between days, months, years, decades, centuries, and millennia without scrolling through intermediate dates.

## Modules

- `bom`: centralized JavaFX and JUnit dependency versions.
- `api`: date picker model contract and navigation views.
- `core`: observable selection model and navigation logic.
- `controls`: JavaFX `EnhancedDatePicker` control.
- `animations`: reusable transitions.
- `themes`: default and weekday-enabled stylesheets.
- `demo`: sample application.

## Requirements

- JDK 25
- Maven 3.9 or later

## Build and test

```shell
mvn clean verify
```

## Run the demo

```shell
mvn clean install
mvn -pl demo javafx:run
```

## Use the control

```java
EnhancedDatePicker picker = new EnhancedDatePicker();
```

The selected-date panel uses the available width and wraps the date when needed. The default date format can be replaced with a `DateTimeFormatter`:

```java
EnhancedDatePicker picker = new EnhancedDatePicker(
    DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US)
);
```

The date format can also be combined with a theme:

```java
EnhancedDatePicker picker = new EnhancedDatePicker(
    DatePickerTheme.WITH_WEEKDAY,
    DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.forLanguageTag("es-ES"))
);
```

## Customize appearance and language

`DatePickerConfiguration` configures the locale, font family, font size, text and accent colors, date format, and panel dimensions. The locale also controls month and weekday names and the first day of the week. Spanish, English, and German translations are included. For other languages, provide a `ResourceBundle` with the same keys as `DatePickerMessages.properties`.

```java
DatePickerConfiguration configuration = DatePickerConfiguration.builder()
    .locale(Locale.forLanguageTag("en-US"))
    .fontFamily("Arial")
    .fontSize(16)
    .textColor(Color.web("#24324a"))
    .accentColor(Color.web("#7651c9"))
    .dateFormatter(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
    .build();

EnhancedDatePicker picker = new EnhancedDatePicker(DatePickerTheme.DEFAULT, configuration);
```

Panel dimensions are specified in JavaFX points. Heights are automatic by default; set a height to use a fixed preferred height:

```java
DatePickerConfiguration configuration = DatePickerConfiguration.builder()
    .selectedDatePanelWidth(340)
    .selectedDatePanelHeight(120)
    .selectionPanelWidth(720)
    .selectionPanelHeight(560)
    .build();
```

The default theme shows only the selected date. To also show the weekday:

```java
EnhancedDatePicker picker = new EnhancedDatePicker(DatePickerTheme.WITH_WEEKDAY);
```

You can inject a custom `DatePickerModel`, with or without a theme:

```java
EnhancedDatePicker picker = new EnhancedDatePicker(model, DatePickerTheme.DEFAULT);
```

## License

This project is licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for details.
