package com.example.home.moviematters;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String movieID;
    @Bind(R.id.titleTextView)
    public AutofitTextView mTitleTextView;
    @Bind(R.id.yearOfReleaseTextView)
    public TextView mReleaseYearTextView;
    @Bind(R.id.durationTextView)
    public TextView mDurationTextView;
    @Bind(R.id.ratingsTextView)
    public TextView mRatingsTextView;
    @Bind(R.id.expand_text_view)
    public ExpandableTextView mOverviewTextView;
    @Bind(R.id.posterImageView)
    public ImageView mPosterImageTextView;
    @Bind(R.id.containerForReviews)
    public LinearLayout mLinearLayout;
    @Bind(R.id.containerForTrailers)
    public LinearLayout tLinearLayout;


    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);



        return view;
    }
}
