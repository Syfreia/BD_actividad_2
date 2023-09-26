package com.example.bd_actividad_2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        val bdINFO = findViewById<TextView>(R.id.Out_BD_Info)
        val alta = findViewById<Button>(R.id.btn_alta)
        val consulta_cod = findViewById<Button>(R.id.btn_consulta_cod)
        val consulta_des = findViewById<Button>(R.id.btn_consulta_descrip)
        val baja = findViewById<Button>(R.id.btn_baja_cod)
        val modificar = findViewById<Button>(R.id.btn_mod)
        val lista = findViewById<Button>(R.id.btn_lista)

        // Get references to EditText fields
        val editTextCode = findViewById<EditText>(R.id.IN_cod)
        val editTextDescription = findViewById<EditText>(R.id.In_descrip)
        val editTextPrice = findViewById<EditText>(R.id.IN_precio)

        alta.setOnClickListener {
            val code = editTextCode.text.toString()
            val description = editTextDescription.text.toString()
            val priceStr = editTextPrice.text.toString()

            val price = priceStr.toDoubleOrNull() ?: 0.0

            val data = hashMapOf(
                "code" to code,
                "description" to description,
                "price" to price
            )

            db.collection("Productos").add(data)
                .addOnSuccessListener { documentReference ->
                    bdINFO.text = "Document added with ID: ${documentReference.id}"
                }
                .addOnFailureListener { e ->
                    bdINFO.text = "Error adding document: ${e.message}"
                }
        }

        consulta_cod.setOnClickListener {
            val codeToQuery = editTextCode.text.toString()

            db.collection("Productos")
                .whereEqualTo("code", codeToQuery)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        bdINFO.text = "Code exists in the database."
                    } else {
                        bdINFO.text = "Code not found in the database."
                    }
                }
                .addOnFailureListener { e ->
                    bdINFO.text = "Error querying data: ${e.message}"
                }
        }

        consulta_des.setOnClickListener {
            val descriptionToQuery = editTextDescription.text.toString()

            db.collection("Productos")
                .whereEqualTo("description", descriptionToQuery)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        bdINFO.text = "Description exists in the database."
                    } else {
                        bdINFO.text = "Description not found in the database."
                    }
                }
                .addOnFailureListener { e ->
                    bdINFO.text = "Error querying data: ${e.message}"
                }
        }

    }
}