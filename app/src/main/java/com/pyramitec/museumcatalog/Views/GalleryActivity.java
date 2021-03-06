package com.pyramitec.museumcatalog.Views;

import android.content.Intent;
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

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.pyramitec.museumcatalog.Controllers.MuseumController;
import com.pyramitec.museumcatalog.Models.Masterpiece;
import com.pyramitec.museumcatalog.Models.Museum;
import com.pyramitec.museumcatalog.R;

import io.realm.RealmList;

public class GalleryActivity extends AppCompatActivity {

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
    private Museum mMuseum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        final long museumId = intent.getLongExtra("museumId", 0);
        MuseumController museumController = new MuseumController();
        mMuseum = museumController.getMuseumById(museumId);

        Toast.makeText(getApplicationContext(), "id" + String.valueOf(museumId), Toast.LENGTH_LONG).show();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getApplicationContext(), "log", Toast.LENGTH_LONG).show();
                toolbar.setTitle(mMuseum.getGallery().get(position).getName());
                //toolbar.setSelectedNavigationItem(position);
            }
        });

        //mViewPager.setCurrentItem(1, true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                intent.putExtra("museumId", mMuseum.getIdMuseum());
                intent.putExtra("masterpieceId", mViewPager.getCurrentItem());
                startActivity(intent);
            }
        });

        ImageButton videoButton = (ImageButton) findViewById(R.id.videoButton);
        final ImageButton soundButton = (ImageButton) findViewById(R.id.audioButton);

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final VideoView videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
                //soundButton.setVisibility(View.GONE);
                //videoPlayer.bringToFront();
                if(videoPlayer.getVisibility()==View.VISIBLE)
                {
                    videoPlayer.setVisibility(View.GONE);
                }
                else if(videoPlayer.getVisibility()==View.GONE)
                {
                    videoPlayer.setVisibility(View.VISIBLE);
                }
            }
        });

        soundButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final VideoView videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
                //soundButton.setVisibility(View.GONE);
                //videoPlayer.bringToFront();
                if(videoPlayer.getVisibility()==View.VISIBLE)
                {
                    videoPlayer.setVisibility(View.GONE);
                }
                else if(videoPlayer.getVisibility()==View.GONE)
                {
                    videoPlayer.setVisibility(View.VISIBLE);
                }
            }
        });

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        private static RealmList<Masterpiece> mGallery;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, RealmList<Masterpiece> gallery) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            mGallery = gallery;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ImageView imageShow = (ImageView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER))); //
            //textView.setText(mGallery.get(getArguments().getInt(ARG_SECTION_NUMBER)).getDescription());
            //imageShow.setImageResource(R.mipmap.ic_launcher);
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
            return PlaceholderFragment.newInstance(position + 1, mMuseum.getGallery()); //TODO: Deve ser position + 1, está com bug!
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mMuseum.getGallery().size();
        }
    }
}
