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
import ai.fairytech.moment.notification.ActivityMatchType
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast

class BusinessMatchReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ActionNameConstants.TRIGGER_BROADCAST_ACTION_NAME) return
        val businessId = intent.getStringExtra(ExtraNameConstants.BUSINESS_ID_EXTRA_NAME)
        val matchType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(ExtraNameConstants.ACTIVITY_MATCH_TYPE_EXTRA_NAME, ActivityMatchType::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getSerializableExtra(ExtraNameConstants.ACTIVITY_MATCH_TYPE_EXTRA_NAME) as ActivityMatchType
        }
        val timestamp = intent.getLongExtra(ExtraNameConstants.TIMESTAMP_MILLIS_EXTRA_NAME, 0)
        Toast.makeText(context, "비즈니스 인식 (id: $businessId, type: $matchType, timestamp: $timestamp)", Toast.LENGTH_SHORT).show()
    }
}