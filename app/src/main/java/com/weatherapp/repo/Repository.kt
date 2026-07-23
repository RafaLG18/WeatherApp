package com.weatherapp.repo

import com.weatherapp.db.fb.FBCity
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.FBUser
import com.weatherapp.db.fb.toFBCity
import com.weatherapp.db.local.LocalDatabase
import com.weatherapp.db.local.toCity
import com.weatherapp.db.local.toLocalCity
import com.weatherapp.model.City
import com.weatherapp.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository (private val fbDB: FBDatabase,
                  private val localDB : LocalDatabase
) : FBDatabase.Listener {
    interface Listener {
        fun onUserLoaded(user: User)
        fun onUserSignOut()
        fun onCityAdded(city: City)
        fun onCityUpdated(city: City)
        fun onCityRemoved(city: City)
    }
    private var listener : Listener? = null
    fun setListener(listener: Listener? = null) {
        this.listener = listener
    }
    private var ioScope : CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var cityMap = emptyMap<String, City>()
    init {
        fbDB.setListener(this)
        ioScope.launch {
            localDB.getCities().collect { localCityList ->
                val cityList = localCityList.map { it.toCity() }
                val nameList = cityList.map { it.name }
                val deletedCities = cityMap.filter { it.key !in nameList}.values
                val updatedCities = cityList.filter { it.name in cityMap.keys }
                val newCities = cityList.filter { it.name !in cityMap.keys }
                newCities.forEach { listener?.onCityAdded(it) }
                updatedCities.forEach { listener?.onCityUpdated(it) }
                deletedCities.forEach { listener?.onCityRemoved(it) }
                cityMap = cityList.associateBy { it.name }
            }
        }
    }
    fun add(city: City) = fbDB.add(city.toFBCity())
    fun remove(city: City) = fbDB.remove(city.toFBCity())
    fun update(city: City) = fbDB.update(city.toFBCity())
    override fun onUserLoaded(user: FBUser)= listener?.onUserLoaded(user.toUser())?:Unit
    override fun onUserSignOut() = listener?.onUserSignOut()?:Unit
    override fun onCityAdded(city: FBCity) {
        localDB.insert(city.toCity().toLocalCity())
    }
    override fun onCityUpdated(city: FBCity) {
        localDB.update(city.toCity().toLocalCity())
    }
    override fun onCityRemoved(city: FBCity) {
        localDB.delete(city.toCity().toLocalCity())
    }
}