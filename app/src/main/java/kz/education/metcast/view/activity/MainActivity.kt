package kz.education.metcast.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kz.education.metcast.R


import kz.education.metcast.view.fragments.FragmentCities
import kz.education.metcast.view.fragments.FragmentCityInfo

class MainActivity : AppCompatActivity() {
    lateinit var fragmentMenager : FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeFragmentManager()
        initializeDefaultFragment()
    }

    fun initializeFragmentManager(){
        fragmentMenager = supportFragmentManager
    }

    fun initializeDefaultFragment(){
        fragmentMenager.beginTransaction()
            .add(R.id.activity_main_relative_layout_fragment_cities,FragmentCities(),"FragmentCities")
            .commit()
        fragmentMenager.beginTransaction()
            .add(R.id.activity_main_relative_layout_fragment_city_info,FragmentCityInfo(),"FragmentCityInfo")
            .commit()
    }
}