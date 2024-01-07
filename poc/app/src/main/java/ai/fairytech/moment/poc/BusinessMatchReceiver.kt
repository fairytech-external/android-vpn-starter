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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BusinessMatchReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != "ai.fairytech.moment.action.BUSINESS_TRIGGER") return
        val businessId = intent.getStringExtra("business_id")
        Toast.makeText(context, "비즈니스 인식 (id: $businessId)", Toast.LENGTH_SHORT).show()
    }
}