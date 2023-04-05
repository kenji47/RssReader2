package com.kenji1947.rssreader.util;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.azimolabs.conditionwatcher.Instruction;
import com.kenji1947.rssreader.App;


/**
 * Created by kenji1947 on 16.10.2017.
 */

public class InstructionBuilder {

//    Activity activity = ((App)
//            InstrumentationRegistry.getTargetContext().getApplicationContext()).getCurrentActivity();

//    private static Activity getActivity() {
//        return ((App) InstrumentationRegistry.getTargetContext().getApplicationContext()).getCurrentActivity();
//    }

//    public static Instruction buildCheckViewVisibility(int resId, boolean visibility, String description) {
//        return new Instruction() {
//            @Override
//            public String getDescription() {
//                return description;
//            }
//
//            @Override
//            public boolean checkCondition() {
//                View view = getActivity().findViewById(resId);
//                if (visibility) {
//                    return view != null && view.getVisibility() == View.VISIBLE;
//                } else {
//                    return view != null && view.getVisibility() != View.VISIBLE;
//                }
//            }
//        };
//    }

    public static Instruction buildCheckAdapterSize(int recyclerViewId, int adapterSize, String description, Activity activity) {
        return new Instruction() {
            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public boolean checkCondition() {
                RecyclerView recyclerView = activity.findViewById(recyclerViewId);
                return recyclerView != null && recyclerView.getAdapter().getItemCount() == adapterSize;
            }
        };
    }


}
