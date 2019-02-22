package org.bigdata.zczw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.StartActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeadImageFragment extends Fragment {


    private ImageView ret;
    private TextView btn;

    public LeadImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_lead, null);
        ret = (ImageView) view.findViewById(R.id.img_lead_frag);
        btn = (TextView) view.findViewById(R.id.btn_go_on);
        Bundle bundle = getArguments();
        int index = 0;
        if (bundle!=null){
            index = bundle.getInt("value");
        }

        int[] imageRes = new int[]{R.drawable.lead_image_one,
                R.drawable.lead_image_two,
                R.drawable.lead_image_three,
                R.drawable.lead_image_four};
        ret.setImageResource(imageRes[index]);
        if (index == 3){
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().startActivity(new Intent(getActivity(), StartActivity.class));
                    getActivity().finish();
                }
            });
        }
        return view;
    }

}
