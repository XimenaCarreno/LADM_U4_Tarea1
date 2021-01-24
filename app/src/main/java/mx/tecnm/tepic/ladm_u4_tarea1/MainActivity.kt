package mx.tecnm.tepic.ladm_u4_tarea1

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CallLog
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var siLlamadas = 1
    var siCalendario = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            listarLlamadas()
        }

        button2.setOnClickListener {
            listarCalendarios()
        }
    }

    private fun listarCalendarios() {
        limpiar()
        textView.setText("Calendarios")

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALENDAR),siCalendario)
        }
        else{

            var calendarios = ArrayList<String>()
            var proyeccion = arrayOf(CalendarContract.Calendars.NAME)

            try{

                var cursorCalendario = contentResolver.query(
                    CalendarContract.Calendars.CONTENT_URI, proyeccion, null,null,null)

                if(cursorCalendario!!.moveToFirst()){
                    do{
                        calendarios.add(cursorCalendario.getString(0))

                    }while(cursorCalendario.moveToNext())
                }
                else{
                    calendarios.add("No hay calendarios")
                }
                listaResultado.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,calendarios)
                cursorCalendario.close()

            }catch(e:Exception){
                Toast.makeText(this,"Hubo un error"+e.message,Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun listarLlamadas() {
        limpiar()
        textView.setText("Llamadas Perdidas")

        if(ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siLlamadas)
        }
        else{
            var llamadas = ArrayList<String>()

            try{
                var cursorLlamadas = contentResolver.query(
                    Uri.parse("content://call_log/calls"),null,"TYPE = 3",null,null)

                var concatenacion = ""

                if(cursorLlamadas!!.moveToFirst())
                {
                    do{

                        var contactoLlamada = cursorLlamadas.getString(
                            cursorLlamadas.getColumnIndex(CallLog.Calls.CACHED_NAME))

                        var telefonoLlamada = cursorLlamadas.getString(
                            cursorLlamadas.getColumnIndex(CallLog.Calls.NUMBER))

                        concatenacion = "Llamada Perdida:\nContacto: "+contactoLlamada+"\nTeléfono: "+telefonoLlamada
                        llamadas.add(concatenacion)

                    }while(cursorLlamadas.moveToNext())
                }
                else{
                    llamadas.add("No hay llamadas perdidas")
                }

                listaResultado.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,llamadas)
                cursorLlamadas.close()
            }
            catch(e:Exception){
                Toast.makeText(this,"Hubo un error"+e.message,Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun limpiar(){
        var registros = ArrayList<String>()
        registros.add("")
        listaResultado.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,registros)
    }
}