package com.kenji1947.rssreader.presentation.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chamber on 26.03.2018.
 */

public class RadioDialog extends DialogFragment {
    private static final String ARGUMENT_RADIO_LIST = "radiodialog_list";
    private static final String ARGUMENT_TITLE = "radiodialog_title";
    private static final String ARGUMENT_SELECTED_ITEM_POS   = "radiodialog_selected_item_pos";

    public interface RadioDialogCallback {
        void choose(int pos);
    };
    private RadioDialogCallback callback;

    public static RadioDialog newInstance(String title, int selectedItemPos, ArrayList<String> radioList) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARGUMENT_RADIO_LIST, radioList);
        args.putString(ARGUMENT_TITLE, title);
        args.putInt(ARGUMENT_SELECTED_ITEM_POS, selectedItemPos);
        RadioDialog fragment = new RadioDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() != null && getActivity() instanceof RadioDialogCallback)
            callback = (RadioDialogCallback) getActivity();
        else if (getParentFragment() != null && getParentFragment() instanceof RadioDialogCallback)
            callback = (RadioDialogCallback) getParentFragment();
        else
            callback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> radioList = getArguments().getStringArrayList(ARGUMENT_RADIO_LIST);
        CharSequence[] charSequences = radioList.toArray(new CharSequence[radioList.size()]);
        String title = getArguments().getString(ARGUMENT_TITLE);
        int selectedItemPos = getArguments().getInt(ARGUMENT_SELECTED_ITEM_POS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(title)
                .setSingleChoiceItems(charSequences, selectedItemPos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(i);
                    }
                })
//                .setItems(charSequences, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        sendResult(i);
//                    }
//                })
                .setNegativeButton("Cancel", (dialog, id) -> dismiss());
        return builder.create();
    }

    private void sendResult(int pos) {
        if (callback == null)
            return;
        callback.choose(pos);
        dismiss();
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }
}
