package com.example.bookkeeping.frag_analysis;

import android.os.Bundle;

public class OutcomeFragment extends BaseAnalysisFragment{
    public static final int kind = 0;

    public OutcomeFragment() {
        super(kind);
    }

    public static OutcomeFragment newInstance(Bundle bundle) {

        OutcomeFragment fragment = new OutcomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
