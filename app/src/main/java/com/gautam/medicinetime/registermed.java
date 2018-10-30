package com.gautam.medicinetime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gautam.medicinetime.data.source.local.MedicineDBHelper;

public class registermed extends Activity {

    MedicineDBHelper db;
    EditText username, email, password, password2;
    Button btnregistrar;
    ImageView imgreg, face, google, twitter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_medico);

        db= new MedicineDBHelper(this);
        username=(EditText)findViewById(R.id.txtreg_user);
        email=(EditText)findViewById(R.id.txtregemail);
        password=(EditText)findViewById(R.id.txtreg_pass);
        password2=(EditText)findViewById(R.id.txtreg_confpass);
        btnregistrar=(Button)findViewById(R.id.btnreg);

        //Iniciar imagenes
        imgreg=(ImageView)findViewById(R.id.imgreg);
        face=(ImageView)findViewById(R.id.imgfacereg);
        google=(ImageView)findViewById(R.id.imggooreg);
        twitter=(ImageView)findViewById(R.id.imgtwireg);
        imgreg.setImageResource(R.drawable.doctor);
        face.setImageResource(R.drawable.facebook);
        google.setImageResource(R.drawable.google);
        twitter.setImageResource(R.drawable.twitter);


        btnregistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usn = username.getText().toString();
                String ema = email.getText().toString();
                String pas = password.getText().toString();
                String pas2 = password2.getText().toString();
                if(usn.equals("") || ema.equals("") || pas.equals("") || pas2.equals("")){
                    Toast.makeText(getApplicationContext(),"No dejes ningun campo vacio", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (pas.equals(pas2)){
                        //user jairo27 pass 123 DOCTOR
                        Boolean chkeusername = db.chkusername(usn);
                        if(chkeusername==true){
                            Boolean chkemail = db.chkemail(ema);
                                if(chkemail==true) {
                                    Boolean insert1 = db.insertmedicos(usn, ema, pas);
                                    if (insert1 == true) {
                                        Toast.makeText(getApplicationContext(), "Registro Con Exito", Toast.LENGTH_SHORT).show();
                                        limpiar_pantalla();
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Corre Registrado, intenta otro", Toast.LENGTH_SHORT).show();
                                }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Usuario Registrado, intenta otro", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"¡Contraseñas no coiciden!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void limpiar_pantalla(){
        username.setText("");
        email.setText("");
        password.setText("");
        password2.setText("");
    }
}