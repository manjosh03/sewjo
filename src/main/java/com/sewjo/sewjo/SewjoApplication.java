package com.sewjo.sewjo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class SewjoApplication {

	public static void main(String[] args)  {
		try {
			if (FirebaseApp.getApps().isEmpty()) {
				FileInputStream serviceAccount = new FileInputStream("/app/serviceAccountKey.json");

				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.build();

				FirebaseApp.initializeApp(options);
			}
		} catch (IOException error) {
			error.printStackTrace();
		}

		SpringApplication.run(SewjoApplication.class, args);
	}

}
