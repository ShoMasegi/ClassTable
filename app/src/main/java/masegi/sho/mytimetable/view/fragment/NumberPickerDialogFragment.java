package masegi.sho.mytimetable.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import masegi.sho.mytimetable.R;

/**
 * Created by masegi on 2017/06/25.
 */

public class NumberPickerDialogFragment extends DialogFragment {

    private String title;
    NumberPickerCallback callback;
    private int minValue;
    private int maxValue;
    private int defValue;
    private NumberPicker numberPicker;


    public NumberPickerDialogFragment(){

    }


    /**
     *
     * @param callback
     * @see NumberPickerCallback#callback
     * Call callback(int number) if tap save button.
     * @param title
     */
    public NumberPickerDialogFragment createDialog(NumberPickerCallback callback,
                                      String title,
                                      int minValue,
                                      int maxValue,
                                      int defValue) {

        this.title = title;
        this.callback = callback;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defValue = defValue;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)getActivity());
        View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.view_number_picker,null);
        numberPicker = (NumberPicker)dialogLayout.findViewById(R.id.num_picker);
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setValue(defValue);

        builder.setTitle(title)
                .setView(dialogLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.callback(numberPicker.getValue());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel button click
                    }
                });
        return builder.create();
    }

    public interface NumberPickerCallback {
        /**
         * @param number
         * NumberPicker#getValue()
         */
        void callback(int number);
    }
}