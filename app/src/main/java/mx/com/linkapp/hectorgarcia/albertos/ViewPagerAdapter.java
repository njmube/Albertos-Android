package mx.com.linkapp.hectorgarcia.albertos;

/**
 * Created by hectorgarcia on 29/06/15.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;


import mx.com.linkapp.hectorgarcia.albertos.fragments.Login;
import mx.com.linkapp.hectorgarcia.albertos.fragments.Promociones;
import mx.com.linkapp.hectorgarcia.albertos.fragments.Premios;
import mx.com.linkapp.hectorgarcia.albertos.fragments.Registro;
import mx.com.linkapp.hectorgarcia.albertos.fragments.Ubicacion;
import mx.com.linkapp.hectorgarcia.albertos.fragments.Cuenta;
import mx.com.linkapp.hectorgarcia.albertos.fragments.user_logged;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    Context context;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.context = context;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Promociones();

            case 1:
                return new Premios();

            case 2:
                return new Ubicacion();

            case 3:
                return new Login();

        }

        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {

        return Titles[position];
    }



    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
