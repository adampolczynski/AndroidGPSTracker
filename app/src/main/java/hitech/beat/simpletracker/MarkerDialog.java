package hitech.beat.simpletracker;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by beat on 2017-01-14.
 */

public class MarkerDialog extends DialogFragment {

    Button ButtonOk;
    Button ButtonCancel;
    EditText et_title;
    Spinner spinner_category;
    double[] markerLatLng = new double[] {};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        markerLatLng = getArguments().getDoubleArray("latLng");
        View view = inflater.inflate(R.layout.marker_input, container);
        ButtonOk = (Button) view.findViewById(R.id.but_ok);
        ButtonCancel = (Button) view.findViewById(R.id.but_cancel);
        et_title = (EditText) view.findViewById(R.id.etMarker_title);
        spinner_category = (Spinner) view.findViewById(R.id.spinner);

        getDialog().setTitle(R.string.dialog_marker_title);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.textviewcenter_shape);
        ButtonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                String[] markerDetails = new String[] {et_title.getText().toString(), spinner_category.getSelectedItem().toString()};
                intent.putExtra("markerData", markerDetails);
                intent.putExtra("markerLatLng",markerLatLng);
                getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                dismiss();
                //((MarkersFragment)(DialogFragment.this.getActivity())).onDialogOKPressed();
                //dismiss();


            }

        });

        ButtonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                MarkerDialog.this.dismiss();

            }
        });

        return view;
    }
}
