package com.mikeschen.www.rxsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.regex.Pattern;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    private CompositeSubscription compositeSubs = new CompositeSubscription();

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
        Button registerButton = (Button) findViewById(R.id.btnRegister);

        Observable<Boolean> userNameValid = RxTextView.textChanges(unameEdit)
                .map(t -> t.length() > 4);

        Observable<Boolean> emailValid = RxTextView.textChanges(emailEdit)
                .map(t -> emailPattern.matcher(t).matches());

        compositeSubs.add(userNameValid.distinctUntilChanged()
                .doOnNext( b -> Log.d("[Rx]", "UserName " + (b ? "Valid" : "Invalid")))
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(c -> unameEdit.setTextColor(c)));

        compositeSubs.add(emailValid.distinctUntilChanged()
                .doOnNext( b -> Log.d("[Rx]", "Email " + (b ? "Valid" : "Invalid")))
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(color -> emailEdit.setTextColor(color)));

        Observable<Boolean> registerEnabled =
                Observable.combineLatest(userNameValid, emailValid, (a,b) -> a && b);

        compositeSubs.add(registerEnabled.distinctUntilChanged()
                .doOnNext( b -> Log.d("[Rx]", "Button Active " + (b ? "Valid" : "Invalid")))
                .subscribe( enabled -> registerButton.setEnabled(enabled)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubs.unsubscribe();
    }
}
