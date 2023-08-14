package com.example.bookkeeping.frag_analysis;

import android.os.Bundle;

public class IncomeFragment extends BaseAnalysisFragment{
    public static final int kind = 1;

    public IncomeFragment() {
        super(kind);
    }

    public static IncomeFragment newInstance(Bundle bundle) {

        IncomeFragment fragment = new IncomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
