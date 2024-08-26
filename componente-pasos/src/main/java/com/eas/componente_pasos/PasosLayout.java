package com.eas.componente_pasos;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class PasosLayout extends LinearLayout {

    private int numberOfSteps;
    private int currentStep;
    private String[] stepDescriptionList;
    private int unSelectedColor = Color.LTGRAY;
    private int selectedColor = Color.GREEN;
    private boolean isRainbow = false;

    public PasosLayout(Context context) {
        super(context);
        init(context, null);
    }

    public PasosLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setStepperData(int numberOfSteps, int currentStep, String[] stepDescriptionList) {
        this.numberOfSteps = numberOfSteps;
        this.currentStep = currentStep;
        this.stepDescriptionList = stepDescriptionList;
        buildStepper();
    }

    private void buildStepper() {
        removeAllViews();
        for (int step = 1; step <= numberOfSteps; step++) {
            addStep(step);
            if (step < numberOfSteps) {
                addSeparator();
            }
        }
    }

    private void addStep(int step) {
        LinearLayout stepContainer = new LinearLayout(getContext());
        stepContainer.setOrientation(VERTICAL);
        stepContainer.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView circle = new ImageView(getContext());
        circle.setLayoutParams(new LayoutParams(60, 60));
        circle.setImageResource(step < currentStep ? android.R.drawable.checkbox_on_background : android.R.drawable.checkbox_off_background);

        TextView stepLabel = new TextView(getContext());
        stepLabel.setText(stepDescriptionList[step - 1]);
        stepLabel.setGravity(Gravity.CENTER_HORIZONTAL);
        stepLabel.setTextColor(step == currentStep ? selectedColor : unSelectedColor);

        stepContainer.addView(circle);
        stepContainer.addView(stepLabel);
        addView(stepContainer);
    }

    private void addSeparator() {
        View line = new View(getContext());
        LayoutParams params = new LayoutParams(60, 4);
        params.gravity = Gravity.CENTER_VERTICAL;
        line.setLayoutParams(params);
        line.setBackgroundColor(unSelectedColor);
        addView(line);
    }
}
