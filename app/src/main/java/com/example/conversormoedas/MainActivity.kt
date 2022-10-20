package com.example.conversormoedas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txt_result)
        val buttonConverter = findViewById<Button>(R.id.botao_converter)

        buttonConverter.setOnClickListener{
            converter()
        }

    }
    private fun converter(){
        val selectedCurrency = findViewById<RadioGroup>(R.id.radio_group)
        val checked = selectedCurrency.checkedRadioButtonId

        // when = mesma lógica do if...else
        val currency = when(checked){
            R.id.radio_dolar -> "USD"
            R.id.radio_euro -> "EUR"
            else -> "CLP"
        }

        val editField = findViewById<EditText>(R.id.edit_filed)
        val value = editField.text.toString()
        if(value.isEmpty())
            return


        Thread{
            //aqui acontece em paralelo
            val url = URL("https://free.currconv.com/api/v7/convert?q=${currency}_BRL&compact=ultra&apiKey=65c11d347037d21506b2")
            val conn = url.openConnection() as HttpsURLConnection
            try{
                val data = conn.inputStream.bufferedReader().readText()
                val obj = JSONObject(data)


                //diz para o sistema que deve atualizar nesta aplicação
                runOnUiThread{
                    val res = obj.getDouble("${currency}_BRL")
                    result.text = "R$${"%.2f".format(value.toDouble() * res)}"
                    result.visibility = View.VISIBLE
                }

            }
           /*catch (ex){

           } */
           finally {
                conn.disconnect()
           }

        }.start()

    }
}