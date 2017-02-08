package hu.bendaf.udacity.popularmovies.popularmoviesapp.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import hu.bendaf.udacity.popularmovies.popularmoviesapp.R;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.fragments.MovieListFragment;

public class MainAct extends AppCompatActivity {
    private static final String TAG = "MainAct";
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_FAVORITES = "favorites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return MovieListFragment.newInstance(PATH_POPULAR);
                case 1:
                    return MovieListFragment.newInstance(PATH_TOP_RATED);
                case 2:
                default:
                    return MovieListFragment.newInstance(PATH_FAVORITES);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return getString(R.string.title_tab_popular).toUpperCase();
                case 1:
                    return getString(R.string.title_tab_top_rated).toUpperCase();
                case 2:
                    return getString(R.string.title_tab_favorites).toUpperCase();
            }
            return null;
        }
    }
}
