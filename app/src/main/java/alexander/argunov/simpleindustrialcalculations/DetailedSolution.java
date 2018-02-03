package alexander.argunov.simpleindustrialcalculations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import static java.lang.String.format;

public class DetailedSolution extends AppCompatActivity {
    Oxygen o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_solution);

        //group of + - buttons to perform steper input
        TextView incrOxyPer = findViewById(R.id.incrOxyPer);
        TextView decrOxyPer = findViewById(R.id.decrOxyPer);
        TextView incrOxyFlow = findViewById(R.id.incrOxyFlow);
        TextView decrOxyFlow = findViewById(R.id.decrOxyFlow);
        TextView incrAirFlow = findViewById(R.id.incrAirFlow);
        TextView decrAirFlow = findViewById(R.id.decrAirFlow);

        //calculate button
        TextView onCalcDissipation = findViewById(R.id.onCalcDissipation);

        //group of to default buttons
        TextView toDefaultOxyConc = findViewById(R.id.toDefaultOxyConc);
        TextView toDefaultOxyFlow = findViewById(R.id.toDefaultOxyFlow);
        TextView toDefaultAirFlow = findViewById(R.id.toDefaultAirFlow);

        //input edit text fields
        final EditText inputOxyFlow = findViewById(R.id.inputOxyFlow);
        final EditText inputAirFlow = findViewById(R.id.inputAirFlow);
        final EditText inputFurnaceOxyConc = findViewById(R.id.inputFurnaceOxyConc);

        //getting doubles from the intent
        o = (Oxygen) getIntent().getSerializableExtra("OxygenObject");
        final double oxyInAir = o.getOxyInAir();

        //set edit text default value
        String oxyFlowByDefault = getString(R.string.oxy_flow_by_default);
        String airFlowByDefault = getString(R.string.air_flow_by_default);
        String furnaceOxyConcByDef = getString(R.string.furnace_oxy_conc_by_default);

        inputOxyFlow.setText(oxyFlowByDefault);
        inputAirFlow.setText(airFlowByDefault);
        inputFurnaceOxyConc.setText(furnaceOxyConcByDef);

        //group of + - buttons listeners implementation
        incrOxyFlow.setOnClickListener(new StepperInputListener(inputOxyFlow, 0.5d, "%.1f"));
        decrOxyFlow.setOnClickListener(new StepperInputListener(inputOxyFlow, -0.5d, "%.1f"));
        incrAirFlow.setOnClickListener(new StepperInputListener(inputAirFlow, 5, "%.0f"));
        decrAirFlow.setOnClickListener(new StepperInputListener(inputAirFlow, -5, "%.0f"));
        incrOxyPer.setOnClickListener(new StepperInputListener(inputFurnaceOxyConc, 0.1d, "%.1f"));
        decrOxyPer.setOnClickListener(new StepperInputListener(inputFurnaceOxyConc, -0.1d, "%.1f"));

        //group of to default buttons listeners implementation
        toDefaultOxyFlow.setOnClickListener(new ToDefaultListener(inputOxyFlow, oxyFlowByDefault));
        toDefaultAirFlow.setOnClickListener(new ToDefaultListener(inputAirFlow, airFlowByDefault));
        toDefaultOxyConc.setOnClickListener(new ToDefaultListener(inputFurnaceOxyConc, furnaceOxyConcByDef));

        final Intent intent = new Intent(this, Answer.class);

        //find air dissipation method implementation
        onCalcDissipation.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                //find textviews to display warning message
                // if entered number is out of range
                TextView warningOxyFlow = findViewById(R.id.warning_oxyFlow);
                TextView warningAirFlow = findViewById(R.id.warning_airFlow);
                TextView warningOxyConc = findViewById(R.id.warning_oxyConc);
                //set warning messages to empty field
                warningOxyFlow.setText("");
                warningAirFlow.setText("");
                warningOxyConc.setText("");
                //get doubles from input views
                double oxyFlow = OxyTools.setParam(inputOxyFlow);
                double airFlow = OxyTools.setParam(inputAirFlow);
                double furnaceOxyConc = OxyTools.setParam(inputFurnaceOxyConc);
                //assigning values to Oxygen private variables
                o.setOxyFlow(oxyFlow);
                o.setAirFlow(airFlow);
                o.setFurnaceOxyConc(furnaceOxyConc);
                //checking if doubles from input views are correct
                // and display warning message if they are not
                //then put extra to the intent and start Answer activity
                if (OxyTools.isCorrect(oxyFlow, 0, 40, warningOxyFlow) &&
                        OxyTools.isCorrect(airFlow, 100, 300, warningAirFlow) &&
                        OxyTools.isCorrect(furnaceOxyConc, o.getOxyInAir(), o.calcOxyConc(), warningOxyConc)) {
                    o.calcOxyConc();
                    if (o.calcAirDissipation() + o.getAirFlow() < 300) {
                        intent.putExtra("oxygenObjectTwo", o);
                        startActivity(intent);
                    } else {
                        warningOxyConc.setText("При таком проценте потери превышают выработку дутья. Процент должен быть выше, но не более " + String.format(Locale.US, "%.1f", o.getOxyConc()));
                    }
                }
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    class StepperInputListener implements View.OnClickListener {
        EditText editText;
        double step;
        String format;

        StepperInputListener(EditText editText, double step, String format) {
            this.editText = editText;
            this.step = step;
            this.format = format;
        }

        public void onClick(View view) {
            incrementView();
        }

        private void incrementView() {
            hideKeyboard();
            double value = Double.valueOf(editText.getText().toString());
            value += step;
            editText.setText(format(Locale.US, format, value));
        }
    }

    class ToDefaultListener implements View.OnClickListener {
        EditText editText = null;
        String value = "";

        ToDefaultListener(EditText editText, String value) {
            this.editText = editText;
            this.value = value;
        }

        public void onClick(View view) {
            editText.setText(value);
        }
    }
}

