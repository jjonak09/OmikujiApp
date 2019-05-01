package jp.wings.nikkeibp.omikuji

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fortune.*
import kotlinx.android.synthetic.main.omikuji.*

class OmikujiActivity : AppCompatActivity() ,SensorEventListener{

    lateinit var manager: SensorManager

    val omikujiShelf = Array<OmikujiParts>(20){OmikujiParts(R.drawable.result2,R.string.contents1)}
    var omikujiNumber = -1
    val omikujiBox = OmikujiBox()

    // すでにおみくじを振ったか
    var shaked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.omikuji)

        manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val value = pref.getBoolean("button",true)
        button.visibility = if(value)View.VISIBLE else View.INVISIBLE

        omikujiBox.omikujiView = imageView

        omikujiShelf[0].drawID = R.drawable.result1
        omikujiShelf[0].fortuneID = R.string.contents2
        omikujiShelf[1].drawID = R.drawable.result3
        omikujiShelf[1].fortuneID = R.string.contents9

        omikujiShelf[2].fortuneID = R.string.contents3
        omikujiShelf[3].fortuneID = R.string.contents4
        omikujiShelf[4].fortuneID = R.string.contents5
        omikujiShelf[5].fortuneID = R.string.contents6
    }



    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(omikujiBox.chkShake(event)){
            if(omikujiNumber < 0 && !shaked){
                omikujiBox.shake()
                shaked = true
            }
        }
//        val value = event?.values?.get(0)
//        if(value != null && 10 < value){
//            val toast = Toast.makeText(this,"加速度: ${value}",Toast.LENGTH_LONG)
//            toast.show()
//        }
    }

    override fun onPause() {
        super.onPause()
        manager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        val sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        manager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)


    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.item1){
            val intent = Intent(this,OmikujiPreferenceActivity::class.java)
            startActivity(intent)
        }
        return super.onContextItemSelected(item)
    }

    fun onButtonClick(v:View){
        if(!shaked){
            omikujiBox.shake()
            shaked = true
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            if(omikujiNumber < 0 && omikujiBox.finish){
                drawResult()
            }
        }
        return super.onTouchEvent(event)
    }

    fun drawResult(){
        omikujiNumber = omikujiBox.num
        val op = omikujiShelf[omikujiNumber]
        setContentView(R.layout.fortune)
        imageView2.setImageResource(op.drawID)
        textView3.setText(op.fortuneID)
    }

}
