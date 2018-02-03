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

public class CalcOxyConc extends AppCompatActivity {
    Oxygen o;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_oxy_conc);

//group of + - buttons to perform steper input
        TextView incrOxyFlow = findViewById(R.id.incrOxyFlow);
        TextView decrOxyFlow =  findViewById(R.id.decrOxyFlow);
        TextView incrAirFlow =  findViewById(R.id.incrAirFlow);
        TextView decrAirFlow =  findViewById(R.id.decrAirFlow);

        //calculate button
        TextView onCalcOxyConc =  findViewById(R.id.onCalcOxyConc);


        //group of to default buttons
        TextView toDefaultOxyFlow=findViewById(R.id.toDefaultOxyFlow);
        TextView toDefaultAirFlow=findViewById(R.id.toDefaultAirFlow);

        //input edit text fields
        final EditText inputOxyFlow =  findViewById(R.id.inputOxyFlow);
        final EditText inputAirFlow =  findViewById(R.id.inputAirFlow);

        //set default values
        String oxyFlowByDefault = getString(R.string.oxy_flow_by_default);
        String airFlowByDefault = getString(R.string.air_flow_by_default);
        inputOxyFlow.setText(oxyFlowByDefault);
        inputAirFlow.setText(airFlowByDefault);

        //group of + - buttons listeners implementation
        incrOxyFlow.setOnClickListener(new StepperInputListener(inputOxyFlow, 0.5d, "%.1f"));
        decrOxyFlow.setOnClickListener(new StepperInputListener(inputOxyFlow, -0.5d, "%.1f"));
        incrAirFlow.setOnClickListener(new StepperInputListener(inputAirFlow, 5, "%.0f"));
        decrAirFlow.setOnClickListener(new StepperInputListener(inputAirFlow, -5, "%.0f"));

        //group of to default buttons listeners implementation
        toDefaultOxyFlow.setOnClickListener(new ToDefaultListener(inputOxyFlow,oxyFlowByDefault));
        toDefaultAirFlow.setOnClickListener(new ToDefaultListener(inputAirFlow,airFlowByDefault));

        final Intent intent = new Intent(this, Answer.class);

        onCalcOxyConc.setOnClickListener(new OnClickListener() {
            public void onClick (View view){
                //textviews to display warning message if entered number is out of range
                TextView warningOxyFlow=findViewById(R.id.warning_oxyFlow);
                TextView warningAirFlow=findViewById(R.id.warning_airFlow);
                warningOxyFlow.setText("");
                warningAirFlow.setText("");
                o=(Oxygen) getIntent().getSerializableExtra("OxygenObject");
                double oxyFlow = OxyTools.setParam(inputOxyFlow);
                double airFlow = OxyTools.setParam(inputAirFlow);
                if (OxyTools.isCorrect(oxyFlow,0,40,warningOxyFlow)&&OxyTools.isCorrect(airFlow,100,300,warningAirFlow)) {
                    o.setOxyFlow(oxyFlow);
                    o.setAirFlow(airFlow);
                    o.calcOxyConc();
                    intent.putExtra("oxygenObjectTwo", o);
                    startActivity(intent);
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
        StepperInputListener (EditText editText, double step, String format) {
            this.editText=editText;
            this.step=step;
            this.format=format;
        }
        public void onClick(View view){
            incrementView();
        }
        private void incrementView() {
            hideKeyboard();
            double value=Double.valueOf(editText.getText().toString());
            value+=step;
            editText.setText(format(Locale.US,format,value));
        }
    }

    class ToDefaultListener implements View.OnClickListener{
        EditText editText=null;
        String value="";
        ToDefaultListener(EditText editText,String value) {
            this.editText=editText;
            this.value=value;
        }
        public void onClick(View view) {
            editText.setText(value);
        }
    }

}

