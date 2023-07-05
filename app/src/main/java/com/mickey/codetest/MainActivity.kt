package com.mickey.codetest

import BikeStationListAdapter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MainActivity : AppCompatActivity() {
    lateinit var bikeStations: Array<BikeStation>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEditText: EditText = findViewById<EditText>(R.id.searchEditText)

        val url =
            "https://tcgbusfs.blob.core.windows.net/dotapp/youbike/v2/youbike_immediate.json"  // 將此處的 URL 替換為你想要獲取 JSON 的實際 URL
        Thread {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            val response: Response = client.newCall(request).execute()
            val jsonString = response.body?.string()

            // 使用 jsonString 來處理 JSON 數據
            println(jsonString)
            val gson = Gson()
            bikeStations = gson.fromJson(jsonString, Array<BikeStation>::class.java)

            // 使用解析後的數據進行操作
            for (bikeStation in bikeStations) {
                println("站點編號: ${bikeStation.sno}")
                println("站點名稱: ${bikeStation.sna}")
                println("總停車格數: ${bikeStation.tot}")
                println("目前車輛數: ${bikeStation.sbi}")
            }

            runOnUiThread {
                setBikeStationList(bikeStations.toList())
            }
        }.start()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                var searchQuery = searchEditText.text
                val filteredStations = bikeStations.filter { station ->
                    station.sno.contains(searchQuery, ignoreCase = true) ||
                            station.sna.contains(searchQuery, ignoreCase = true) ||
                            station.sarea.contains(searchQuery, ignoreCase = true)
                }.toList()
                setBikeStationList(filteredStations)
            }

        })
    }

    private fun setBikeStationList(bikeStationList: List<BikeStation>) {
        val listView: ListView = findViewById<ListView>(R.id.bikeStationItemList)
        val adapter = BikeStationListAdapter(
            this,
            R.layout.bike_station_item_adapter,
            bikeStationList.toList()
        )

        listView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setLogo(R.drawable.ubilelogo)  // 替換主選單的標題為 logo

        val menuItem = menu.findItem(R.id.menu_item3)
        val actionView = menuItem?.actionView
        actionView?.setBackgroundColor(Color.rgb(181,204,34))  // 修改主選單項目的背景顏色
        menuItem?.title = Html.fromHtml("<font color='#000000'>站點資訊</font>")  // 修改主選單項目的文字顏色

        return super.onCreateOptionsMenu(menu)
    }
}