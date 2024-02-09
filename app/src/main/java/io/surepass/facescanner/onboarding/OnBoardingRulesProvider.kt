package io.surepass.facescanner.onboarding

import android.content.Context
import io.surepass.facescanner.R

class OnBoardingRulesProvider(private val context: Context) {
    fun getOnBoardingRules(): List<ModelOnBoardingRules> {
        return listOf(
            ModelOnBoardingRules(
                context.getString(R.string.rule_requirement_text),
                context.getString(R.string.rule_text_1),
                context.getString(R.string.rule_text_2),
                context.getString(R.string.rule_text_3),
                context.getString(R.string.rule_text_4),
                context.getString(R.string.rule_text_5),
            ),
            ModelOnBoardingRules(
                context.getString(R.string.rule_requirement_text),
                context.getString(R.string.rule_text_6),
                context.getString(R.string.rule_text_7),
                context.getString(R.string.rule_text_8),
                context.getString(R.string.rule_text_9),
                context.getString(R.string.rule_text_10),
            )
        )
    }
}
