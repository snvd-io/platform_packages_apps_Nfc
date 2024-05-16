
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.nfc.utils;

import static android.content.Context.RECEIVER_EXPORTED;

import android.content.Context;
import android.content.IntentFilter;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.google.android.mobly.snippet.Snippet;
import com.google.android.mobly.snippet.event.SnippetEvent;

import com.google.android.mobly.snippet.rpc.AsyncRpc;
import com.google.android.mobly.snippet.rpc.Rpc;
import android.util.Log;
import android.os.RemoteException;

public abstract class NfcSnippet implements Snippet {
    protected static final String TAG = "NfcSnippet";
    protected final Context mContext = InstrumentationRegistry.getInstrumentation().getContext();
    private final UiDevice mDevice =
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    /** Turns device screen off */
    @Rpc(description = "Turns device screen off")
    public void turnScreenOff() {
        try {
            mDevice.sleep();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    /** Turns device screen on */
    @Rpc(description = "Turns device screen on")
    public void turnScreenOn() {
        try {
            mDevice.wakeUp();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    /** Press device menu button to return device to home screen between tests. */
    @Rpc(description = "Press menu button")
    public void pressMenu() {
        mDevice.pressMenu();
    }

    @Rpc(description = "Log info level message to device logcat")
    public void logInfo(String message) {
        Log.i(TAG, message);
    }

    /** Creates a SnippetBroadcastReceiver that listens for when the specified action is received */
    protected void registerSnippetBroadcastReceiver(
            String callbackId, String eventName, String action) {
        IntentFilter filter = new IntentFilter(action);
        mContext.registerReceiver(
                new SnippetBroadcastReceiver(
                        mContext, new SnippetEvent(callbackId, eventName), action),
                filter,
                Context.RECEIVER_EXPORTED);
    }
}