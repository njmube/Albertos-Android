package mx.com.linkapp.hectorgarcia.albertos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by hectorgarcia on 06/07/15.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText fechaNacimientoText;

    public DateDialog(){
        //fechaNacimientoText = (EditText) view;
    }

    public void setView(View view){
        fechaNacimientoText = (EditText) view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear+1;
        String month;
        String day;

        if(monthOfYear < 10){
            month = "0"+String.valueOf(monthOfYear);
        }else{
            month = String.valueOf(monthOfYear);
        }

        if(dayOfMonth < 10){
            day = "0"+String.valueOf(dayOfMonth);
        }else{
            day = String.valueOf(dayOfMonth);
        }

        String date = year+"-"+month+"-"+day;
        fechaNacimientoText.setText(date);
    }
}
