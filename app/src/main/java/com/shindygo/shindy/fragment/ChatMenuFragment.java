package com.shindygo.shindy.fragment;

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

import com.shindygo.shindy.BlockedEventFragment;
import com.shindygo.shindy.EventReviewFragment;
import com.shindygo.shindy.LikedEventFragment;
import com.shindygo.shindy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatMenuFragment extends Fragment implements
       MessagesFragment.OnFragmentInteractionListener {


    /*  @BindView(R.id.tabs)
      BadgedTabLayout tabs;*/
    @BindView(R.id.taab)
    TabLayout taab;
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
        taab.setupWithViewPager(mViewPager);


        return view;
    }

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
                   return MessagesFragment.newInstance(String.valueOf(position), "1");
                case 1:
                    return MessagesFragment.newInstance(String.valueOf(position), "2");
                case 2:
                    return MessagesFragment.newInstance(String.valueOf(position), "3");
                default:
                    return MessagesFragment.newInstance(String.valueOf(position), "1");
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.messages);
                case 1:
                    return getString(R.string.active);
                case 2:
                    return  getString(R.string.groups);
            }
            return null;
        }
    }


}
