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

public class DetailedSolution extends AppCompatActivity {
    Oxygen o;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_solution);

        //group of + - buttons to perform steper input
        TextView incrOxyPer=findViewById(R.id.incrOxyPer);
        TextView decrOxyPer =  findViewById(R.id.decrOxyPer);

        //calculate button
        TextView onCalcDissipation =  findViewById(R.id.onCalcDissipation);

        //group of to default buttons
        TextView toDefaultOxyConc=findViewById(R.id.toDefaultOxyConc);

        //input edit text fields
        final EditText inputFurnaceOxyConc =  findViewById(R.id.inputFurnaceOxyConc);

        //output textview field
        final TextView outputData =  findViewById(R.id.outputData);

        //getting doubles from the intent
        o=(Oxygen) getIntent().getSerializableExtra("OxygenObject");
        final double oxyPurity=o.getOxyPurity();
        final double oxyConc=o.getOxyConc();
        final double oxyInAir=o.getOxyInAir();

        //set edit text default value
        inputFurnaceOxyConc.setText(format(Locale.US,"%.1f",oxyConc));

        //group of + - buttons listeners implementation
        incrOxyPer.setOnClickListener(new StepperInputListener(inputFurnaceOxyConc, 0.1d, "%.1f"));
        decrOxyPer.setOnClickListener(new StepperInputListener(inputFurnaceOxyConc, -0.1d, "%.1f"));

        //group of to default buttons listeners implementation
        toDefaultOxyConc.setOnClickListener(new ToDefaultListener(inputFurnaceOxyConc,format(Locale.US,"%.1f",oxyConc)));

        //find air dissipation method implementation
        onCalcDissipation.setOnClickListener(new OnClickListener() {
            public void onClick (View view){
                //textviews to display warning message if entered number is out of range
                TextView warningOxyConc=findViewById(R.id.warning_oxyConc);
                warningOxyConc.setText("");
                double furnaceOxyConc=OxyTools.setParam(inputFurnaceOxyConc);
                if (OxyTools.isCorrect(furnaceOxyConc,oxyInAir,oxyConc,warningOxyConc)) {
                    o.setFurnaceOxyConc(furnaceOxyConc);
                    if (o.calcAirDissipation()+o.getAirFlow()<300){
                        OxyTools.printParam(outputData, "Потери дутья  %.0f тыс.м3/ч", o.calcAirDissipation());
                    } else {
                        warningOxyConc.setText("Низкое содержание кислорода в дутье доменной печи. Данные неверны");
                    }
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
            System.out.println("*****************************"+value);
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

