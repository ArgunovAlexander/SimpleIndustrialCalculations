package alexander.argunov.simpleindustrialcalculations;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

class OxyTools {

    static double setParam(EditText editText) {
        return isEmpty(editText) ? 0 :Double.valueOf(editText.getText().toString()) ;
    }

    private static boolean isEmpty(EditText editText) {
        return  (editText.getText().toString().trim().length() <= 0);
    }

    static boolean isCorrect(double value, double min, double max, TextView textView, String warningMessage) {
        textView.setVisibility(View.GONE);
        if (value<min||value>max) {
            textView.setTextColor(Color.parseColor("#F44336"));
            warningMessage = String.format(warningMessage, min, max);
            textView.setText(warningMessage);
            textView.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private static String printParam(String formattedMessage, double param) {
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
