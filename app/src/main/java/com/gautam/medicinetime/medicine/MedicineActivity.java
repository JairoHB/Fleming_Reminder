package com.gautam.medicinetime.medicine;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gautam.medicinetime.Injection;
import com.gautam.medicinetime.R;
import com.gautam.medicinetime.Session;
import com.gautam.medicinetime.registerpac;
import com.gautam.medicinetime.report.MonthlyReportActivity;
import com.gautam.medicinetime.utils.ActivityUtils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedicineActivity extends AppCompatActivity {

    @BindView(R.id.compactcalendar_view)
    CompactCalendarView mCompactCalendarView;

    @BindView(R.id.date_picker_text_view)
    TextView datePickerTextView;

    @BindView(R.id.date_picker_button)
    RelativeLayout datePickerButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;

    @BindView(R.id.fab_add_task)
    FloatingActionButton fabAddTask;

    @BindView(R.id.fab_sms_task)
    FloatingActionButton fabSmsTask;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.date_picker_arrow)
    ImageView arrow;

    private MedicinePresenter presenter;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", /*Locale.getDefault()*/Locale.ENGLISH);

    private boolean isExpanded = false;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        session = new Session(this);
        String tipo = session.gettype();
        String tel = session.gettel();

        //Preguntar que tipo de usuario es 1 para medico 2 para paciente
        if(tipo.equals("1")){
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sms_task);
            fab.setImageResource(R.drawable.ic_sms);
            fab.setVisibility(FloatingActionButton.GONE);
        }
        else if(tipo.equals("2")){
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addpac_task);
            fab.setImageResource(R.drawable.ic_action_addpac);
            fab.setVisibility(FloatingActionButton.GONE);
        }

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_addpac_task);
        fab1.setImageResource(R.drawable.ic_action_addpac);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicineActivity.this, registerpac.class);
                startActivity(intent);
            }
        });

        mCompactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);

        mCompactCalendarView.setShouldDrawDaysHeader(true);

        mCompactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateClicked);

                int day = calendar.get(Calendar.DAY_OF_WEEK);

                if (isExpanded) {
                    ViewCompat.animate(arrow).rotation(0).start();
                } else {
                    ViewCompat.animate(arrow).rotation(180).start();
                }
                isExpanded = !isExpanded;
                appBarLayout.setExpanded(isExpanded, true);
                presenter.reload(day);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
            }
        });
        setCurrentDate(new Date());
        MedicineFragment medicineFragment = (MedicineFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (medicineFragment == null) {
            medicineFragment = MedicineFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), medicineFragment, R.id.contentFrame);
        }

        //Create MedicinePresenter
        presenter = new MedicinePresenter(Injection.provideMedicineRepository(MedicineActivity.this), medicineFragment);

        //SMS MESSAGE
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sms_task);
        fab.setImageResource(R.drawable.ic_sms);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(tel);
            }
        });
    }

    public void makeCall(String tel)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tel));
        int result = ContextCompat.checkSelfPermission(MedicineActivity.this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){

            startActivity(intent);

        } else {
            requestPermission();
        }
    }

    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MedicineActivity.this,Manifest.permission.CALL_PHONE))
        {
        }
        else {
            ActivityCompat.requestPermissions(MedicineActivity.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
        }
    }
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String tel = session.gettel();
                    makeCall(tel);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medicine_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_stats) {
            Intent intent = new Intent(this, MonthlyReportActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        mCompactCalendarView.setCurrentDate(date);
    }

    /* public void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }*/

    public void setSubtitle(String subtitle) {
        datePickerTextView.setText(subtitle);
    }

    @OnClick(R.id.date_picker_button)
    void onDatePickerButtonClicked() {
        if (isExpanded) {
            ViewCompat.animate(arrow).rotation(0).start();
        } else {
            ViewCompat.animate(arrow).rotation(180).start();
        }

        isExpanded = !isExpanded;
        appBarLayout.setExpanded(isExpanded, true);
    }
}
