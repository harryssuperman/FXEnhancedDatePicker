package com.github.harryssuperman.fxdatepicker.core;

import com.github.harryssuperman.fxdatepicker.api.DateView;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultDatePickerModelTest {
    @Test
    void navigationJumpsByCurrentView() {
        DefaultDatePickerModel model = new DefaultDatePickerModel();
        model.showDate(LocalDate.of(2026, 2, 28));

        model.navigate(1);
        assertEquals(LocalDate.of(2026, 3, 28), model.displayedDateProperty().get());

        model.setView(DateView.YEARS);
        model.navigate(1);
        assertEquals(LocalDate.of(2027, 3, 28), model.displayedDateProperty().get());

        model.setView(DateView.DECADES);
        model.navigate(1);
        assertEquals(LocalDate.of(2037, 3, 28), model.displayedDateProperty().get());

        model.setView(DateView.CENTURIES);
        model.navigate(1);
        assertEquals(LocalDate.of(2137, 3, 28), model.displayedDateProperty().get());
    }

    @Test
    void selectingDateUpdatesSelectionAndReturnsToMonthView() {
        DefaultDatePickerModel model = new DefaultDatePickerModel();
        model.setView(DateView.CENTURIES);

        model.selectDate(LocalDate.of(2000, 2, 29));

        assertEquals(LocalDate.of(2000, 2, 29), model.selectedDateProperty().get());
        assertEquals(DateView.MONTHS, model.viewProperty().get());
    }

    @Test
    void selectingMonthClampsTheDayToMonthLength() {
        DefaultDatePickerModel model = new DefaultDatePickerModel();
        model.showDate(LocalDate.of(2024, 1, 31));

        model.selectMonth(2);

        assertEquals(LocalDate.of(2024, 2, 29), model.displayedDateProperty().get());
    }
}
