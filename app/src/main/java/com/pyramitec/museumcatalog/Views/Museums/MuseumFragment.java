package com.pyramitec.museumcatalog.Views.Museums;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pyramitec.museumcatalog.Controllers.MuseumController;
import com.pyramitec.museumcatalog.Models.Museum;
import com.pyramitec.museumcatalog.R;
import com.pyramitec.museumcatalog.Views.MainActivity;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */
//OnListFragmentInteractionListener
public class MuseumFragment extends Fragment implements RecyclerViewOnClickListenerHack{
    private final String LOG_TAG = MuseumFragment.class.getSimpleName();
    List<Museum> mMuseums;
    private RecyclerView mRecyclerView;

    public MuseumFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        MuseumController museumController = new MuseumController();
        mMuseums = museumController.getMuseums();

        Log.d("tam", String.valueOf(mMuseums.size()));

        for (int i = 0; i < mMuseums.size(); i++){
            Log.d(LOG_TAG, "SAHSUIADHKSAHDKASHDKASJHSDAK");
        }
        View rootView = inflater.inflate(R.layout.fragment_museum, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.rv_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Context context = listView.getContext();
            Activity activity = (Activity) context;
            Intent intent = new Intent(context, MainActivity.class);
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                intent.putExtra(MainActivity.EXTRA_NAME, mMuseums.get(position).getIdMuseum());
                                                activity.startActivity(intent);
                                            }
                                        });

        //mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        //mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        //LinearLayoutManager  llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //mRecyclerView.setLayoutManager(llm);

        MuseumAdapter museumAdapter = new MuseumAdapter(this.getActivity(), mMuseums);

        listView.setAdapter(museumAdapter);


        // Retornamos tudo
        return rootView;
    }

    @Override
    public void onClickListener(View view, int position) {
        Context mainContext = ((MuseumActivity) getContext());
        Intent intent = new Intent(mainContext, MainActivity.class);
        intent.putExtra("idActivity", position); //mMuseums.get(position)());
        //intent.putExtra(HistoricActivity.VIDEO_ID_PARAMETER,mList.get(position).getId());
        startActivity(intent);
    }

    //não usado
    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(), "Item " + position + " foi clicado e será excluído", Toast.LENGTH_SHORT).show();
    }

    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        public RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv) );
                    }

                    return(true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
