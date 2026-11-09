package com.github.harryssuperman.fx_enhanced_datepicker.api.model;

import java.time.LocalDate;

public interface DateSelectionModel {

    LocalDate selectedDate();

    void select(LocalDate date);

    void clear();
}
