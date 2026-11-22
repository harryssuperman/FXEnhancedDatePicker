package io.github.harryssuperman.fxdatepicker.core;

import io.github.harryssuperman.fxdatepicker.api.DateView;
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
        model.navigate(-1);
        assertEquals(LocalDate.of(2026, 2, 28), model.displayedDateProperty().get());

        model.setView(DateView.MONTHS);
        model.navigate(1);
        assertEquals(LocalDate.of(2027, 2, 28), model.displayedDateProperty().get());
        model.navigate(-1);
        assertEquals(LocalDate.of(2026, 2, 28), model.displayedDateProperty().get());

        model.setView(DateView.YEARS);
        model.navigate(1);
        assertEquals(LocalDate.of(2036, 2, 28), model.displayedDateProperty().get());
        model.navigate(-1);
        assertEquals(LocalDate.of(2026, 2, 28), model.displayedDateProperty().get());

        model.setView(DateView.DECADES);
        model.navigate(1);
        assertEquals(LocalDate.of(2126, 2, 28), model.displayedDateProperty().get());
        model.navigate(-1);
        assertEquals(LocalDate.of(2026, 2, 28), model.displayedDateProperty().get());

        model.setView(DateView.CENTURIES);
        model.navigate(1);
        assertEquals(LocalDate.of(3026, 2, 28), model.displayedDateProperty().get());
        model.navigate(-1);
        assertEquals(LocalDate.of(2026, 2, 28), model.displayedDateProperty().get());
    }

    @Test
    void selectingDateUpdatesSelectionAndReturnsToMonthView() {
        DefaultDatePickerModel model = new DefaultDatePickerModel();
        model.setView(DateView.CENTURIES);

        model.selectDate(LocalDate.of(2000, 2, 29));

        assertEquals(LocalDate.of(2000, 2, 29), model.selectedDateProperty().get());
        assertEquals(DateView.DAYS, model.viewProperty().get());
    }

    @Test
    void selectingMonthClampsTheDayToMonthLength() {
        DefaultDatePickerModel model = new DefaultDatePickerModel();
        model.showDate(LocalDate.of(2024, 1, 31));

        model.selectMonth(2);

        assertEquals(LocalDate.of(2024, 2, 29), model.displayedDateProperty().get());
        assertEquals(DateView.DAYS, model.viewProperty().get());
    }

    @Test
    void selectingYearOpensMonthSelectionAndDecadeSelectionOpensYearSelection() {
        DefaultDatePickerModel model = new DefaultDatePickerModel();
        model.showDate(LocalDate.of(2024, 2, 29));

        model.selectYear(2025);
        assertEquals(LocalDate.of(2025, 2, 28), model.displayedDateProperty().get());
        assertEquals(DateView.MONTHS, model.viewProperty().get());

        model.selectDecade(2031);
        assertEquals(LocalDate.of(2031, 2, 28), model.displayedDateProperty().get());
        assertEquals(DateView.YEARS, model.viewProperty().get());

        model.selectCentury(2001);
        assertEquals(LocalDate.of(2001, 2, 28), model.displayedDateProperty().get());
        assertEquals(DateView.DECADES, model.viewProperty().get());
    }
}
