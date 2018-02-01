package alexander.argunov.simpleindustrialcalculations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import static java.lang.String.format;

public class MenuActivity extends Activity {
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

        //set default values
        String oxyPurityByDefault = "99.5";
        String OxyInAirByDefault = "20.7";
        inputOxyPurity.setText(oxyPurityByDefault);
        inputOxyInAirPerc.setText(OxyInAirByDefault);

        incrOxyInAir.setOnClickListener(new StepperInputListener(inputOxyInAirPerc, 0.1d, "%.1f"));
        decrOxyInAir.setOnClickListener(new StepperInputListener(inputOxyInAirPerc, -0.1d, "%.1f"));
        incrOxyPurity.setOnClickListener(new StepperInputListener(inputOxyPurity, 0.1d, "%.1f"));
        decrOxyPurity.setOnClickListener(new StepperInputListener(inputOxyPurity, -0.1d, "%.1f"));

        toDefaultOxyPurity.setOnClickListener(new ToDefaultListener(inputOxyPurity, oxyPurityByDefault));
        toDefaultOxyInAir.setOnClickListener(new ToDefaultListener(inputOxyInAirPerc, OxyInAirByDefault));

        oxyFlow.setOnClickListener(new ButtonsListener(this, CalcOxyFlow.class));
        oxyConc.setOnClickListener(new ButtonsListener(this, CalcOxyConc.class));
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
            warningOxyInAir.setText("");
            warningOxyPurity.setText("");

            if (OxyTools.isCorrect(oxyPur, 20, 99.9f, warningOxyPurity) && OxyTools.isCorrect(oxyPer, 20, 22, warningOxyInAir)) {
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
