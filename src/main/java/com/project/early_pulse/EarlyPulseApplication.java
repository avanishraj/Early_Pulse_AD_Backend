package com.project.early_pulse;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class EarlyPulseApplication {

	public static void main(String[] args) throws IOException {
		ClassLoader classLoader = EarlyPulseApplication.class.getClassLoader();
		File file = new File(Objects.requireNonNull(classLoader.getResource("early_pulse_firebase_key.json")).getFile());
		FileInputStream serviceAccount = new FileInputStream("D:/Early Pulse/Early_Pulse_AD_Backend/early_pulse/src/main/resources/early_pulse_firebase_key.json");


		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

		FirebaseApp.initializeApp(options);

		SpringApplication.run(EarlyPulseApplication.class, args);
	}

}
