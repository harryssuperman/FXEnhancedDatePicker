package com.github.harryssuperman.fxdatepicker.controls;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatePickerConfigurationTest {
    @Test
    void panelDimensionsCanBeConfigured() {
        DatePickerConfiguration configuration = DatePickerConfiguration.builder()
                .selectedDatePanelWidth(340)
                .selectedDatePanelHeight(120)
                .selectionPanelWidth(720)
                .selectionPanelHeight(560)
                .build();

        assertEquals(340, configuration.selectedDatePanelWidth());
        assertEquals(120, configuration.selectedDatePanelHeight());
        assertEquals(720, configuration.selectionPanelWidth());
        assertEquals(560, configuration.selectionPanelHeight());
    }

    @Test
    void panelDimensionsMustBePositiveAndFinite() {
        assertThrows(IllegalArgumentException.class,
                () -> DatePickerConfiguration.builder().selectionPanelWidth(0));
        assertThrows(IllegalArgumentException.class,
                () -> DatePickerConfiguration.builder().selectedDatePanelHeight(Double.NaN));
    }
}
