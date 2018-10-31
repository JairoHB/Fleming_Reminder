package com.gautam.medicinetime.data.source.local;

import android.content.Context;

import com.gautam.medicinetime.Session;
import com.gautam.medicinetime.data.source.History;
import com.gautam.medicinetime.data.source.MedicineAlarm;
import com.gautam.medicinetime.data.source.MedicineDataSource;
import com.gautam.medicinetime.data.source.Pills;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by gautam on 13/07/17.
 */

public class MedicinesLocalDataSource implements MedicineDataSource {

    private static MedicinesLocalDataSource mInstance;

    private MedicineDBHelper mDbHelper;

    private Session session;

    private String user_id;


    private MedicinesLocalDataSource(Context context) {
        mDbHelper = new MedicineDBHelper(context);
        session = new Session(context);
    }

    public static MedicinesLocalDataSource getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MedicinesLocalDataSource(context);
        }
        return mInstance;
    }


    @Override
    public void getMedicineHistory(LoadHistoryCallbacks loadHistoryCallbacks) {
        String tipo = session.gettype();
        String id = session.getid();
        //Preguntar que tipo de usuario es 1 para medico 2 para paciente
        if(tipo.equals("1")){
            user_id = mDbHelper.pac_id(id);
        }
        else if(tipo.equals("2")){
            user_id = id;
        }
        List<History> historyList = mDbHelper.getHistory(user_id);
        loadHistoryCallbacks.onHistoryLoaded(historyList);
    }

    @Override
    public void getMedicineAlarmById(long id, GetTaskCallback callback) {

        try {
            MedicineAlarm medicineAlarm = getAlarmById(id);
            if (medicineAlarm != null) {
                callback.onTaskLoaded(medicineAlarm);
            } else {
                callback.onDataNotAvailable();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            callback.onDataNotAvailable();
        }

    }

    @Override
    public void saveMedicine(MedicineAlarm medicineAlarm, Pills pill) {
        mDbHelper.createAlarm(medicineAlarm, pill.getPillId());
    }

    @Override
    public void getMedicineListByDay(int day, LoadMedicineCallbacks callbacks) {
        String tipo = session.gettype();
        String id = session.getid();
        //Preguntar que tipo de usuario es 1 para medico 2 para paciente
        if(tipo.equals("1")){
            user_id = mDbHelper.pac_id(id);
        }
        else if(tipo.equals("2")){
            user_id = id;
        }
        List<MedicineAlarm> medicineAlarmList = mDbHelper.getAlarmsByDay(day, user_id);
        callbacks.onMedicineLoaded(medicineAlarmList);
    }

    @Override
    public boolean medicineExits(String pillName) {
        for (Pills pill : getPills()) {
            if (pill.getPillName().equals(pillName))
                return true;
        }
        return false;
    }

    @Override
    public List<Long> tempIds() {
        return null;
    }

    @Override
    public void deleteAlarm(long alarmId) {
        deleteAlarmById(alarmId);
    }

    @Override
    public List<MedicineAlarm> getMedicineByPillName(String pillName) {
        try {
            return getMedicineByPill(pillName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Pills getPillsByName(String pillName) {
        return getPillByName(pillName);
    }

    @Override
    public long savePills(Pills pills) {
        return savePill(pills);
    }

    @Override
    public void saveToHistory(History history) {
        mDbHelper.createHistory(history);
    }

    private List<Pills> getPills() {
        return mDbHelper.getAllPills();
    }

    private long savePill(Pills pill) {
        long pillId = mDbHelper.createPill(pill);
        pill.setPillId(pillId);
        return pillId;
    }

    private Pills getPillByName(String pillName) {
        return mDbHelper.getPillByName(pillName);
    }

    private List<MedicineAlarm> getMedicineByPill(String pillName) throws URISyntaxException {
        return mDbHelper.getAllAlarmsByPill(pillName);
    }

    public void deletePill(String pillName) throws URISyntaxException {
        mDbHelper.deletePill(pillName);
    }

    private void deleteAlarmById(long alarmId) {
        mDbHelper.deleteAlarm(alarmId);
    }

    public void addToHistory(History h) {
        mDbHelper.createHistory(h);
    }

    public List<History> getHistory() {
        String tipo = session.gettype();
        String id = session.getid();
        //Preguntar que tipo de usuario es 1 para medico 2 para paciente
        if(tipo.equals("1")){
            user_id = mDbHelper.pac_id(id);
        }
        else if(tipo.equals("2")){
            user_id = id;
        }
        return mDbHelper.getHistory(user_id);
    }

    private MedicineAlarm getAlarmById(long alarm_id) throws URISyntaxException {
        String tipo = session.gettype();
        String id = session.getid();
        //Preguntar que tipo de usuario es 1 para medico 2 para paciente
        if(tipo.equals("1")){
            user_id = mDbHelper.pac_id(id);
        }
        else if(tipo.equals("2")){
            user_id = id;
        }
        return mDbHelper.getAlarmById(alarm_id, user_id);
    }

    public int getDayOfWeek(long alarm_id) throws URISyntaxException {
        return mDbHelper.getDayOfWeek(alarm_id);
    }


}
