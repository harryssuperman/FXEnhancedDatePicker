package com.github.harryssuperman.fxdatepicker.controls;

import com.github.harryssuperman.fxdatepicker.animations.PickerAnimations;
import com.github.harryssuperman.fxdatepicker.api.DatePickerModel;
import com.github.harryssuperman.fxdatepicker.api.DateView;
import com.github.harryssuperman.fxdatepicker.core.DefaultDatePickerModel;
import com.github.harryssuperman.fxdatepicker.themes.DatePickerTheme;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

public final class EnhancedDatePicker extends HBox {
    private static final DateTimeFormatter SUMMARY_DAY =
            DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
    private static final DateTimeFormatter SUMMARY_DATE =
            DateTimeFormatter.ofPattern("d 'de' MMMM", Locale.getDefault());

    private final DatePickerModel model;
    private final VBox calendar = new VBox(12);
    private final Label selectedDay = new Label();
    private final Label selectedDate = new Label();
    private final Label selectedYear = new Label();
    private final Button title = new Button();
    private final Button[] levelButtons = new Button[DateView.values().length];
    private final GridPane choices = new GridPane();

    public EnhancedDatePicker() {
        this(new DefaultDatePickerModel());
    }

    public EnhancedDatePicker(DatePickerModel model) {
        this.model = Objects.requireNonNull(model, "model");
        getStyleClass().add("enhanced-date-picker");
        getStylesheets().add(DatePickerTheme.defaultStylesheet());

        getChildren().addAll(createSummary(), createCalendar());
        model.selectedDateProperty().addListener((observable, oldDate, newDate) -> updateSummary(newDate));
        model.displayedDateProperty().addListener((observable, oldDate, newDate) -> refresh());
        model.viewProperty().addListener((observable, oldView, newView) -> refresh());
        updateSummary(model.selectedDateProperty().get());
        refresh();
    }

    public DatePickerModel getModel() {
        return model;
    }

    private VBox createSummary() {
        Label caption = new Label("FECHA SELECCIONADA");
        caption.getStyleClass().add("date-summary-caption");
        selectedDay.getStyleClass().add("date-summary-day");
        selectedDate.getStyleClass().add("date-summary-date");
        selectedYear.getStyleClass().add("date-summary-year");

        Button today = new Button("Hoy");
        today.setOnAction(event -> model.selectDate(LocalDate.now()));
        today.setAccessibleText("Seleccionar la fecha de hoy");

        VBox summary = new VBox(10, caption, selectedDay, selectedDate, selectedYear, today);
        summary.getStyleClass().add("date-summary");
        return summary;
    }

    private VBox createCalendar() {
        Button previous = new Button("‹");
        previous.setTooltip(new Tooltip("Anterior"));
        previous.setAccessibleText("Periodo anterior");
        previous.setOnAction(event -> model.navigate(-1));

        Button next = new Button("›");
        next.setTooltip(new Tooltip("Siguiente"));
        next.setAccessibleText("Periodo siguiente");
        next.setOnAction(event -> model.navigate(1));

        title.getStyleClass().add("date-picker-title");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setOnAction(event -> moveToCoarserView());
        HBox.setHgrow(title, Priority.ALWAYS);

        HBox navigation = new HBox(6, previous, title, next);
        navigation.getStyleClass().add("date-picker-navigation");
        navigation.setAlignment(Pos.CENTER);

        HBox levels = new HBox();
        levels.getStyleClass().add("date-picker-levels");
        String[] labels = {"Mes", "Año", "Década", "Siglo"};
        DateView[] views = DateView.values();
        for (int index = 0; index < views.length; index++) {
            DateView view = views[index];
            Button button = new Button(labels[index]);
            button.setOnAction(event -> model.setView(view));
            button.setAccessibleText("Mostrar " + labels[index].toLowerCase(Locale.ROOT));
            levelButtons[index] = button;
            HBox.setHgrow(button, Priority.ALWAYS);
            levels.getChildren().add(button);
        }

        choices.getStyleClass().add("date-grid");
        for (int column = 0; column < 7; column++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(100.0 / 7);
            constraints.setHgrow(Priority.ALWAYS);
            choices.getColumnConstraints().add(constraints);
        }
        calendar.getStyleClass().add("date-calendar");
        calendar.getChildren().addAll(navigation, levels, choices);
        return calendar;
    }

    private void updateSummary(LocalDate date) {
        selectedDay.setText(date.format(SUMMARY_DAY));
        selectedDate.setText(date.format(SUMMARY_DATE));
        selectedYear.setText(Integer.toString(date.getYear()));
    }

    private void refresh() {
        LocalDate date = model.displayedDateProperty().get();
        DateView view = model.viewProperty().get();
        title.setText(titleFor(date, view));
        title.setAccessibleText(title.getText() + ". Cambiar a una vista más amplia");
        title.setDisable(view == DateView.CENTURIES);
        for (int index = 0; index < levelButtons.length; index++) {
            levelButtons[index].pseudoClassStateChanged(
                    javafx.css.PseudoClass.getPseudoClass("selected"), DateView.values()[index] == view);
        }
        choices.getChildren().clear();
        choices.getRowConstraints().clear();
        if (view == DateView.MONTHS) {
            showMonth(date);
        } else {
            showChoices(date, view);
        }
        PickerAnimations.fadeIn(choices);
    }

    private void showMonth(LocalDate date) {
        DayOfWeek firstDay = DayOfWeek.MONDAY;
        for (int column = 0; column < 7; column++) {
            DayOfWeek day = firstDay.plus(column);
            Label weekday = new Label(day.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
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
            day.setOnAction(event -> model.selectDate(cellDate));
            day.setAccessibleText(cellDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())));
            choices.add(day, index % 7, index / 7 + 1);
        }
    }

    private void showChoices(LocalDate date, DateView view) {
        int columns = 3;
        for (int index = 0; index < 12; index++) {
            int row = index / columns;
            int column = index % columns;
            Button choice = switch (view) {
                case YEARS -> monthChoice(index + 1, date);
                case DECADES -> yearChoice(decadeStart(date.getYear()) - 1 + index);
                case CENTURIES -> decadeChoice(centuryStart(date.getYear()) - 10 + (index * 10));
                case MONTHS -> throw new IllegalStateException("Month view is rendered separately");
            };
            choice.setMaxWidth(Double.MAX_VALUE);
            choices.add(choice, column, row);
        }
    }

    private Button monthChoice(int month, LocalDate displayedDate) {
        Button choice = new Button(java.time.Month.of(month).getDisplayName(TextStyle.SHORT, Locale.getDefault()));
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

    private String titleFor(LocalDate date, DateView view) {
        return switch (view) {
            case MONTHS -> date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())
                    + " " + date.getYear();
            case YEARS -> "Selecciona un mes · " + date.getYear();
            case DECADES -> {
                int start = decadeStart(date.getYear());
                yield "Década " + start + "–" + (start + 9);
            }
            case CENTURIES -> {
                int start = centuryStart(date.getYear());
                yield "Siglo " + start + "–" + (start + 99);
            }
        };
    }

    private void moveToCoarserView() {
        model.setView(switch (model.viewProperty().get()) {
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
}
