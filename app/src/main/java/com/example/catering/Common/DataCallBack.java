package com.example.catering.Common;

import com.google.firebase.database.DatabaseError;

public interface DataCallBack<T> {

    void onSuccess(T data);

    void onError(DatabaseError error);
}
