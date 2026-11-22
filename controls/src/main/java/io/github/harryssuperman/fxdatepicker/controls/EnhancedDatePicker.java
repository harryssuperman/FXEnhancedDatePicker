package io.github.harryssuperman.fxdatepicker.controls;

import io.github.harryssuperman.fxdatepicker.animations.PickerAnimations;
import io.github.harryssuperman.fxdatepicker.api.DatePickerModel;
import io.github.harryssuperman.fxdatepicker.api.DateView;
import io.github.harryssuperman.fxdatepicker.core.DefaultDatePickerModel;
import io.github.harryssuperman.fxdatepicker.themes.DatePickerTheme;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public final class EnhancedDatePicker extends HBox {
    private static final DateTimeFormatter DEFAULT_DATE_FORMAT =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
    private static final String[] VIEW_LABEL_KEYS = {
            "view.days", "view.months", "view.years", "view.decades", "view.centuries"
    };

    private final DatePickerModel model;
    private final DatePickerTheme theme;
    private final DatePickerConfiguration configuration;
    private final Locale locale;
    private final ResourceBundle messages;
    private final DateTimeFormatter dateFormatter;
    private final Popup pickerPopup = new Popup();
    private final VBox selectedDatePanel = new VBox(10);
    private final VBox calendar = new VBox(12);
    private final Label selectedDay = new Label();
    private final Button selectedDate = new Button();
    private final Button previousPage = new Button("<");
    private final Button nextPage = new Button(">");
    private final Button title = new Button();
    private final Button[] levelButtons = new Button[DateView.values().length];
    private final GridPane choices = new GridPane();
    public EnhancedDatePicker() {
        this(new DefaultDatePickerModel(), DatePickerTheme.DEFAULT, DatePickerConfiguration.builder().build());
    }

    public EnhancedDatePicker(DatePickerTheme theme) {
        this(new DefaultDatePickerModel(), theme, DatePickerConfiguration.builder().build());
    }

    public EnhancedDatePicker(DateTimeFormatter dateFormatter) {
        this(new DefaultDatePickerModel(), DatePickerTheme.DEFAULT,
                DatePickerConfiguration.builder().dateFormatter(dateFormatter).build());
    }

    public EnhancedDatePicker(DatePickerTheme theme, DateTimeFormatter dateFormatter) {
        this(new DefaultDatePickerModel(), theme,
                DatePickerConfiguration.builder().dateFormatter(dateFormatter).build());
    }

    public EnhancedDatePicker(DatePickerModel model) {
        this(model, DatePickerTheme.DEFAULT, DatePickerConfiguration.builder().build());
    }

    public EnhancedDatePicker(DatePickerModel model, DateTimeFormatter dateFormatter) {
        this(model, DatePickerTheme.DEFAULT, DatePickerConfiguration.builder().dateFormatter(dateFormatter).build());
    }

    public EnhancedDatePicker(DatePickerModel model, DatePickerTheme theme) {
        this(model, theme, DatePickerConfiguration.builder().build());
    }

    public EnhancedDatePicker(DatePickerConfiguration configuration) {
        this(new DefaultDatePickerModel(), DatePickerTheme.DEFAULT, configuration);
    }

    public EnhancedDatePicker(DatePickerTheme theme, DatePickerConfiguration configuration) {
        this(new DefaultDatePickerModel(), theme, configuration);
    }

    public EnhancedDatePicker(DatePickerModel model, DatePickerConfiguration configuration) {
        this(model, DatePickerTheme.DEFAULT, configuration);
    }

    public EnhancedDatePicker(DatePickerModel model, DatePickerTheme theme, DateTimeFormatter dateFormatter) {
        this(model, theme, DatePickerConfiguration.builder().dateFormatter(dateFormatter).build());
    }

    public EnhancedDatePicker(DatePickerModel model, DatePickerTheme theme,
                              DatePickerConfiguration configuration) {
        this.model = Objects.requireNonNull(model, "model");
        this.theme = Objects.requireNonNull(theme, "theme");
        this.configuration = Objects.requireNonNull(configuration, "configuration");
        locale = configuration.locale();
        messages = configuration.messages() == null ? loadMessages(locale) : configuration.messages();
        this.dateFormatter = configuration.dateFormatter() == null
                ? DEFAULT_DATE_FORMAT.withLocale(locale)
                : configuration.dateFormatter().withLocale(locale);
        getStyleClass().add("enhanced-date-picker");
        setStyle("-fx-font-family: \"" + configuration.fontFamily().replace("\"", "\\\"") + "\";"
                + "-fx-font-size: " + configuration.fontSize() + "px;"
                + "-fx-text-base-color: " + configuration.textColor().toString() + ";"
                + "-fx-accent: " + configuration.accentColor().toString() + ";");
        getStylesheets().add(theme.stylesheet());

        getChildren().add(createSummary());
        createCalendar();
        configurePanelSize(selectedDatePanel, configuration.selectedDatePanelWidth(),
                configuration.selectedDatePanelHeight());
        configurePanelSize(calendar, configuration.selectionPanelWidth(),
                configuration.selectionPanelHeight());
        calendar.getStylesheets().add(theme.stylesheet());
        calendar.setStyle("-fx-font-family: \"" + configuration.fontFamily().replace("\"", "\\\"") + "\";"
                + "-fx-font-size: " + configuration.fontSize() + "px;"
                + "-fx-text-base-color: " + configuration.textColor().toString() + ";"
                + "-fx-accent: " + configuration.accentColor().toString() + ";");
        selectedDay.setVisible(theme.showsWeekday());
        selectedDay.setManaged(theme.showsWeekday());
        pickerPopup.getContent().add(calendar);
        pickerPopup.setAutoFix(true);
        pickerPopup.setAutoHide(true);
        pickerPopup.setHideOnEscape(true);
        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null) {
                closePicker();
            }
        });
        model.selectedDateProperty().addListener((observable, oldDate, newDate) -> updateSummary(newDate));
        model.displayedDateProperty().addListener((observable, oldDate, newDate) -> refresh());
        model.viewProperty().addListener((observable, oldView, newView) -> refresh());
        updateSummary(model.selectedDateProperty().get());
        refresh();
    }

    public DatePickerModel getModel() {
        return model;
    }

    public DatePickerTheme getTheme() {
        return theme;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    public DatePickerConfiguration getConfiguration() {
        return configuration;
    }

    private ResourceBundle loadMessages(Locale requestedLocale) {
        String language = switch (requestedLocale.getLanguage()) {
            case "en" -> "en";
            case "de" -> "de";
            default -> "es";
        };
        return ResourceBundle.getBundle(
            "io.github.harryssuperman.fxdatepicker.controls.DatePickerMessages",
                Locale.forLanguageTag(language),
                EnhancedDatePicker.class.getModule());
    }

    private String text(String key, Object... arguments) {
        return new MessageFormat(messages.getString(key), locale).format(arguments);
    }

    private VBox createSummary() {
        selectedDay.getStyleClass().add("date-summary-day");
        selectedDate.getStyleClass().add("date-summary-date");
        selectedDate.getStyleClass().add("date-trigger");
        selectedDate.setAlignment(Pos.CENTER_LEFT);
        selectedDate.setWrapText(true);
        selectedDate.setMaxWidth(Double.MAX_VALUE);
        selectedDate.setOnAction(event -> openPicker());

        selectedDatePanel.getChildren().addAll(selectedDay, selectedDate);
        selectedDatePanel.getStyleClass().add("date-summary");
        selectedDatePanel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(selectedDatePanel, Priority.ALWAYS);
        return selectedDatePanel;
    }

    private void configurePanelSize(VBox panel, double width, double height) {
        panel.setMinWidth(width);
        panel.setPrefWidth(width);
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.setStyle("-fx-min-width: " + width + "px; -fx-pref-width: " + width + "px;");
        if (height > 0) {
            panel.setMinHeight(height);
            panel.setPrefHeight(height);
            panel.setMaxHeight(Double.MAX_VALUE);
            panel.setStyle(panel.getStyle() + "-fx-min-height: " + height
                    + "px; -fx-pref-height: " + height + "px;");
        }
    }

    private void createCalendar() {
        previousPage.setTooltip(new Tooltip(text("page.previous")));
        previousPage.setOnAction(event -> model.navigate(-1));

        nextPage.setTooltip(new Tooltip(text("page.next")));
        nextPage.setOnAction(event -> model.navigate(1));

        Button close = new Button("×");
        close.setTooltip(new Tooltip(text("close")));
        close.setAccessibleText(text("close"));
        close.setOnAction(event -> closePicker());

        title.getStyleClass().add("date-picker-title");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setOnAction(event -> moveToCoarserView());
        HBox.setHgrow(title, Priority.ALWAYS);

        HBox navigation = new HBox(6, previousPage, title, nextPage, close);
        navigation.getStyleClass().add("date-picker-navigation");
        navigation.setAlignment(Pos.CENTER);

        HBox levels = new HBox();
        levels.getStyleClass().add("date-picker-levels");
        DateView[] views = DateView.values();
        for (int index = 0; index < views.length; index++) {
            DateView view = views[index];
            String label = text(VIEW_LABEL_KEYS[index]);
            Button button = new Button(label);
            button.setOnAction(event -> model.setView(view));
            button.setAccessibleText(text("view.accessible", label.toLowerCase(locale)));
            levelButtons[index] = button;
            HBox.setHgrow(button, Priority.ALWAYS);
            levels.getChildren().add(button);
        }

        Button today = new Button(text("today"));
        today.setOnAction(event -> {
            model.selectDate(LocalDate.now());
            closePicker();
        });
        today.setAccessibleText(text("today.accessible"));

        choices.getStyleClass().add("date-grid");
        calendar.getStyleClass().add("date-calendar");
        calendar.getChildren().addAll(navigation, levels, today, choices);
    }

    private void updateSummary(LocalDate date) {
        selectedDay.setText(date.format(DateTimeFormatter.ofPattern("EEEE", locale)));
        selectedDate.setText(date.format(dateFormatter));
        selectedDate.setAccessibleText(text("selected-date.accessible",
                date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale))));
    }

    private void openPicker() {
        if (pickerPopup.isShowing()) {
            closePicker();
            return;
        }
        Window owner = getScene() == null ? null : getScene().getWindow();
        if (owner == null || !owner.isShowing()) {
            return;
        }
        model.showDate(model.selectedDateProperty().get());
        model.setView(DateView.DAYS);
        Bounds triggerBounds = selectedDate.localToScreen(selectedDate.getBoundsInLocal());
        if (triggerBounds == null) {
            return;
        }
        pickerPopup.show(owner, triggerBounds.getMinX(), triggerBounds.getMaxY());
        PickerAnimations.fadeIn(calendar);
    }

    private void closePicker() {
        pickerPopup.hide();
    }

    private void refresh() {
        LocalDate date = model.displayedDateProperty().get();
        DateView view = model.viewProperty().get();
        title.setText(titleFor(date, view));
        title.setAccessibleText(text("title.accessible", title.getText()));
        title.setDisable(view == DateView.CENTURIES);
        String period = switch (view) {
            case DAYS -> text("period.month");
            case MONTHS -> text("period.year");
            case YEARS -> text("period.decade");
            case DECADES -> text("period.century");
            case CENTURIES -> text("period.millennium");
        };
        previousPage.setAccessibleText(text("page.previous.accessible", period));
        nextPage.setAccessibleText(text("page.next.accessible", period));
        for (int index = 0; index < levelButtons.length; index++) {
            levelButtons[index].pseudoClassStateChanged(
                    javafx.css.PseudoClass.getPseudoClass("selected"), DateView.values()[index] == view);
        }
        choices.getChildren().clear();
        choices.getRowConstraints().clear();
        if (view == DateView.DAYS) {
            showMonth(date);
        } else {
            showChoices(date, view);
        }
        PickerAnimations.fadeIn(choices);
    }

    private void showMonth(LocalDate date) {
        setGridColumns(7);
        DayOfWeek firstDay = WeekFields.of(locale).getFirstDayOfWeek();
        for (int column = 0; column < 7; column++) {
            DayOfWeek day = firstDay.plus(column);
            Label weekday = new Label(day.getDisplayName(TextStyle.FULL, locale));
            weekday.getStyleClass().add("date-weekday");
            weekday.setMaxWidth(Double.MAX_VALUE);
            weekday.setAlignment(Pos.CENTER);
            choices.add(weekday, column, 0);
        }

        YearMonth month = YearMonth.from(date);
        LocalDate firstOfMonth = month.atDay(1);
        int offset = Math.floorMod(firstOfMonth.getDayOfWeek().getValue() - firstDay.getValue(), 7);
        LocalDate firstCell = firstOfMonth.minusDays(offset);
        LocalDate selected = model.selectedDateProperty().get();
        for (int index = 0; index < 42; index++) {
            LocalDate cellDate = firstCell.plusDays(index);
            Button day = new Button(Integer.toString(cellDate.getDayOfMonth()));
            day.getStyleClass().add("date-cell");
            if (!YearMonth.from(cellDate).equals(month)) {
                day.getStyleClass().add("date-cell-outside");
            }
            if (cellDate.equals(selected)) {
                day.getStyleClass().add("date-cell-selected");
            }
            if (cellDate.equals(LocalDate.now())) {
                day.getStyleClass().add("date-today");
            }
            day.setMaxWidth(Double.MAX_VALUE);
            day.setOnAction(event -> {
                model.selectDate(cellDate);
                closePicker();
            });
            day.setAccessibleText(cellDate.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale)));
            choices.add(day, index % 7, index / 7 + 1);
        }
    }

    private void showChoices(LocalDate date, DateView view) {
        int optionCount = view == DateView.MONTHS ? 12 : 10;
        int columns = view == DateView.MONTHS ? 4 : 5;
        setGridColumns(columns);
        for (int index = 0; index < optionCount; index++) {
            int row = index / columns;
            int column = index % columns;
            Button choice = switch (view) {
                case MONTHS -> monthChoice(index + 1, date);
                case YEARS -> yearChoice(decadeStart(date.getYear()) + index);
                case DECADES -> decadeChoice(centuryStart(date.getYear()) + (index * 10));
                case CENTURIES -> centuryChoice(millenniumStart(date.getYear()) + (index * 100));
                case DAYS -> throw new IllegalStateException("Days view is rendered separately");
            };
            choice.setMaxWidth(Double.MAX_VALUE);
            choices.add(choice, column, row);
        }
    }

    private void setGridColumns(int count) {
        choices.getColumnConstraints().clear();
        for (int column = 0; column < count; column++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(100.0 / count);
            constraints.setHgrow(Priority.ALWAYS);
            choices.getColumnConstraints().add(constraints);
        }
    }

    private Button monthChoice(int month, LocalDate displayedDate) {
        Button choice = new Button(java.time.Month.of(month).getDisplayName(TextStyle.FULL, locale));
        choice.getStyleClass().add("date-choice");
        if (month == model.selectedDateProperty().get().getMonthValue()
                && displayedDate.getYear() == model.selectedDateProperty().get().getYear()) {
            choice.getStyleClass().add("date-choice-selected");
        }
        choice.setOnAction(event -> model.selectMonth(month));
        return choice;
    }

    private Button yearChoice(int year) {
        Button choice = new Button(Integer.toString(year));
        choice.getStyleClass().add("date-choice");
        if (year == model.selectedDateProperty().get().getYear()) {
            choice.getStyleClass().add("date-choice-selected");
        }
        choice.setOnAction(event -> model.selectYear(year));
        return choice;
    }

    private Button decadeChoice(int startYear) {
        Button choice = new Button(startYear + "–" + (startYear + 9));
        choice.getStyleClass().add("date-choice");
        int selectedYear = model.selectedDateProperty().get().getYear();
        if (selectedYear >= startYear && selectedYear < startYear + 10) {
            choice.getStyleClass().add("date-choice-selected");
        }
        choice.setOnAction(event -> model.selectDecade(startYear));
        return choice;
    }

    private Button centuryChoice(int startYear) {
        Button choice = new Button(startYear + "–" + (startYear + 99));
        choice.getStyleClass().add("date-choice");
        int selectedYear = model.selectedDateProperty().get().getYear();
        if (selectedYear >= startYear && selectedYear < startYear + 100) {
            choice.getStyleClass().add("date-choice-selected");
        }
        choice.setOnAction(event -> model.selectCentury(startYear));
        return choice;
    }

    private String titleFor(LocalDate date, DateView view) {
        return switch (view) {
            case MONTHS -> text("title.months", date.getYear());
            case YEARS -> {
                int start = decadeStart(date.getYear());
                yield text("title.decade", start, start + 9);
            }
            case DECADES -> {
                int start = centuryStart(date.getYear());
                yield text("title.century", start, start + 99);
            }
            case DAYS -> text("title.days", date.getMonth().getDisplayName(TextStyle.FULL, locale),
                    date.getYear());
            case CENTURIES -> {
                int start = millenniumStart(date.getYear());
                yield text("title.millennium", start, start + 999);
            }
        };
    }

    private void moveToCoarserView() {
        model.setView(switch (model.viewProperty().get()) {
            case DAYS -> DateView.MONTHS;
            case MONTHS -> DateView.YEARS;
            case YEARS -> DateView.DECADES;
            case DECADES -> DateView.CENTURIES;
            case CENTURIES -> DateView.CENTURIES;
        });
    }

    private int decadeStart(int year) {
        return Math.floorDiv(year - 1, 10) * 10 + 1;
    }

    private int centuryStart(int year) {
        return Math.floorDiv(year - 1, 100) * 100 + 1;
    }

    private int millenniumStart(int year) {
        return Math.floorDiv(year - 1, 1000) * 1000 + 1;
    }
}
