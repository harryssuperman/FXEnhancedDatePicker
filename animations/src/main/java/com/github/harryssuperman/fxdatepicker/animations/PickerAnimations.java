package com.github.harryssuperman.fxdatepicker.animations;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public final class PickerAnimations {
    private PickerAnimations() {
    }

    public static void fadeIn(Node node) {
        FadeTransition transition = new FadeTransition(Duration.millis(120), node);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }
}
