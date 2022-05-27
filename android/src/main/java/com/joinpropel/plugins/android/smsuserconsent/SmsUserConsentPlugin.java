package com.joinpropel.plugins.android.smsuserconsent;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

@CapacitorPlugin(name = "SmsUserConsent")
public class SmsUserConsentPlugin extends Plugin {

    private PluginCall persistedCall;
    private SmsBroadcastReceiver smsBroadcastReceiver;

    public static final int SMS_CONSENT_REQUEST = 1244;
    private static final String RECEIVED_OTP_PROPERTY = "receivedOtpMessage";

    @PluginMethod
    public void subscribeToOTP(PluginCall call) {
        rejectPreviousCall();
        unregisterReceiver();
        this.persistedCall = call;

        Task<Void> task = SmsRetriever.getClient(getActivity()).startSmsUserConsent(null);
        task.addOnSuccessListener(
            new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // successfully started an SMS Retriever for one SMS message
                    registerReceiver();
                }
            }
        );
        task.addOnFailureListener(
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    call.reject("Failed to start SMS User Consent API", null, e);
                }
            }
        );
    }

    private void rejectPreviousCall() {
        if (this.persistedCall != null) {
            this.persistedCall.reject("[TODO]: Error reason");
            this.persistedCall = null;
        }
    }

    private void unregisterReceiver() {
        if (this.smsBroadcastReceiver != null) {
            getContext().unregisterReceiver(this.smsBroadcastReceiver);
            this.smsBroadcastReceiver = null;
        }
    }

    private void registerReceiver() {
        this.smsBroadcastReceiver = new SmsBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        getContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case SMS_CONSENT_REQUEST:
                unregisterReceiver();
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = intent.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    WritableMap map = Arguments.createMap();
                    map.putString(RECEIVED_OTP_PROPERTY, message);
                    this.persistedCall.resolve(map);
                } else {
                    this.persistedCall.reject("[TODO] Error", null, new Error("Result code: " + resultCode));
                }
                this.persistedCall = null;
                break;
        }
    }
}
