package com.github.harryssuperman.fxdatepicker.api;

import javafx.beans.property.ReadOnlyObjectProperty;

import java.time.LocalDate;

public interface DatePickerModel {
    ReadOnlyObjectProperty<LocalDate> selectedDateProperty();

    ReadOnlyObjectProperty<LocalDate> displayedDateProperty();

    ReadOnlyObjectProperty<DateView> viewProperty();

    void selectDate(LocalDate date);

    void showDate(LocalDate date);

    void setView(DateView view);

    void navigate(int direction);

    void selectMonth(int month);

    void selectYear(int year);

    void selectDecade(int decadeStartYear);
}
