package com.github.harryssuperman.fx_enhanced_datepicker.api.constraints;

import java.time.LocalDate;

public interface DateConstraints {

    boolean isSelectable(LocalDate date);

    LocalDate minDate();

    LocalDate maxDate();

}