package com.mikeschen.www.rxsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

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

        Observable<OnTextChangeEvent> userNameText =
                WidgetObservable.text((EditText) findViewById(R.id.edtUserName));

        userNameText.filter( e -> e.text().length() > 4).subscribe( e -> Log.d("[Rx]", e.text().toString()));
    }
}
