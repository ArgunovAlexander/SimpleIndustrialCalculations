package alexander.argunov.simpleindustrialcalculations;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

import static java.lang.String.format;
import java.util.Locale;

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
        final TextView onCalcMore=    findViewById(R.id.onCalcMore);
        onCalcMore.setEnabled(false);

        //group of to default buttons
        TextView toDefaultOxyFlow=findViewById(R.id.toDefaultOxyFlow);
        TextView toDefaultAirFlow=findViewById(R.id.toDefaultAirFlow);

        //input edit text fields
        final EditText inputOxyFlow =  findViewById(R.id.inputOxyFlow);
        final EditText inputAirFlow =  findViewById(R.id.inputAirFlow);

        //output textview field
        final TextView outputData =  findViewById(R.id.outputData);

        //set default values
        String oxyFlowByDefault="3";
        String airFlowByDefault="200";
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

        final Intent intent = new Intent(this, DetailedSolution.class);

        onCalcOxyConc.setOnClickListener(new OnClickListener() {
            public void onClick (View view){
                //textviews to display warning message if entered number is out of range
                TextView warningOxyFlow=findViewById(R.id.warning_oxyFlow);
                TextView warningAirFlow=findViewById(R.id.warning_airFlow);
                warningOxyFlow.setText("");
                warningAirFlow.setText("");
                o=(Oxygen) getIntent().getSerializableExtra("OxygenObject");
                double oxyInAir = o.getOxyInAir();
                double oxyPurity = o.getOxyPurity();
                double oxyFlow = OxyTools.setParam(inputOxyFlow);
                double airFlow = OxyTools.setParam(inputAirFlow);
                if (OxyTools.isCorrect(oxyFlow,0,40,warningOxyFlow)&&OxyTools.isCorrect(airFlow,100,300,warningAirFlow)) {
                    o.setOxyFlow(oxyFlow);
                    o.setAirFlow(airFlow);
                    OxyTools.printParam(outputData,"Содержание О2= %.1f %%", o.calcOxyConc());
                    onCalcMore.setEnabled(true);

                }
            }
        });
        onCalcMore.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("OxygenObject", o);
                startActivity(intent);
            }
        });
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
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

