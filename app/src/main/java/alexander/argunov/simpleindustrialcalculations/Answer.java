package alexander.argunov.simpleindustrialcalculations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class Answer extends AppCompatActivity {
    Oxygen o;
    LinearLayout verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        verticalLayout = findViewById(R.id.vertical_layout);
        o = (Oxygen) getIntent().getSerializableExtra("oxygenObjectTwo");

        printResult("Расход дутья", String.format(Locale.US, "%.0f", o.getAirFlow()), "тыс.м3/ч");
        printResult("Расход кислорода", String.format(Locale.US, "%.1f", o.getOxyFlow()), "тыс.м3/ч");
        printResult("Концентрация кислорода в дутье", String.format(Locale.US, "%.1f", o.getOxyConc()), "%");
        printResult("Концентрация кислорода в дутье по прибору на ДП", String.format(Locale.US, "%.0f", o.getFurnaceOxyConc()), "%");
        printResult("Потери дутья", String.format(Locale.US, "%.0f", o.getAirDissipation()), "тыс.м3/ч");
        printResult("Чистота кислорода", String.format(Locale.US, "%.1f", o.getOxyPurity()), "%");
        printResult("Содержание кислорода в атмосфере", String.format(Locale.US, "%.1f", o.getOxyInAir()), "%");

        final TextView sendButton = new TextView(this);
        LinearLayout.LayoutParams lpButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpButton.setMargins(convertDpToPixels(16), convertDpToPixels(8), 0, 0);
        sendButton.setLayoutParams(lpButton);
        sendButton.setPadding(convertDpToPixels(8), convertDpToPixels(8), convertDpToPixels(8), convertDpToPixels(8));
        sendButton.setAllCaps(true);
        sendButton.setText("Отправить результаты расчета");
        sendButton.setBackgroundColor(Color.parseColor("#e0e0e0"));
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
        sendButton.setGravity(View.TEXT_ALIGNMENT_CENTER);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(rp);
        //relativeLayout.setBackgroundColor(Color.RED);
        relativeLayout.setGravity(RelativeLayout.CENTER_HORIZONTAL);
        sendButton.setGravity(RelativeLayout.CENTER_HORIZONTAL);
        relativeLayout.addView(sendButton);
        verticalLayout.addView(relativeLayout);
    }

    void printResult(String textOne, String value, String textThree) {
        if (!value.equals("0")) {
            LinearLayout parentLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(convertDpToPixels(16), convertDpToPixels(8), convertDpToPixels(16), 0);
            parentLayout.setOrientation(LinearLayout.HORIZONTAL);
            parentLayout.setLayoutParams(params);
            //parentLayout.setBackgroundColor(Color.RED);

            TextView first = new TextView(this);
            TextView second = new TextView(this);
            TextView third = new TextView(this);

            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 5);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f);
            LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2);

//            lp1.setMargins(convertDpToPixels(16),convertDpToPixels(8),convertDpToPixels(16),0);
//            lp2.setMargins(convertDpToPixels(16),convertDpToPixels(8),convertDpToPixels(16),0);
//            lp2.setMargins(convertDpToPixels(16),convertDpToPixels(8),convertDpToPixels(16),0);

            first.setLayoutParams(lp1);
            second.setLayoutParams(lp2);
            third.setLayoutParams(lp3);

            first.setText(textOne);
            second.setText(value);
            third.setText(textThree);

            parentLayout.addView(first);
            parentLayout.addView(second);
            parentLayout.addView(third);

            verticalLayout.addView(parentLayout);
        }
    }

    int convertDpToPixels(int sizeInDp) {
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
        return dpAsPixels;
    }
}
