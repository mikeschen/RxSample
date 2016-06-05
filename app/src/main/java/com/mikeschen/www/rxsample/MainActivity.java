package com.mikeschen.www.rxsample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onStart() {
        super.onStart();

        final Pattern emailPattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        EditText unameEdit = (EditText)findViewById(R.id.edtUserName);
        EditText emailEdit = (EditText)findViewById(R.id.edtEmail);

        Observable<Boolean> userNameValid = WidgetObservable.text(unameEdit)
                .map(e -> e.text())
                .map(t -> t.length() > 4);
        Observable<Boolean> emailValid = WidgetObservable.text(emailEdit)
                .map(e -> e.text())
                .map(t -> emailPattern.matcher(t).matches());

        emailValid.map(b -> b ? Color.BLACK : Color.RED)
                .subscribe( color -> emailEdit.setTextColor(color));
        userNameValid.map(b -> b ? Color.BLACK : Color.RED)
                .subscribe( color -> unameEdit.setTextColor(color));

        Button registerButton = (Button) findViewById(R.id.btnRegister);

        Observable<Boolean> registerEnabled =
                Observable.combineLatest(userNameValid, emailValid, (a,b) -> a && b);
        registerEnabled.subscribe( enabled -> registerButton.setEnabled(enabled));
    }
}
