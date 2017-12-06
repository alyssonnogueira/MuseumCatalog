package com.pyramitec.museumcatalog.Views.Museums;

import android.view.View;

/**
 * Created by alyss on 05/12/2017.
 */

public interface RecyclerViewOnClickListenerHack {
    void onClickListener(View view, int position);

    void onLongPressClickListener(View view, int position);
}