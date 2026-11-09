package com.github.harryssuperman.fx_enhanced_datepicker.api.navigation;

import com.github.harryssuperman.fx_enhanced_datepicker.api.model.ViewMode;

public interface DateNavigator {

    ViewMode currentView();

    void navigateUp();

    void navigateDown(Object selection);

}
