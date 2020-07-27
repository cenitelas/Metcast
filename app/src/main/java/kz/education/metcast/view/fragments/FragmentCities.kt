package kz.education.metcast.view.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cities.*
import kz.education.metcast.R
import kz.education.metcast.data.api.RequestInterface
import kz.education.metcast.data.api.models.CityDetail
import kz.education.metcast.data.dao.AppDatabase
import kz.education.metcast.data.dao.entities.CityDetailCurrentEntity
import kz.education.metcast.data.dao.entities.CityDetailEntity
import kz.education.metcast.utils.CityUtils
import kz.education.metcast.view.CitiesAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FragmentCities : Fragment(), CitiesAdapter.Listener {

    var db:AppDatabase? = null

    private var cityUtils = CityUtils()

    private val TAG = FragmentCities::class.java.simpleName

    private var mCompositeDisposable: CompositeDisposable? = null

    private var mAdapter: CitiesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCompositeDisposable = CompositeDisposable()
        initRecyclerView()
        initializeDb()
        initiateListeners()
        loadJSON()

    }

    private fun initializeDb(){
        db = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java,
            "AppDatabase")
            .allowMainThreadQueries()
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertCity(cityDetail: CityDetail){
        var cityDetailEntity = db?.cityDetailDao()?.getCityDetailById(cityDetail.city.id);
        if(cityDetailEntity==null) {
            val cityDetailEntityNew =
                CityDetailEntity(cityDetail.city.id, Gson().toJson(cityDetail))
            db?.cityDetailDao()?.insert(cityDetailEntityNew);
        }else{
            cityDetailEntity.data = Gson().toJson(cityDetail)
            db?.cityDetailDao()?.update(cityDetailEntity);
        }
        cityDetailEntity = db?.cityDetailDao()?.getCityDetailById(cityDetail.city.id);
        var mCityDetailArrayList: ArrayList<CityDetail> = ArrayList()
        db?.cityDetailDao()?.getCitiesDetails()?.forEach {
            mCityDetailArrayList.add(Gson().fromJson(it.data,CityDetail::class.java));
        }
        var cityDetailCurrentEntity = db?.cityDetailCurrentDao()?.getCityDetailById(1);
        if(cityDetailCurrentEntity==null){
            db?.cityDetailCurrentDao()?.insert(CityDetailCurrentEntity(idCurrent = cityDetailEntity?.id ?: cityDetail.city.id,id=1))
        }else{
            cityDetailCurrentEntity.idCurrent=cityDetailEntity?.id ?: cityDetail.city.id;
            db?.cityDetailCurrentDao()?.update(cityDetailCurrentEntity)
        }
        val fragment = fragmentManager?.findFragmentByTag("FragmentCityInfo") as FragmentCityInfo
        fragment.onChangeCity(Gson().fromJson(cityDetailEntity?.data,CityDetail::class.java))
        mAdapter = CitiesAdapter(mCityDetailArrayList, this)
        fragment_cities_rv_list.adapter = mAdapter
    }


    private fun initRecyclerView() {

        fragment_cities_rv_list.setHasFixedSize(true)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(view!!.context)
        fragment_cities_rv_list.layoutManager = layoutManager
    }

    private fun loadJSON() {
        val cityDetailCurrentEntity = db?.cityDetailCurrentDao()?.getCityDetailById(1)
        if(cityDetailCurrentEntity!=null) {
            val requestInterface = Retrofit.Builder()
                .baseUrl(cityUtils.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

            mCompositeDisposable?.add(
                requestInterface.getCityById(cityDetailCurrentEntity.idCurrent.toString(), cityUtils.API_KEY, "ru")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError)
            )
        }else{
            Toast.makeText(view!!.context, "Добавьте город", Toast.LENGTH_SHORT).show()
        }
    }


    fun addCity(name:String){
        val requestInterface = Retrofit.Builder()
            .baseUrl(cityUtils.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RequestInterface::class.java)

        mCompositeDisposable?.add(requestInterface.getCity(name,cityUtils.API_KEY,"ru")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError))
    }

    fun initiateListeners(){
        fragment_cities_add_city.setOnClickListener {
            cityUtils.addCity(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleResponse(cityDetail: CityDetail) {
        insertCity(cityDetail)
    }

    fun updateCityInfo(){
        val cityDetailCurrentEntity = db?.cityDetailCurrentDao()?.getCityDetailById(1)
        if(cityDetailCurrentEntity!=null) {
            val requestInterface = Retrofit.Builder()
                .baseUrl(cityUtils.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

            mCompositeDisposable?.add(
                requestInterface.getCityById(
                    cityDetailCurrentEntity.idCurrent.toString(),
                    cityUtils.API_KEY,
                    "ru"
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError)
            )
        }
    }

    private fun handleError(error: Throwable) {

        Log.d(TAG, error.localizedMessage)
        if(error.localizedMessage=="HTTP 404 Not Found"){
            Toast.makeText(view!!.context, "Город не найден", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(view!!.context, "Ошибка подключения к серверу", Toast.LENGTH_SHORT).show()
        }

        var mCityDetailArrayList: ArrayList<CityDetail> = ArrayList()
        db?.cityDetailDao()?.getCitiesDetails()?.forEach {
            mCityDetailArrayList.add(Gson().fromJson(it.data,CityDetail::class.java));
        }
        mAdapter = CitiesAdapter(mCityDetailArrayList, this)
        fragment_cities_rv_list.adapter = mAdapter

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(cityDetail: CityDetail) {
        var currentEntity = db?.cityDetailCurrentDao()?.getCityDetailById(1);
        if(currentEntity!=null){
            currentEntity.idCurrent=cityDetail.city.id;
            db?.cityDetailCurrentDao()?.update(currentEntity);
        }else{
            db?.cityDetailCurrentDao()?.insert(CityDetailCurrentEntity(1,cityDetail.city.id))
        }
        val fragment = fragmentManager?.findFragmentByTag("FragmentCityInfo") as FragmentCityInfo
        fragment.onChangeCity(cityDetail)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deleteCity(cityDetail:CityDetail){
        val cityDetailEntity = db?.cityDetailDao()?.getCityDetailById(cityDetail.city.id);
        val currentEntity = db?.cityDetailCurrentDao()?.getCityDetailById(1);
        if(currentEntity?.idCurrent==cityDetailEntity?.id){
            db?.cityDetailCurrentDao()?.delete(currentEntity!!);
            val fragment = fragmentManager?.findFragmentByTag("FragmentCityInfo") as FragmentCityInfo
            fragment.onChangeCity(null)
        }
        db?.cityDetailDao()?.delete(cityDetailEntity!!)
        var mCityDetailArrayList: ArrayList<CityDetail> = ArrayList()
        db?.cityDetailDao()?.getCitiesDetails()?.forEach {
            mCityDetailArrayList.add(Gson().fromJson(it.data,CityDetail::class.java));
        }
        mAdapter = CitiesAdapter(mCityDetailArrayList, this)
        fragment_cities_rv_list.adapter = mAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }
}