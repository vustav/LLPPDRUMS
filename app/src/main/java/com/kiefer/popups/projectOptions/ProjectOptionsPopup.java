package com.kiefer.popups.projectOptions;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.info.main.ProjectOptionsInfo;
import com.kiefer.options.projectOptions.ProjectOptionsManager;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ImgUtils;

public class ProjectOptionsPopup extends Popup {
    private ProjectOptionsManager projectOptionsManager;

    private int driverPopupBgId;
    private CSpinnerButton driverSpinnerButton;

    public ProjectOptionsPopup(final LLPPDRUMS llppdrums, ProjectOptionsManager projectOptionsManager){
        super(llppdrums);

        this.projectOptionsManager = projectOptionsManager;

        driverPopupBgId = ImgUtils.getRandomImageId();

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_project_options, null);
        popupView.setBackground(ContextCompat.getDrawable(llppdrums, llppdrums.getDrumMachine().getOptionsBgId()));

        //create the popupWindow
        //int width = RelativeLayout.LayoutParams.WRAP_CONTENT;@dimen/defaultSeekBarWidth
        int width = (int) llppdrums.getResources().getDimension(R.dimen.defaultSeekBarWidth) * 2 + (int) llppdrums.getResources().getDimension(R.dimen.marginLarge) * 6;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        //int height = 100;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //set up the driver spinner
        if(llppdrums.getEngineFacade().supportsAAudio()) {
            //(( Spinner ) findViewById( R.id.DriverSpinner )).setOnItemSelectedListener( new DriverChangeHandler() );

            driverSpinnerButton = new CSpinnerButton(llppdrums);
            driverSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //engineFacade.setDriver();
                    new DriverPopup(llppdrums, driverPopupBgId, driverSpinnerButton, projectOptionsManager);
                }
            });
            driverSpinnerButton.setSelection(llppdrums.getEngineFacade().getDriver());
            FrameLayout spinnerContainer = popupView.findViewById(R.id.projSettingsDriverSpinnerContainer);
            spinnerContainer.addView(driverSpinnerButton);
        }
        else{
            //ta bort typ label
        }

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.projectOptionsInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, ProjectOptionsInfo.key);
            }
        });

        //show the popup with a little offset
        show(popupWindow);
    }
}
