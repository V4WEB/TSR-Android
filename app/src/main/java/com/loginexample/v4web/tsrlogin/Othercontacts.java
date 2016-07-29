package com.loginexample.v4web.tsrlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

public class Othercontacts extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othercontacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case(R.id.menu_Add_TSR):
                Toast.makeText(this, "TSR", Toast.LENGTH_LONG).show();
                AddTSRClick();
                break;
            case(R.id.menu_Profile):
                Toast.makeText(this, "Already on Profile", Toast.LENGTH_LONG).show();
                ViewProfile();
                break;
            case(R.id.menu_OtherContacts):
                Toast.makeText(this, "Already on Other Contacts", Toast.LENGTH_LONG).show();
                break;
            case(R.id.menu_Webbies):
                Toast.makeText(this, "V4Webbies", Toast.LENGTH_LONG).show();
                ViewV4();
                break;
            case(R.id.menu_Logout):
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
                logout();
                break;
        }
        return true;
    }
    public void AddTSRClick() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(Othercontacts.this, tsr.class);
        startActivity(i);
        finish();
    }
    public void ViewV4() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(Othercontacts.this, V4Webbies.class);
        startActivity(i);
        finish();
    }
    public void ViewProfile() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(Othercontacts.this, Profile.class);
        startActivity(i);
        finish();
    }
    public void logout() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(Othercontacts.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_othercontacts, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    return  v4webFragment.newInstance();
                case 2:
                    return PlaceholderFragment.newInstance(position + 1);
                case 3:
                    return v4webbiesFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "OFFICE";
                case 1:
                    return "SUPPORT";
                case 2:
                    return "GENERAL";
                case 3:
                    return "V4WEBBIES";

            }
            return null;
        }
    }
}
