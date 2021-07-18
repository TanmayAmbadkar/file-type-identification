package xyz.hisname.fireflyiii.ui.base

/*
 * Copyright (C) 2009 The Android Open Source Project
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

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf


/**
 * Base class for implementing an Activity that is used to help implement an
 * AbstractAccountAuthenticator. If the AbstractAccountAuthenticator needs to use an activity
 * to handle the request then it can have the activity extend AccountAuthenticatorActivity.
 * The AbstractAccountAuthenticator passes in the response to the intent using the following:
 * <pre>
 *      intent.putExtra({@link AccountManager#KEY_ACCOUNT_AUTHENTICATOR_RESPONSE}, response);
 * </pre>
 * The activity then sets the result that is to be handed to the response via
 * {@link #setAccountAuthenticatorResult(android.os.Bundle)}.
 * This result will be sent as the result of the request when the activity finishes. If this
 * is never set or if it is set to null then error {@link AccountManager#ERROR_CODE_CANCELED}
 * will be called on the response.
 */

//https://android.googlesource.com/platform/frameworks/base.git/+/master/core/java/android/accounts/AccountAuthenticatorActivity.java
@SuppressLint("Registered")
open class AccountAuthenticatorActivity: BaseActivity() {

    private var accountAuthenticatorResponse: AccountAuthenticatorResponse? = null
    private var resultBundle: Bundle? = null

    /**
     * Set the result that is to be sent as the result of the request that caused this
     * Activity to be launched. If result is null or this method is never called then
     * the request will be canceled.
     * @param result this is returned as the result of the AbstractAccountAuthenticator request
     */
    fun setAccountAuthenticatorResult(result: Bundle){
        resultBundle = result
    }

    /**                                                              icicle?! what year is this? 2011?
     * Retrieves the AccountAuthenticatorResponse from either the intent of the icicle, if the
     * icicle is non-zero.
     * @param savedInstanceState the save instance data of this Activity, may be null
     */
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        accountAuthenticatorResponse = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)
        if(accountAuthenticatorResponse != null){
            accountAuthenticatorResponse?.onRequestContinued()
        }
    }

    override fun finish() {
        if (accountAuthenticatorResponse != null){
            if(resultBundle != null){
                accountAuthenticatorResponse?.onResult(resultBundle)
            } else {
                accountAuthenticatorResponse?.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled")
            }
            accountAuthenticatorResponse = null
        }
        super.finish()
    }
}