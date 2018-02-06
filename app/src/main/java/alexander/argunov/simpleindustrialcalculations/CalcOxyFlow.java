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



public class CalcOxyFlow extends AppCompatActivity {
    Oxygen o;

    //input edit text fields
    EditText inputOxyConc;
    EditText inputAirFlow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_oxy_flow);
        //input and output views
        inputOxyConc = findViewById(R.id.inputOxyConc);
        inputAirFlow = findViewById(R.id.inputAirFlow);

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

        //set default values
        String oxyConcByDefault = getString(R.string.oxy_conc_by_default);
        String airFlowByDefault = getString(R.string.air_flow_by_default);
        inputOxyConc.setText(oxyConcByDefault);
        inputAirFlow.setText(airFlowByDefault);

        //group of + - buttons listeners implementation
        incrOxyPer.setOnClickListener(new StepperInputListener(inputOxyConc, 0.1d,
                "%.1f"));
        decrOxyPer.setOnClickListener(new StepperInputListener(inputOxyConc, -0.1d,
                "%.1f"));
        incrAirFlow.setOnClickListener(new StepperInputListener(inputAirFlow, 5,
                "%.0f"));
        decrAirFlow.setOnClickListener(new StepperInputListener(inputAirFlow, -5,
                "%.0f"));

        //group of to default buttons listeners implementation
        toDefaultOxyConc.setOnClickListener(new ToDefaultListener(inputOxyConc,oxyConcByDefault));
        toDefaultAirFlow.setOnClickListener(new ToDefaultListener(inputAirFlow,airFlowByDefault));


        onCalcOxyFlow.setOnClickListener(new ButtonListener(this, Answer.class));

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

    class ButtonListener implements View.OnClickListener {
        Class<?> cls;
        Context packageContext;

        ButtonListener(Context packageContext, Class<?> cls) {
            this.cls = cls;
            this.packageContext = packageContext;
        }

        public void onClick(View view) {
            //textviews to display warning message if entered number is out of range
            TextView warningOxyConc = findViewById(R.id.warning_oxyConc);
            TextView warningAirFlow = findViewById(R.id.warning_airFlow);
            o = (Oxygen) getIntent().getSerializableExtra("OxygenObject");
            double oxyConc = OxyTools.setParam(inputOxyConc);
            double airFlow = OxyTools.setParam(inputAirFlow);
            String message = getString(R.string.warning_mes_enter_between);
            String messageAir = getString(R.string.warning_mes_enter_between_air);

            if (OxyTools.isCorrect(oxyConc, o.getOxyInAir(), o.getOxyPurity(), warningOxyConc, message)
                    && OxyTools.isCorrect(airFlow, 100, 300, warningAirFlow, messageAir)) {
                o.setOxyConc(oxyConc);
                o.setAirFlow(airFlow);
                o.calcOxyFlow();
                Intent intent = new Intent(packageContext, cls);
                intent.putExtra("oxygenObjectTwo", o);
                startActivity(intent);
            }
        }
    }
}

