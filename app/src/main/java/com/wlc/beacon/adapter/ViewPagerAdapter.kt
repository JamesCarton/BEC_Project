package com.wlc.beacon.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wlc.beacon.fragment.CampaignFragment
import com.wlc.beacon.fragment.FavouriteFragment
import com.wlc.beacon.fragment.SearchFragment
import com.wlc.beacon.fragment.SettingFragment


class ViewPagerAdapter(fragmentManager: FragmentManager) :
         FragmentPagerAdapter(fragmentManager){


    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return CampaignFragment()
            1 -> return SearchFragment()
            2 -> return FavouriteFragment()
            else -> return SettingFragment()
        }

    }

    override fun getCount(): Int {
        return 4
    }


}


