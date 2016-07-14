package ello.com.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import ello.com.stormy.R;

/**
 * Created by venkatgonuguntala on 9/20/15.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builer = new AlertDialog.Builder(context) //Factory method Pattern
                .setTitle(context.getString(R.string.error_title))
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.error_ok_button_text, null);

        AlertDialog dialog = builer.create();
        return dialog;
    }
}
