package com.example.bookkeeping.frag_record;

import com.example.bookkeeping.UniteApp;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.entity.Type;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OutcomeFragment extends BaseRecordFragment{
    public static final int kind = 0;

    public OutcomeFragment() {
        super(kind);
    }
}
