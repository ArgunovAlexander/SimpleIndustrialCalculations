package alexander.argunov.simpleindustrialcalculations;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import static java.lang.String.format;
import java.util.Locale;

public class CalcOxyFlow extends AppCompatActivity {
    Oxygen o;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_oxy_flow);

//group of + - buttons to perform steper input
        TextView incrOxyPer=findViewById(R.id.incrOxyPer);
        TextView decrOxyPer =  findViewById(R.id.decrOxyPer);
        TextView incrAirFlow =  findViewById(R.id.incrAirFlow);
        TextView decrAirFlow =  findViewById(R.id.decrAirFlow);

        //calculate button
        TextView onCalcOxyFlow =  findViewById(R.id.onCalcOxyFlow);

        //group of to default buttons
        TextView toDefaultOxyConc=findViewById(R.id.toDefaultOxyConc);
        TextView toDefaultAirFlow=findViewById(R.id.toDefaultAirFlow);

        //input edit text fields
        final EditText inputOxyConc =  findViewById(R.id.inputOxyConc);
        final EditText inputAirFlow =  findViewById(R.id.inputAirFlow);

        //output textview field
        final TextView outputData =  findViewById(R.id.outputData);

        //set default values
        String oxyConcByDefault="22";
        String airFlowByDefault="200";
        inputOxyConc.setText(oxyConcByDefault);
        inputAirFlow.setText(airFlowByDefault);

        //group of + - buttons listeners implementation
        incrOxyPer.setOnClickListener(new StepperInputListener(inputOxyConc, 0.1d, "%.1f"));
        decrOxyPer.setOnClickListener(new StepperInputListener(inputOxyConc, -0.1d, "%.1f"));
        incrAirFlow.setOnClickListener(new StepperInputListener(inputAirFlow, 5, "%.0f"));
        decrAirFlow.setOnClickListener(new StepperInputListener(inputAirFlow, -5, "%.0f"));

        //group of to default buttons listeners implementation
        toDefaultOxyConc.setOnClickListener(new ToDefaultListener(inputOxyConc,oxyConcByDefault));
        toDefaultAirFlow.setOnClickListener(new ToDefaultListener(inputAirFlow,airFlowByDefault));


        onCalcOxyFlow.setOnClickListener(new OnClickListener() {
            public void onClick (View view){
                //textviews to display warning message if entered number is out of range
                TextView warningOxyConc=findViewById(R.id.warning_oxyConc);
                TextView warningAirFlow=findViewById(R.id.warning_airFlow);
                warningOxyConc.setText("");
                warningAirFlow.setText("");
                System.out.println("**********************---------------");

                o=(Oxygen) getIntent().getSerializableExtra("OxygenObject");
                System.out.println("**********************0000000000000");

                double oxyInAir = o.getOxyInAir();
                double oxyPurity = o.getOxyPurity();
                double oxyConc = OxyTools.setParam(inputOxyConc);
                double airFlow = OxyTools.setParam(inputAirFlow);
                System.out.println("**********************1111111111111");
                if (OxyTools.isCorrect(oxyConc,oxyInAir,oxyPurity,warningOxyConc)&&OxyTools.isCorrect(airFlow,100,300, warningAirFlow)) {
                    System.out.println("**********************22222222222");
                    o.setOxyConc(oxyConc);
                    o.setAirFlow(airFlow);
                    OxyTools.printParam(outputData, "Расход О2 = %.1f тыс.м3/ч", o.calcOxyFlow());
                }
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

