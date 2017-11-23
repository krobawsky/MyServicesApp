package pe.tecsup.com.myservicesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Trigger;

import pe.tecsup.com.myservicesapp.services.MyJobService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FirebaseJobDispatcher configuration
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(MainActivity.this));
        dispatcher.mustSchedule(
                dispatcher.newJobBuilder()
                        .setService(MyJobService.class)
                        .setTag("MyJobService")
                        .setRecurring(true)
                        .setTrigger(Trigger.executionWindow(5, 30)) // Cada 5 a 30 segundos
                        .build()
        );

    }
}
