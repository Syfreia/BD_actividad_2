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
                    bdINFO.text = "Document added: $data"
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
                        bdINFO.text = "($codeToQuery) Code exists in the database."
                    } else {
                        bdINFO.text = "($codeToQuery) Code not found in the database."
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
                        bdINFO.text = "($descriptionToQuery) Description exists in the database."
                    } else {
                        bdINFO.text = "($descriptionToQuery) Description not found in the database."
                    }
                }
                .addOnFailureListener { e ->
                    bdINFO.text = "Error querying data: ${e.message}"
                }
        }

        baja.setOnClickListener {
            val codeToDelete = editTextCode.text.toString()

            // Delete the document with the specified code
            db.collection("Productos").whereEqualTo("code", codeToDelete)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // Delete the first matching document
                        val document = querySnapshot.documents[0]
                        db.collection("Productos").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                bdINFO.text = "Document with code ($codeToDelete) deleted."
                            }
                            .addOnFailureListener { e ->
                                bdINFO.text = "Error deleting document: ${e.message}"
                            }
                    } else {
                        bdINFO.text = "($codeToDelete) Code not found in the database."
                    }
                }
                .addOnFailureListener { e ->
                    bdINFO.text = "Error querying data: ${e.message}"
                }
        }

        modificar.setOnClickListener {
            // Get the code to identify the document to update
            val codeToUpdate = editTextCode.text.toString()

            // New data to update the document
            val newDescription = editTextDescription.text.toString()
            val newPriceStr = editTextPrice.text.toString()
            val newPrice = newPriceStr.toDoubleOrNull() ?: 0.0

            // Update the document with the new data
            db.collection("Productos").whereEqualTo("code", codeToUpdate)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // Update the first matching document
                        val document = querySnapshot.documents[0]
                        db.collection("Productos").document(document.id)
                            .update(
                                "description", newDescription,
                                "price", newPrice
                            )
                            .addOnSuccessListener {
                                bdINFO.text = "Document with code ($codeToUpdate) updated."
                            }
                            .addOnFailureListener { e ->
                                bdINFO.text = "Error updating document: ${e.message}"
                            }
                    } else {
                        bdINFO.text = "($codeToUpdate) Code not found in the database."
                    }
                }
                .addOnFailureListener { e ->
                    bdINFO.text = "Error querying data: ${e.message}"
                }
        }

        lista.setOnClickListener {
            // Retrieve and list all documents from the "Productos" collection
            db.collection("Productos")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val result = StringBuilder()
                    for (document in querySnapshot) {
                        result.append("Code: ${document["code"]}, Description: ${document["description"]}, Price: ${document["price"]}\n")
                    }
                    bdINFO.text = result.toString()
                }
                .addOnFailureListener { e ->
                    bdINFO.text = "Error retrieving data: ${e.message}"
                }
        }


    }
}