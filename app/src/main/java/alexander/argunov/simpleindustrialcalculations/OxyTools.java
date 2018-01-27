package alexander.argunov.simpleindustrialcalculations;

/**
 * Created by user on 26.01.2018.
 */

import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import static java.lang.String.format;

public class OxyTools {
    static double setParam(EditText editText) {
        return isEmpty(editText) ? 0 :Double.valueOf(editText.getText().toString()) ;
    }

    static boolean isEmpty(EditText editText) {
        return  (editText.getText().toString().trim().length() <= 0);
    }

    static boolean isCorrect(double value, double min, double max, TextView textView) {
        if (value<min||value>max) {
            textView.setText(format(Locale.US,"Введите число между %1$.1f и %2$.1f ",min,max));
            return false;
        }
        return true;
    }

    static void printParam(TextView textView, String formattedMessage, double param){
        String message=format(Locale.US,formattedMessage, param);
        textView.setText(message);
    }
}
