package alexander.argunov.simpleindustrialcalculations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class Answer extends AppCompatActivity {
    Oxygen o;
    LinearLayout verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        //assigning  a value to a vertical layout instance variable
        verticalLayout = findViewById(R.id.vertical_layout);
        o = (Oxygen) getIntent().getSerializableExtra("oxygenObjectTwo");

        //print Oxygen object
        printResult(getString(R.string.air_flow),
                String.format(Locale.US, "%.0f", o.getAirFlow()), getString(R.string.tkm));
        printResult(getString(R.string.oxy_flow),
                String.format(Locale.US, "%.1f", o.getOxyFlow()), getString(R.string.tkm));
        printResult(getString(R.string.oxy_conc),
                String.format(Locale.US, "%.1f", o.getOxyConc()), getString(R.string.percent));
        printResult(getString(R.string.oxy_conc_dp),
                String.format(Locale.US, "%.1f", o.getFurnaceOxyConc()), getString(R.string.percent));
        printResult(getString(R.string.air_loss),
                String.format(Locale.US, "%.0f", o.getAirDissipation()), getString(R.string.tkm));
        printResult(getString(R.string.oxy_purity),
                String.format(Locale.US, "%.1f", o.getOxyPurity()), getString(R.string.percent));
        printResult(getString(R.string.oxy_in_air),
                String.format(Locale.US, "%.1f", o.getOxyInAir()), getString(R.string.percent));

        //creating a sendButton and setting it a listener
        final TextView sendButton = createButton(getString(R.string.send));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chooserTitle = getString(R.string.chooser);
                String subject = getString(R.string.subject);
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, OxyTools.oxyMessage(o));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                Intent chosenIntent = Intent.createChooser(sendIntent, chooserTitle);
                startActivity(chosenIntent);
            }
        });
        sendButton.setGravity(Gravity.CENTER);


        //creating a home button to return to main menu
        final TextView homeButton = createButton(getString(R.string.back_to_menu));
        final Intent homeIntent = new Intent(this, MenuActivity.class);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(homeIntent);
            }
        });

        //creating a Horizontal Linear Layout to take buttons
        LinearLayout.LayoutParams horParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        horParams.setMargins(convertDpToPixels(16), convertDpToPixels(8),
                convertDpToPixels(16), 0);
        //create and instantiate a horizontal layout for our text views
        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayout.setLayoutParams(horParams);

        //add views to a parent vertical layout
        horizontalLayout.addView(sendButton);
        horizontalLayout.addView(homeButton);
        //horizontalLayout.setBackgroundColor(Color.CYAN);
        verticalLayout.addView(horizontalLayout);
    }

    TextView createButton(String buttonsText) {
        final TextView button = new TextView(this);
        LinearLayout.LayoutParams lpButton = new LinearLayout.LayoutParams
                (0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lpButton.setMargins(convertDpToPixels(8), convertDpToPixels(8),
                0, 0);
        button.setLayoutParams(lpButton);
        button.setPadding(convertDpToPixels(8), convertDpToPixels(16),
                convertDpToPixels(8), convertDpToPixels(16));
        button.setText(buttonsText);
        button.setBackgroundColor(Color.parseColor("#e0e0e0"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.setTextAppearance(R.style.TextAppearance_AppCompat_Button);
        }
        button.setGravity(Gravity.CENTER);
        return button;
    }
    void printResult(String textOne, String value, String textThree) {
        if (!value.equals("0") && !value.equals("0.0")) {
            //create and instantiate a LayoutParams object
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(convertDpToPixels(16), convertDpToPixels(8),
                    convertDpToPixels(16), 0);
            //create and instantiate a horizontal layout for our text views
            LinearLayout parentLayout = new LinearLayout(this);
            parentLayout.setOrientation(LinearLayout.HORIZONTAL);
            parentLayout.setLayoutParams(params);

            //create three textviews
            TextView first = new TextView(this);
            TextView second = new TextView(this);
            TextView third = new TextView(this);

            //assigning parameters to the textviews
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 5);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f);
            LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 2);

            first.setLayoutParams(lp1);
            second.setLayoutParams(lp2);
            third.setLayoutParams(lp3);

            //setting text to a textviews
            first.setText(textOne);
            second.setText(value);
            third.setText(textThree);

            //adding textviews to a horizontal layout
            parentLayout.addView(first);
            parentLayout.addView(second);
            parentLayout.addView(third);

            //adding a horizontal layout to parent vertical layout
            verticalLayout.addView(parentLayout);
        }
    }

    int convertDpToPixels(int sizeInDp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }
}
