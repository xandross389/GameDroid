package cu.com.xandross.gamedroid;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Locale;

public class ContactActivity extends AppCompatActivity {
    private FloatingActionButton fab;

    private ImageView imgPerson;
    private TextView txtPerson;
    private ImageView imgFixPhone;
    private TextView txtFixPhone;
    private ImageView imgMovilPhone;
    private TextView txtMovilPhone;
    private ImageView imgAddress;
    private TextView txtAddress;
    private ImageView imgMail;
    private TextView txtMail;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();

        setTitle("Contacto");

        fab = (FloatingActionButton) findViewById(R.id.fab);

        YoYo.with(Techniques.RotateInDownLeft)
                .duration(600)
                .playOn(fab);

        initViewObjects();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                writeSMS(txtMovilPhone.getText().toString(), "Sobre juegos");

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initViewObjects() {
        imgPerson = (ImageView) findViewById(R.id.imgPerson);
        txtPerson = (TextView) findViewById(R.id.txtPerson);

        imgFixPhone = (ImageView) findViewById(R.id.imgFixPhone);
        txtFixPhone = (TextView) findViewById(R.id.txtFixPhone);

        imgMovilPhone  = (ImageView) findViewById(R.id.imgMovilPhone);
        txtMovilPhone = (TextView) findViewById(R.id.txtMovilPhone);

        imgAddress = (ImageView) findViewById(R.id.imgAddress);
        txtAddress = (TextView) findViewById(R.id.txtAddress);

        imgMail = (ImageView) findViewById(R.id.imgMail);
        txtMail = (TextView) findViewById(R.id.txtMail);

        // agregar a contactos
        imgPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactPerson(txtPerson.getText().toString(), txtMovilPhone.getText().toString(),
                        "Movil", txtMail.getText().toString(), "Desarrollador","Desarrollador");
            }
        });

        txtPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactPerson(txtPerson.getText().toString(), txtMovilPhone.getText().toString(),
                        "Movil", txtMail.getText().toString(), "Desarrollador","Desarrollador");
            }
        });

        imgMovilPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoneCall(txtMovilPhone.getText().toString());
            }
        });

        txtMovilPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoneCall(txtMovilPhone.getText().toString());
            }
        });


        imgFixPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoneCall(txtFixPhone.getText().toString());
            }
        });

        txtFixPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoneCall(txtFixPhone.getText().toString());
            }
        });

        imgMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] addressesArray = {txtMail.getText().toString()};
                sendEmail(addressesArray, "Sobre los juegos");
            }
        });

        txtMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] addressesArray = {txtMail.getText().toString()};
                sendEmail(addressesArray, "Sobre los juegos");
            }
        });

        imgAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locateCoordenates("23.091251", "-82.41972", null);
            }
        });

        txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locateCoordenates("23.091251", "-82.41972", null);
            }
        });
    }


    void pickPhoneCall(String pPhoneNumber) {
        try{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(
                    Uri.parse("tel:"+pPhoneNumber));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }catch(Exception e){
            Toast.makeText(getBaseContext(),
                    "Error intentando realizar llamada ("+e.getMessage()+")",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    void sendEmail(String[] addresses, String subject) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }catch(Exception e){
            Toast.makeText(getBaseContext(),
                    "Error intentando realizar llamada ("+e.getMessage()+")",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    void addContactPerson(String personName, String phone, String phoneType,
                          String email, String jobTitle, String comment) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, personName);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, phoneType);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
        intent.putExtra(ContactsContract.Intents.Insert.NOTES, comment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    void locateCoordenates(String latitud, String longitud, Integer zoomLevel) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        String data = String.format("geo:%s,%s", latitud, longitud);

//        if (zoomLevel != null) {
//            data = String.format("%s?z=%s", data, zoomLevel);
//        }

//        intent.setData(Uri.parse(data));
//        startActivity(intent);

//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitud, longitud);
        String uri = "geo:"+latitud.trim()+","+longitud.trim();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        mContext.startActivity(intent);
        mContext.startActivity(intent);
    }

    void locateCoordenates(String latitud, String longitud, String tag, Integer zoomLevel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        String data = String.format("geo:%s,%s", latitud, longitud);

//        if (zoomLevel != null) {
//            data = String.format("%s?z=%s",data, zoomLevel);
//        }

        intent.setData(Uri.parse(data));
        mContext.startActivity(intent);
    }

    void writeSMS(String destinationNumber){
        //String number = "5354389534";  // The number on which you want to send SMS
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", destinationNumber, null)));
    }

    void writeSMS(String destinationNumber, String Message){
        Uri uri = Uri.parse("smsto:"+destinationNumber.trim());
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "Sobre juegos");
        mContext.startActivity(it);
    };



}
