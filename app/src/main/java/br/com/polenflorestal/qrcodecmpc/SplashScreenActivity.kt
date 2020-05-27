package br.com.polenflorestal.qrcodecmpc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.polenflorestal.qrcodecmpc.Constants.*


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var versionName : String
    private var currentVersionCode : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferences = getSharedPreferences(SP_NOME, Context.MODE_PRIVATE)

        versionName = packageManager.getPackageInfo(packageName, 0).versionName
        currentVersionCode = BuildConfig.VERSION_CODE

        Log.i("SPLASH_SCREEN", "CurrentVersionName: $versionName")
        Log.i("SPLASH_SCREEN", "CurrentVersionCode: $currentVersionCode")

        //findViewById<TextView>(R.id.splash_version_name).text = versionName

        checkFirstRun()

        DataBaseUtil.abrir(this)
        val c : Cursor? = DataBaseUtil.buscar("Arvore", arrayOf<String>("codigo", "local"), "local = 'Viçosa/MG'", "")

        while (c?.moveToNext()!!){
            val cod : String = c.getString(0)
            val emp : String = c.getString(1)

            Log.i("BANCO_DADOS", "teste busca: $cod $emp");
        }

        // adiciona listener nos comentarios
        //listenerComentarios?.remove()
        DataBaseOnlineUtil.getInstance().getCollectionReference("Empresa/$EMPRESA_NOME/Comentario").addSnapshotListener { value, e ->
            if( e != null ){
                Log.w("MY_FIREBASE", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (value != null) {
                for( doc in value ){
                    //var c : Comentario = doc.toObject(Comentario::class.java)
                    //Log.i("MY_FIREBASE", "comentario: ${doc.toObject(Comentario::class.java).texto}")
                }
            }
            Log.i("MY_FIREBASE_SPLASH", "listener recebido!")
        }

        Handler().postDelayed( {fechaSplash()}, 500)
    }

    private fun fechaSplash() {
        val it = Intent(this, MainActivity::class.java)
        startActivity(it)
        finish()
    }

    private fun checkFirstRun() {
        //val currentVersionCode : Int = BuildConfig.VERSION_CODE
        val savedVersionCode : Int = sharedPreferences.getInt(SP_KEY_VERSION_CODE, DEFAULT_INT_VALUE)

        when {
            currentVersionCode == savedVersionCode -> {
                // this is just a normal run
                Log.i("SPLASH_SCREEN", "NORMAL RUN")
                return
            }
            savedVersionCode == DEFAULT_INT_VALUE -> {
                // this is a new install (or the user cleared the preferences)
                // cria o BD, ...

                Log.i("SPLASH_SCREEN", "NEW INSTALL")

                DataBaseUtil.abrir(this)
                DataBaseUtil.criaDB()

                val editor = sharedPreferences.edit()
                editor.putInt(SP_KEY_VERSION_CODE, currentVersionCode)
                editor.apply()

                return
            }
            currentVersionCode > savedVersionCode -> {
                // this is an upgrade
                // atualiza o BD, ...

                Log.i("SPLASH_SCREEN", "UPDATE RUN")

                DataBaseUtil.abrir(this)
                DataBaseUtil.criaDB()

                val editor = sharedPreferences.edit()
                editor.putInt(SP_KEY_VERSION_CODE, currentVersionCode)
                editor.apply()

                return
            }
        }
    }

}
