package com.example.lab32;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog.Builder;

public class MainActivity extends AppCompatActivity {

    private int totalIterations = 0;
    double P = 4.0;
    int[][] points =  {{0, 6}, {1, 5}, {3, 3}, {2, 4}};
    double w1, w2;
    TextView TextView;


    void trainingFunction(double r, int iter, long end) {
        double y;
        double dt;
        int iterations = 0 ;
        boolean done = false;
        w1 = 0;
        w2 = 0;
        long start = System.nanoTime();
        int index = 0;
        while (iterations++ < iter && (System.nanoTime() - start) < end) {
            index %= 4;
            y = points[index][0] * w1 + points[index][1] * w2;
            if (isDone()) {
                done = true;
                break;
            }
            dt = P - y;
            w1 += dt * points[index][0] * r;
            w2 += dt * points[index][1] * r;
            index++;
        }
        if (done) {
            long execTimeMcs = (System.nanoTime() - start) / 1_000;
            totalIterations = iterations;
            TextView.setText(
                    String.format(
                            "Trained successfully!\n" +
                                    "w1 = %-6.3f w2 = %-6.3f\n" +
                                    "Execution time: %d mcs", w1, w2, execTimeMcs
                    )
            );

        } else {
            String reason = "Training unsuccessful!";
            if (iterations >= iter) {
                reason += "\nNeed more iterations";
            } else {
                reason += "\nNeed more time";
            }
            TextView.setText(reason);
        }

    }

    private boolean isDone() {
        return P < points[0][0] * w1 + points[0][1] * w2 && P < points[1][0] * w1 + points[1][1] * w2 &&
                P > points[2][0] * w1 + points[2][1] * w2 && P > points[3][0] * w1 + points[3][1] * w2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final EditText deadlineEditText = (EditText) findViewById(R.id.deadlineEditText);
        final EditText iterationsEditText = (EditText) findViewById(R.id.iterationsEditText);
        final EditText rateEditText = (EditText) findViewById(R.id.rateEditText);
        final TextView popUpView = (TextView) findViewById(R.id.popUpView);

        Button calcButton = (Button) findViewById(R.id.calcButtonl2);
        TextView = (TextView) findViewById(R.id.resTextViewl2);

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Builder builder = new Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Total Iterations");

                try {
                    int iter = Integer.parseInt(iterationsEditText.getText().toString());
                    double learningRate = Double.parseDouble(rateEditText.getText().toString());
                    long timeDeadline = (long) (Double.parseDouble(deadlineEditText.getText().toString()) * 1_000_000_000);
                    trainingFunction(learningRate, iter, timeDeadline);
                    builder.setMessage("Total number of iterations: " + totalIterations);
                    builder.show();

                } catch (NumberFormatException e) {
                    TextView.setText("Wrong input");
                }
            }
        });
    }
}
