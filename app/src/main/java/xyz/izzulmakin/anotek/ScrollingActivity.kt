package xyz.izzulmakin.anotek

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.view.View
import android.widget.LinearLayout
import xyz.izzulmakin.anotek.util.ModelMain

class ScrollingActivity:AppCompatActivity() {
    var ll_datas: LinearLayout? = null;
    var et_key: AppCompatEditText? = null;
    var dataModel: DataModel? = null
    protected override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(object:View.OnClickListener {
            public override fun onClick(view:View) {
                createData()
            }
        })
        et_key = findViewById(R.id.et_key) as AppCompatEditText;
        val data_pool: MutableList<Map<String,Byte>>;
        ll_datas = findViewById(R.id.ll_datas) as LinearLayout;

        dataModel = DataModel(this, et_key!!)
        dataModel!!.load_all(ll_datas!!)
    }

    fun createData() {
        var data = Data(this, et_key!!, ModelMain.getdb(), null);
        data.putToLinearLayout(ll_datas!!)
    }


}
