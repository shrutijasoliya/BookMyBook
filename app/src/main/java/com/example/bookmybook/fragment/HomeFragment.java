package com.example.bookmybook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmybook.R;
import com.example.bookmybook.adapter.CourseAdapter;

public class HomeFragment extends Fragment {

    private View view;

    private RecyclerView rvCourseHome;
    private int[] imagesCourse;
    private String[] namesCourse;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        bindView();

        imagesCourse = new int[]{R.drawable.ic_co, R.drawable.ic_me, R.drawable.ic_it, R.drawable.ic_ee,
                R.drawable.ic_ce, R.drawable.ic_che, R.drawable.ic_ec, R.drawable.ic_te, R.drawable.ic_ae};
        namesCourse = new String[]{
                getContext().getResources().getString(R.string.comp_eng),
                getContext().getResources().getString(R.string.mech_eng),
                getContext().getResources().getString(R.string.info_tech),
                getContext().getResources().getString(R.string.elec_eng),
                getContext().getResources().getString(R.string.civil_eng),
                getContext().getResources().getString(R.string.chem_eng),
                getContext().getResources().getString(R.string.elec_com),
                getContext().getResources().getString(R.string.tex_eng),
                getContext().getResources().getString(R.string.aero_eng)};

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCourseHome.setLayoutManager(linearLayoutManager);
        CourseAdapter courseAdapter = new CourseAdapter(imagesCourse, namesCourse, getContext());
        rvCourseHome.setAdapter(courseAdapter);

//        cdCompEng.setOnClickListener(onClickListener);
//        cdMechEng.setOnClickListener(onClickListener);
//        cdInfoTech.setOnClickListener(onClickListener);
//        cdElecEng.setOnClickListener(onClickListener);
//        cdCivilEng.setOnClickListener(onClickListener);
//        cdChemEng.setOnClickListener(onClickListener);
//        cdElecCom.setOnClickListener(onClickListener);
//        cdTexEng.setOnClickListener(onClickListener);
//        cdAeroEng.setOnClickListener(onClickListener);

        return view;
    }

//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.cdCompEng:
//                    startActivity(new Intent(getContext(), CompEngActivity.class));
//                    break;
//                case R.id.cdMechEng:
//                    startActivity(new Intent(getContext(), MechEngActivity.class));
//                    break;
//                case R.id.cdInfoTech:
//                    startActivity(new Intent(getContext(), InfoTechActivity.class));
//                    break;
//                case R.id.cdElecEng:
//                    startActivity(new Intent(getContext(), ElecEngActivity.class));
//                    break;
//                case R.id.cdCivilEng:
//                    startActivity(new Intent(getContext(), CivilEngActivity.class));
//                    break;
//                case R.id.cdChemEng:
//                    startActivity(new Intent(getContext(), ChemEngActivity.class));
//                    break;
//                case R.id.cdElecCom:
//                    startActivity(new Intent(getContext(), ElecComActivity.class));
//                    break;
//                case R.id.cdTexEng:
//                    startActivity(new Intent(getContext(), TexEngActivity.class));
//                    break;
//                case R.id.cdAeroEng:
//                    startActivity(new Intent(getContext(), AeroEngActivity.class));
//                    break;
//
//            }
//        }
//    };
//
//    public void setOnClickListener(View.OnClickListener listener) {
//        View.OnClickListener mListener = listener;
//    }

    private void bindView() {

//        cdCompEng = view.findViewById(R.id.cdCompEng);
//        cdMechEng = view.findViewById(R.id.cdMechEng);
//        cdInfoTech = view.findViewById(R.id.cdInfoTech);
//        cdElecEng = view.findViewById(R.id.cdElecEng);
//        cdCivilEng = view.findViewById(R.id.cdCivilEng);
//        cdChemEng = view.findViewById(R.id.cdChemEng);
//        cdElecCom = view.findViewById(R.id.cdElecCom);
//        cdTexEng = view.findViewById(R.id.cdTexEng);
//        cdAeroEng = view.findViewById(R.id.cdAeroEng);
        rvCourseHome = view.findViewById(R.id.rvCourseHome);
    }


}
