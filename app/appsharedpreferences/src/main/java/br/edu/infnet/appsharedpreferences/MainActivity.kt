package br.edu.infnet.appsharedpreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    private var seekBarSound: SeekBar? = null
    private var seekBarBrightness: SeekBar? = null

    private var radioGroupDiffLevel: RadioGroup? = null
    private var radioButtonEasy: RadioButton? = null
    private var radioButtonMedium: RadioButton? = null
    private var radioButtonHard: RadioButton? = null

    private var buttonSave: Button? = null

    private var edtPersonName: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarBrightness = findViewById<View>(R.id.seekBar_brightness) as SeekBar
        seekBarSound = findViewById<View>(R.id.seekBar_sound) as SeekBar
        seekBarBrightness!!.max = 100
        seekBarSound!!.max = 100

        radioGroupDiffLevel = findViewById<View>(R.id.radioGroup_diffLevel) as RadioGroup
        radioButtonEasy = findViewById<View>(R.id.radioButton_easy) as RadioButton
        radioButtonMedium = findViewById<View>(R.id.radioButton_medium) as RadioButton
        radioButtonHard = findViewById<View>(R.id.radioButton_hard) as RadioButton

        buttonSave = findViewById<View>(R.id.button_save) as Button
        buttonSave!!.setOnClickListener { view -> doSave(view) }

        edtPersonName = findViewById<View>(R.id.editTextTextPersonName) as EditText

        loadGameSetting()
    }

    private fun loadGameSetting() {
        val sharedPreferences = getSharedPreferences("gameSetting", MODE_PRIVATE)
        if (sharedPreferences != null) {
            val brightness = sharedPreferences.getInt("brightness", 90)
            val sound = sharedPreferences.getInt("sound", 95)
            val checkedRadioButtonId =
                sharedPreferences.getInt("checkedRadioButtonId", R.id.radioButton_medium)
            seekBarSound!!.progress = sound
            seekBarBrightness!!.progress = brightness
            radioGroupDiffLevel!!.check(checkedRadioButtonId)

        }else
        {
            radioGroupDiffLevel!!.check(R.id.radioButton_medium)
            Toast.makeText(this, "Use the default game setting", Toast.LENGTH_LONG).show()

        }
    }

    private fun doSave(view: View?) {
        val sharedPreferences = getSharedPreferences("gameSetting", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("brightness", seekBarBrightness!!.progress)
        editor.putInt("sound", seekBarSound!!.progress)

        val checkedRadioButtonId = radioGroupDiffLevel!!.checkedRadioButtonId
        editor.putInt("checkedRadioButtonId", checkedRadioButtonId)

        editor.apply()
        Toast.makeText(this, "Game Setting saved!", Toast.LENGTH_LONG).show()
    }
}