package com.innovatech.netbeltktz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DropActivity : AppCompatActivity() {
    private lateinit var estadoRef: DatabaseReference
    private lateinit var estadoListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_drop)

        Log.d("DropActivity", "Activity created")

        // Inicializar referencia de Firebase
        estadoRef = FirebaseDatabase.getInstance().getReference("estado")

        // Listener para el cambio de estado
        estadoListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val estado = snapshot.getValue(Boolean::class.java)
                Log.d("DropActivity", "Estado value: $estado")
                if (estado != null && !estado) {
                    Log.d("DropActivity", "Estado is false, starting OkActivity")
                    val intent = Intent(this@DropActivity, OkActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Log.d("DropActivity", "Estado is true or null")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DropActivity", "Error: ${error.message}")
            }
        }

        // Agregar el listener a la referencia para escuchar cambios en tiempo real
        estadoRef.addValueEventListener(estadoListener)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DropActivity", "Activity destroyed, removing listener")
        estadoRef.removeEventListener(estadoListener)
    }
}