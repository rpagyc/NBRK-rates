package com.nbrk.rates;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rpagyc
 * Date: 17.12.12
 * Time: 3:12
 * To change this template use File | Settings | File Templates.
 */
public class Settings extends PreferenceActivity {
    protected Method mLoadHeaders = null;
    protected Method mHasHeaders = null;

    /**
     * Checks to see if using new v11+ way of handling PrefsFragment.
     * @return Returns false pre-v11, else checks to see if using headers.
     */
    public boolean isNewV11Prefs() {
        if (mLoadHeaders!=null && mHasHeaders!=null) {
            try {
                return (Boolean)mHasHeaders.invoke(this);
            }
            catch (IllegalArgumentException e) {}
            catch (IllegalAccessException e) {}
            catch (InvocationTargetException e) {}
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // onBuildHeaders() will be called during super.onCreate()
        try {
            mLoadHeaders = getClass().getMethod("loadHeadersFromResource", int.class, List.class);
            mHasHeaders = getClass().getMethod("hasHeaders");
        }
        catch (NoSuchMethodException e) {}
        super.onCreate(savedInstanceState);
        if (!isNewV11Prefs()) {
            addPreferencesFromResource(R.xml.app_prefs_cat1);
        }

    }

/*    @Override
    public void onBuildHeaders(List<Header> aTarget) {
        try {
            mLoadHeaders.invoke(this, new Object[]{R.xml.pref_headers, aTarget});
        }
        catch (IllegalArgumentException e) {}
        catch (IllegalAccessException e) {}
        catch (InvocationTargetException e) {}
    }*/

    static public class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            Context anAct = getActivity().getApplicationContext();
//            int thePrefRes = anAct.getResources().getIdentifier(getArguments().getString("pref-resource"),"xml",anAct.getPackageName());
            addPreferencesFromResource(R.xml.app_prefs_cat1);
        }
    }

}
