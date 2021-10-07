package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var getDataOfJSON: Advices
    var ID: Int? = null
    lateinit var button: Button
    lateinit var textView: TextView
    private var advice: String? ="In Progress"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button  = findViewById(R.id.btPress)
        textView = findViewById(R.id.tvAdvice)
        //here written the time consuming code, such retrieve from the dataBase
        button.setOnClickListener(){
        CoroutineScope(IO).launch {
            getData()
            withContext(Main){//this written to enable us to access VIEWS
                    textView.setText(advice)
                }
            }
        }
    }

    /*
    only called inside the coroutine scope
    it enable us to control the stop/resume process
     */
    private suspend fun getData(){
        callJSON()
    }
    fun callJSON(){
        //------------------------------------------------------------------------------------------ prepare for API
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java) //required
        val call: Call<Advices?>? = apiInterface!!.doGetListResources() //return targeted object class details

        //------------------------------------------------------------------------------------------ API handler - start
        call?.enqueue(object : Callback<Advices?> {
            override fun onResponse(
                call: Call<Advices?>?, // set the targeted object
                response: Response<Advices?> // set the targeted object
            ) {
                //get the data from JSON object here
                getDataOfJSON = response.body()!!
                advice = getDataOfJSON?.slip?.advice
                ID = getDataOfJSON.slip!!.id!!
            }
            override fun onFailure(call: Call<Advices?>, t: Throwable?) { //required to check if there is failure
                Log.d("MainActivity", "what in hell-----------------------------------")
                call.cancel()
            }
        })
    }
}