package com.github.harryssuperman.fxdatepicker.core;

import com.github.harryssuperman.fxdatepicker.api.DatePickerModel;
import com.github.harryssuperman.fxdatepicker.api.DateView;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public final class DefaultDatePickerModel implements DatePickerModel {
    private final ReadOnlyObjectWrapper<LocalDate> selectedDate;
    private final ReadOnlyObjectWrapper<LocalDate> displayedDate;
    private final ReadOnlyObjectWrapper<DateView> view;

    public DefaultDatePickerModel() {
        LocalDate today = LocalDate.now();
        selectedDate = new ReadOnlyObjectWrapper<>(this, "selectedDate", today);
        displayedDate = new ReadOnlyObjectWrapper<>(this, "displayedDate", today);
        view = new ReadOnlyObjectWrapper<>(this, "view", DateView.DAYS);
    }

    @Override
    public ReadOnlyObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyObjectProperty<LocalDate> displayedDateProperty() {
        return displayedDate.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyObjectProperty<DateView> viewProperty() {
        return view.getReadOnlyProperty();
    }

    @Override
    public void selectDate(LocalDate date) {
        selectedDate.set(Objects.requireNonNull(date, "date"));
        displayedDate.set(date);
        view.set(DateView.DAYS);
    }

    @Override
    public void showDate(LocalDate date) {
        displayedDate.set(Objects.requireNonNull(date, "date"));
    }

    @Override
    public void setView(DateView newView) {
        view.set(Objects.requireNonNull(newView, "view"));
    }

    @Override
    public void navigate(int direction) {
        if (direction == 0) {
            return;
        }
        LocalDate date = displayedDate.get();
        int step = direction < 0 ? -1 : 1;
        displayedDate.set(switch (view.get()) {
            case DAYS -> {
                YearMonth target = YearMonth.from(date).plusMonths(step);
                yield target.atDay(Math.min(date.getDayOfMonth(), target.lengthOfMonth()));
            }
            case MONTHS -> date.plusYears(step);
            case YEARS -> date.plusYears(10L * step);
            case DECADES -> date.plusYears(100L * step);
            case CENTURIES -> date.plusYears(1000L * step);
        });
    }

    @Override
    public void selectMonth(int month) {
        LocalDate date = displayedDate.get();
        YearMonth target = YearMonth.of(date.getYear(), month);
        displayedDate.set(target.atDay(Math.min(date.getDayOfMonth(), target.lengthOfMonth())));
        view.set(DateView.DAYS);
    }

    @Override
    public void selectYear(int year) {
        LocalDate date = displayedDate.get();
        YearMonth target = YearMonth.of(year, date.getMonth());
        displayedDate.set(target.atDay(Math.min(date.getDayOfMonth(), target.lengthOfMonth())));
        view.set(DateView.MONTHS);
    }

    @Override
    public void selectDecade(int decadeStartYear) {
        selectYear(decadeStartYear);
        view.set(DateView.YEARS);
    }

    @Override
    public void selectCentury(int centuryStartYear) {
        selectYear(centuryStartYear);
        view.set(DateView.DECADES);
    }
}
