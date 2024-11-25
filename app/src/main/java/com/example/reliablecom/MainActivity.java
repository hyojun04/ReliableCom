package com.example.reliablecom;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.reliablecom.main.Main;

public class MainActivity extends AppCompatActivity {

    private TextView consoleArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI components
        //Button btnSetup = findViewById(R.id.btn_setup);
        Button btnReceive = findViewById(R.id.btn_receive);
        consoleArea = findViewById(R.id.console_area);

        // Button click listeners
        /*
        btnSetup.setOnClickListener(v -> new Thread(() -> {
            try {
                Main.ComSetupResponse();
                runOnUiThread(() -> logToConsole("Setup completed successfully."));
            } catch (Exception e) {
                runOnUiThread(() -> logToConsole("Setup error: " + e.getMessage()));
            }
        }).start());*/

        btnReceive.setOnClickListener(v -> {
            logToConsole("Starting to receive data...");
            Thread main = new Thread(new Main());
            main.start();
        });

    }

    private void logToConsole(String message) {
        consoleArea.append("\n" + message);
    }
}
