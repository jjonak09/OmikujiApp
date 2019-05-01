package jp.wings.nikkeibp.omikuji

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class OmikujiPreferenceActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(android.R.id.content,OmikujiPreferenceFragment())
        fragmentTransaction.commit()
    }
}