package com.cqu.dialog;

import android.content.Intent;

public interface DialogListener {
	void onCancel();
	boolean onOk(Intent data);
}
