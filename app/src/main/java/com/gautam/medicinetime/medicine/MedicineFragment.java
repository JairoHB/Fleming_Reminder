package com.gautam.medicinetime.medicine;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gautam.medicinetime.R;
import com.gautam.medicinetime.Session;
import com.gautam.medicinetime.addmedicine.AddMedicineActivity;
import com.gautam.medicinetime.data.source.MedicineAlarm;
import com.gautam.medicinetime.data.source.local.MedicineDBHelper;
import com.gautam.medicinetime.views.RobotoLightTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by gautam on 13/07/17.
 */

public class MedicineFragment extends Fragment implements MedicineContract.View {

    @BindView(R.id.medicine_list)
    RecyclerView rvMedList;

    Unbinder unbinder;
    @BindView(R.id.noMedIcon)
    ImageView noMedIcon;

    @BindView(R.id.noMedText)
    RobotoLightTextView noMedText;

    @BindView(R.id.add_med_now)
    TextView addMedNow;

    @BindView(R.id.no_med_view)
    View noMedView;

    @BindView(R.id.progressLoader)
    ProgressBar progressLoader;


    private MedicineContract.Presenter presenter;

    private MedicineAdapter medicineAdapter;

    private Session session;
    MedicineDBHelper db;


    public MedicineFragment() {

    }

    public static MedicineFragment newInstance() {
        Bundle args = new Bundle();
        MedicineFragment fragment = new MedicineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        session = new Session(getContext());
        db= new MedicineDBHelper(getContext());
        Integer userid = 0;
        String us = "0";
        String tipo = session.gettype();
        String id = session.getid();
        //Preguntar que tipo de usuario es 1 para medico 2 para paciente
        if(tipo.equals("1")){
           us = db.pac_id(id);
        }
        else if(tipo.equals("2")){
            us = id;
        }
        userid = Integer.valueOf(us);
        ArrayList<MedicineAlarm> lista = db.consultarListaMedicine(userid);
        medicineAdapter = new MedicineAdapter(lista);
        if(lista.size()>=1){
            medicineAdapter = new MedicineAdapter(lista);
        }
        else
        {
            Toast.makeText(getContext(),"23", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        unbinder = ButterKnife.bind(this, view);
        setAdapter();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addNewMedicine();
            }
        });
    }

    private void setAdapter() {
        rvMedList.setAdapter(medicineAdapter);
        rvMedList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedList.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        presenter.onStart(day);
    }

    @Override
    public void setPresenter(MedicineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        progressLoader.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMedicineList(List<MedicineAlarm> medicineAlarmList) {
        /*medicineAdapter.replaceData(medicineAlarmList);
        rvMedList.setVisibility(View.VISIBLE);
        noMedView.setVisibility(View.GONE);*/
    }

    @Override
    public void showAddMedicine() {
        Intent intent = new Intent(getContext(), AddMedicineActivity.class);
        startActivityForResult(intent, AddMedicineActivity.REQUEST_ADD_TASK);
    }


    @Override
    public void showMedicineDetails(long taskId, String medName) {
        Intent intent = new Intent(getContext(), AddMedicineActivity.class);
        intent.putExtra(AddMedicineActivity.EXTRA_TASK_ID, taskId);
        intent.putExtra(AddMedicineActivity.EXTRA_TASK_NAME, medName);
        startActivity(intent);
    }


    @Override
    public void showLoadingMedicineError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showNoMedicine() {
        showNoTasksViews(
                getResources().getString(R.string.no_medicine_added),
                R.drawable.icon_my_health
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_me_message));
    }

    private void showMessage(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.add_med_now)
    void addMedicine() {
        showAddMedicine();
    }

    private void showNoTasksViews(String mainText, int iconRes) {
        rvMedList.setVisibility(View.GONE);
        noMedView.setVisibility(View.VISIBLE);
        noMedText.setText(mainText);
        noMedIcon.setImageDrawable(getResources().getDrawable(iconRes));
        addMedNow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }
}

