package io.surepass.facescanner.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.surepass.facescanner.databinding.OnboardingitemsBinding

class OnBoardingAdapter(private val items: List<ModelOnBoardingRules>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingItemViewHolder>() {

    inner class OnBoardingItemViewHolder(adapterBinding: OnboardingitemsBinding) :
        RecyclerView.ViewHolder(adapterBinding.root) {
        val tvHeading = adapterBinding.tvHeading
        val tvRule1 = adapterBinding.rule1
        val tvRule2 = adapterBinding.rule2
        val tvRule3 = adapterBinding.rule3
        val tvRule4 = adapterBinding.rule4
        val tvRule5 = adapterBinding.rule5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingItemViewHolder {
        return OnBoardingItemViewHolder(
            OnboardingitemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OnBoardingItemViewHolder, position: Int) {
        val item = items[position]
        holder.tvHeading.text = item.heading
        holder.tvRule1.text = item.rule1
        holder.tvRule2.text = item.rule2
        holder.tvRule3.text = item.rule3
        holder.tvRule4.text = item.rule4
        holder.tvRule5.text = item.rule5
    }
}

