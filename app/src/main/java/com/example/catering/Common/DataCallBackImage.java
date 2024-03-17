package com.example.catering.Common;

import com.google.firebase.database.DatabaseError;

public interface DataCallBackImage<T>{

    void onSuccess(T data);

    void onError(String errorMessage);
}
