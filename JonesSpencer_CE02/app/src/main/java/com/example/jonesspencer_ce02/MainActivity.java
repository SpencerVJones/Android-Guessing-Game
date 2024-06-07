// Spencer Jones
// MDV3832-0 - 062024
// MainActivity.java

package com.example.jonesspencer_ce02;

// Imports
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private List<EditText> guessFields; // List for EditText fields
    private int[] randomNumber; // Array for random generated numbers
    private int attemptsLeft; // Variable to number of track attempts left

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initializing
        guessFields = new ArrayList<>();
        // Adding to list
        guessFields.add(findViewById(R.id.editText1));
        guessFields.add(findViewById(R.id.editText2));
        guessFields.add(findViewById(R.id.editText3));
        guessFields.add(findViewById(R.id.editText4));
        // Getting submit button
        Button submitButton = findViewById(R.id.submitButton);

        randomNumber = new int[4]; // Initializing
        Random random = new Random(); // Creating random object
        // Loop to generate 4 random numbers (0-9)
        for (int i = 0; i < 4; i++) {
            randomNumber[i] = random.nextInt(10);
        }

        // Set click listener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If not valid input show alert
                if (!validateInput()) {
                    showToast("Please enter four valid numbers.");
                    return;
                }

                int[] userGuess = new int[4]; // Array to hold guess
                // Loop to get guess
                for (int i = 0; i < 4; i++) {
                    userGuess[i] = Integer.parseInt(guessFields.get(i).getText().toString());
                }

                boolean correctGuess = true; // Track if correct guess
                // Loop to compare guess with random numbers
                for (int i = 0; i < 4; i++) {
                    if (userGuess[i] < randomNumber[i]) { // To Low: Blue
                        guessFields.get(i).setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                        correctGuess = false;
                    } else if (userGuess[i] > randomNumber[i]) { // To High: Red
                        guessFields.get(i).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        correctGuess = false;
                    } else { // Correct : Green
                        guessFields.get(i).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    }
                }

                attemptsLeft--;

                // If attempts are left alert user on how many
                if (!correctGuess && attemptsLeft > 0) {
                    showToast("Attempts left: " + attemptsLeft);
                }

                // If all correct show win alert
                if (correctGuess) {
                    showDialog("Congratulations! You've won!", "Restart");

                // If no more attempts show lose alert
                } else if (attemptsLeft == 0) {
                    showDialog("Sorry! You've run out of attempts.", "Restart");
                }
            }
        });

        generateRandomNumber();
        attemptsLeft = 4;
    }

    // Method to generate 4 random numbers (0-9)
    private void generateRandomNumber() {
        randomNumber = new Random().ints(0, 10).limit(4).toArray();
    }

    // Method to generate user input
    private boolean validateInput() {
        // Looping through EditText fields
        for (EditText field : guessFields) {
            String value = field.getText().toString(); // Get text
            // If invalid, return false (else return true)
            if (value.isEmpty() || value.length() > 1 || !Character.isDigit(value.charAt(0))) {
                return false;
            }
        }
        return true;
    }

    // Method to show toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method to show dialog
    private void showDialog(String message, String buttonText) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(buttonText, (dialog, which) -> {
                    generateRandomNumber();
                    resetGame();
                })
                .setCancelable(false)
                .show();
    }

    // Method to reset game
    private void resetGame() {
        // Looping through EditText fields
        for (EditText field : guessFields) {
            field.setText(""); // Clear text
            field.setTextColor(getResources().getColor(android.R.color.black)); // Text color: Black
        }
        attemptsLeft = 4; // Reset attempts
    }
}