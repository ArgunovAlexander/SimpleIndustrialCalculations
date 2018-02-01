package alexander.argunov.simpleindustrialcalculations;

import android.view.View;
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
            textView.setVisibility(View.VISIBLE);
            textView.setText(format(Locale.US,"Введите число между %1$.1f и %2$.1f ",min,max));
            return false;
        }
        return true;
    }

    static void printParam(TextView textView, String formattedMessage, double param){
        String message=format(Locale.US,formattedMessage, param);
        textView.setText(message);
    }

    static String printParam(String formattedMessage, double param) {
        return (param != 0) ? String.format(Locale.US, formattedMessage, param) + "\n" : "";
    }

    static String oxyMessage(Oxygen o) {
        String message = "";
        message += printParam("Расход дутья %.0f тыс.м3/ч", o.getAirFlow());
        message += printParam("Расход O2 %.1f тыс.м3/ч", o.getOxyFlow());
        message += printParam("Содержание O2 в дутье (расч) %.1f %%", o.getOxyConc());
        message += printParam("Содержание O2 в дутье (ДП) %.1f %%", o.getFurnaceOxyConc());
        message += printParam("Потери дутья %.0f тыс.м3/ч", o.getAirDissipation());
        message += printParam("Содержание О2 в атмосфере %.1f %%", o.getOxyInAir());
        message += printParam("Чистота O2 %.1f %%", o.getOxyPurity());

        return message;
    }
}
