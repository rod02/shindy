package com.shindygo.shindy;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rahimlis.badgedtablayout.BadgedTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventFeedbackActivity extends Fragment implements
        LikedEventFragment.OnFragmentInteractionListener, BlockedEventFragment.OnFragmentInteractionListener, EventReviewFragment.OnFragmentInteractionListener {


    /*  @BindView(R.id.tabs)
      BadgedTabLayout tabs;*/
    @BindView(R.id.tabs)
    BadgedTabLayout tabs;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.back)
    ImageView back;

    private SectionsPagerAdapter mSectionsPagerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_event_feedback, container, false);

        ButterKnife.bind(this,view);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabs.setupWithViewPager(mViewPager);

        tabs.setIcon(0, R.drawable.event_mgmt);
        tabs.setIcon(1, R.drawable.heart_border);
        tabs.setIcon(2, R.drawable.stop);
        tabs.setIcon(3, R.drawable.star);

        return view;
    }

 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_feedback);
        ButterKnife.bind(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.

        mViewPager.setAdapter(mSectionsPagerAdapter);
        taab.setupWithViewPager(mViewPager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // tabs.setBadgeText(2, "13");


    }
*/

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

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
                    return CreatedEventsFragment.newInstance();
                case 1:
                   return LikedEventFragment.newInstance(String.valueOf(position), "1");
                case 2:
                    return BlockedEventFragment.newInstance(String.valueOf(position), "2");
                case 3:
                    return EventReviewFragment.newInstance(String.valueOf(position), "3");
                default:
                    return LikedEventFragment.newInstance(String.valueOf(position), "1");
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

      /*  @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.likedEvent);
                case 0:
                    return getString(R.string.likedEvent);
                case 1:
                    return getString(R.string.blockedEv);
                case 2:
                    return getString(R.string.eventRev);
            }
            return null;
        }*/
    }


}
