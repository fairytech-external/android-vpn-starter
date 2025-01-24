/*
 * Fairy Technologies CONFIDENTIAL
 * __________________
 *
 * Copyright (C) Fairy Technologies, Inc - All Rights Reserved
 *
 * NOTICE:  All information contained herein is, and remains the property of Fairy
 * Technologies Incorporated and its suppliers, if any. The intellectual and technical
 * concepts contained herein are proprietary to Fairy Technologies Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents, patents in
 * process, and are protected by trade secret or copyright law.
 *
 * Dissemination of this information,or reproduction or modification of this material
 * is strictly forbidden unless prior written permission is obtained from Fairy
 * Technologies Incorporated.
 *
 */

package ai.fairytech.moment.poc

import ai.fairytech.moment.constants.ActionNameConstants
import ai.fairytech.moment.constants.ExtraNameConstants
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BusinessMatchReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ActionNameConstants.TRIGGER_BROADCAST_ACTION_NAME) return
        val businessId = intent.getStringExtra(ExtraNameConstants.BUSINESS_ID_EXTRA_NAME)
        val timestamp = intent.getLongExtra(ExtraNameConstants.TIMESTAMP_MILLIS_EXTRA_NAME, 0)
        Toast.makeText(context, "Business recognized (id: $businessId, timestamp: $timestamp)", Toast.LENGTH_SHORT).show()
    }
}