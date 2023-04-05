package com.kenji1947.rssreader.presentation.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import timber.log.Timber;

/**
 * Created by kenji1947 on 12.10.2017.
 */

public class ConfirmDialog extends DialogFragment {
    private static final String ARGUMENT_TITLE = "confirm_dialog_title";
    private static final String ARGUMENT_MSG = "confirm_dialog_msg";
    private static final String ARGUMENT_POSITIVE_MSG = "confirm_dialog_positive_msg";
    private static final String ARGUMENT_NEGATIVE_MSG = "confirm_dialog_negative_msg";
    private static final String ARGUMENT_TAG = "confirm_dialog_tag";

    public interface DialogConfirmCallback {
        void confirm(String dialogTag);
    };

    private DialogConfirmCallback callback;

    public static ConfirmDialog newInstance(String title,
                                            String message,
                                            String positiveMessage,
                                            String negativeMessage,
                                            String tag) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_TITLE, title);
        args.putString(ARGUMENT_MSG, message);
        args.putString(ARGUMENT_POSITIVE_MSG, positiveMessage);
        args.putString(ARGUMENT_NEGATIVE_MSG, negativeMessage);
        args.putString(ARGUMENT_TAG, tag);
        ConfirmDialog fragment = new ConfirmDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() != null && getActivity() instanceof DialogConfirmCallback)
            callback = (DialogConfirmCallback) getActivity();
        else if (getParentFragment() != null && getParentFragment() instanceof DialogConfirmCallback)
            callback = (DialogConfirmCallback) getParentFragment();
        else
            callback = null;
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARGUMENT_TITLE);
        String message = getArguments().getString(ARGUMENT_MSG); //TODO default value if null
        String positiveMessage = getArguments().getString(ARGUMENT_POSITIVE_MSG);
        String negativeMessage = getArguments().getString(ARGUMENT_NEGATIVE_MSG);
        String tag = getArguments().getString(ARGUMENT_TAG);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveMessage, (dialog, id) -> sendResult(tag))
                .setNegativeButton(negativeMessage, (dialog, id) -> dismiss());
        return builder.create();
    }

    private void sendResult(String tag) {
        if (callback == null)
            return;
        callback.confirm(tag);
    }
}
