package com.gautam.medicinetime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;
import com.gautam.medicinetime.data.source.local.MedicineDBHelper;
import com.gautam.medicinetime.medicine.MedicineActivity;
import com.gautam.medicinetime.utils.PreferenceUtils;


public class login extends AppCompatActivity {
    MedicineDBHelper db;
    EditText username, password;
    Button btninicio;
    RadioButton rbdoc, rbpac;

    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initViews();

        session = new Session(this);
        db= new MedicineDBHelper(this);
        username=(EditText)findViewById(R.id.txtuser);
        password=(EditText)findViewById(R.id.txtpass);
        btninicio=(Button)findViewById(R.id.btnlogin);
        rbdoc=(RadioButton)findViewById(R.id.rbDoctor);
        rbpac=(RadioButton)findViewById(R.id.rbPaciente);
        btninicio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usn = username.getText().toString();
                String pas = password.getText().toString();
                if(rbdoc.isChecked()){
                    String login1 = db.login_doc(usn, pas);
                    //username=jairo27 pass=123
                    if(!login1.equals("0")){
                        Toast.makeText(getApplicationContext(),"Incio Sesión Con Exito", Toast.LENGTH_SHORT).show();
                        session.setusername(usn);
                        session.settype("1");
                        session.setid(login1);
                        PreferenceUtils.saveuser(usn, getApplicationContext());
                        PreferenceUtils.savePassword(pas, getApplicationContext());
                        Intent intent = new Intent(login.this, MedicineActivity.class);
                        startActivity(intent);
                    }
                    else if (login1.equals("0")){
                        Toast.makeText(getApplicationContext(),"Contraseña o Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(rbpac.isChecked()){
                    String login1 = db.login_pac(usn, pas);
                    //username=jairo27 pass=123
                    if(!login1.equals("0")){
                        Toast.makeText(getApplicationContext(),"Incio Sesión Con Exito", Toast.LENGTH_SHORT).show();
                        session.setusername(usn);
                        session.settype("2");
                        session.settel(login1);
                        PreferenceUtils.saveuser(usn, getApplicationContext());
                        PreferenceUtils.savePassword(pas, getApplicationContext());
                        Intent intent = new Intent(login.this, MedicineActivity.class);
                        startActivity(intent);
                    }
                    else if (login1.equals("0")){
                        Toast.makeText(getApplicationContext(),"Contraseña o Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void registeractivity(View v){
        TextView register = (TextView) findViewById(R.id.txtviewreg);
        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(login.this, registermed.class);
                startActivity(intent);
            }
        });
    }

    private void initViews(){
        PreferenceUtils utils = new PreferenceUtils();
        if (utils.getuser(this) != null ){
            Intent intent = new Intent(login.this, MedicineActivity.class);
            startActivity(intent);
        }else{

        }
    }
}
