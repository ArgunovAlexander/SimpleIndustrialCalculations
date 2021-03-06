package alexander.argunov.simpleindustrialcalculations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import static java.lang.String.format;

public class MenuActivity extends AppCompatActivity {
    EditText inputOxyPurity;
    EditText inputOxyInAirPerc;
    Oxygen o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //assigning input EditTexts to value
        inputOxyPurity = findViewById(R.id.inputOxyPurity);
        inputOxyInAirPerc = findViewById(R.id.inputOxyInAirPerc);

        // + & - TextViews
        TextView incrOxyInAir = findViewById(R.id.incrOxyInAir);
        TextView decrOxyInAir = findViewById(R.id.decrOxyInAir);
        TextView incrOxyPurity = findViewById(R.id.incrOxyPurity);
        TextView decrOxyPurity = findViewById(R.id.decrOxyPurity);

        //toDefault TextViews
        TextView toDefaultOxyPurity = findViewById(R.id.toDefaultOxyPurity);
        TextView toDefaultOxyInAir = findViewById(R.id.toDefaultOxyInAir);

        //Buttons TextViews to fire Variant1 & Variant2 Activities
        TextView oxyFlow = findViewById(R.id.oxyFlow);
        TextView oxyConc = findViewById(R.id.oxyConc);
        TextView airLoss = findViewById(R.id.air_loss);

        //set default values
        String oxyPurityByDefault = getString(R.string.oxy_purity_by_default);
        String OxyInAirByDefault = getString(R.string.oxy_in_air_by_default);
        inputOxyInAirPerc.setText(OxyInAirByDefault);
        inputOxyPurity.setText(oxyPurityByDefault);


        incrOxyInAir.setOnClickListener(new StepperInputListener(inputOxyInAirPerc, 0.1d, "%.1f"));
        decrOxyInAir.setOnClickListener(new StepperInputListener(inputOxyInAirPerc, -0.1d, "%.1f"));
        incrOxyPurity.setOnClickListener(new StepperInputListener(inputOxyPurity, 0.1d, "%.1f"));
        decrOxyPurity.setOnClickListener(new StepperInputListener(inputOxyPurity, -0.1d, "%.1f"));

        toDefaultOxyPurity.setOnClickListener(new ToDefaultListener(inputOxyPurity, oxyPurityByDefault));
        toDefaultOxyInAir.setOnClickListener(new ToDefaultListener(inputOxyInAirPerc, OxyInAirByDefault));

        oxyFlow.setOnClickListener(new ButtonsListener(this, CalcOxyFlow.class));
        oxyConc.setOnClickListener(new ButtonsListener(this, CalcOxyConc.class));
        airLoss.setOnClickListener(new ButtonsListener(this, DetailedSolution.class));

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
            double value = Double.valueOf(editText.getText().toString());
            value += step;
            editText.setText(format(Locale.US, format, value));
        }
    }

    class ButtonsListener implements View.OnClickListener {
        Class<?> cls;
        Context packageContext;

        ButtonsListener(Context packageContext, Class<?> cls) {
            this.cls = cls;
            this.packageContext = packageContext;
        }

        public void onClick(View view) {
            double oxyPur = Double.valueOf(inputOxyPurity.getText().toString());
            double oxyPer = Double.valueOf(inputOxyInAirPerc.getText().toString());
            TextView warningOxyPurity = findViewById(R.id.warning_oxyPurity);
            TextView warningOxyInAir = findViewById(R.id.warning_oxyInAir);
            String message = getString(R.string.warning_mes_enter_between);
            if (OxyTools.isCorrect(oxyPur, 20, 99.9f, warningOxyPurity, message) && OxyTools.isCorrect(oxyPer, 20, 22, warningOxyInAir, message)) {
                Intent intent = new Intent(packageContext, cls);
                o = new Oxygen();
                o.setOxyPurity(oxyPur);
                o.setOxyInAir(oxyPer);
                intent.putExtra("OxygenObject", o);
                startActivity(intent);
            }
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
