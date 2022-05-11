package devpatel.apps.vaccisafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VaccineDetailsBottomSheetDialog extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vaccine_detais_bottom_sheet, container, false);

        String vaccine_name = this.getArguments().getString("vaccine_name");
        String vaccine_details = this.getArguments().getString("vaccine_details");

        TextView vaccine_name_tv = v.findViewById(R.id.vaccine_name_bs);
        vaccine_name_tv.setText(vaccine_name);

        TextView vaccine_details_tv = v.findViewById(R.id.vaccine_details_bs);
        vaccine_details_tv.setText(vaccine_details);

        return v;
    }
}
