package com.example.securikey

inline fun<T> sdk29AndUp (onSdk29: () -> T) : T? {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        onSdk29()
    } else{
        return null
    }

}