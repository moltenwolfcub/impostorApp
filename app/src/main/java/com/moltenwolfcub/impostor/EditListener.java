package com.moltenwolfcub.impostor;

import android.view.View;
import android.widget.EditText;

public interface EditListener {
    void onEditStarted(EditText editText, View editRow, View displayRow);
}
