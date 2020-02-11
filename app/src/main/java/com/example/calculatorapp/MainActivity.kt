package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND_STORE = "STATE_OPERAND_STORE"

class MainActivity : AppCompatActivity() {
//    private lateinit var result:EditText
//    private lateinit var newNumber:EditText;
//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation)}

    // operation and values
    private var operand1 : Double? = null;
    private  var operand2: Double = 0.0
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        result = findViewById(R.id.result)
//        newNumber = findViewById(R.id.newNumber)
////        displayOperation.text = pendingOperation;
//        // Buttons
//        val button0 = findViewById<Button>(R.id.button0)
//        val button1 = findViewById<Button>(R.id.button1)
//        val button2 = findViewById<Button>(R.id.button2)
//        val button3: Button = findViewById(R.id.button3)
//        val button4: Button = findViewById(R.id.button4)
//        val button5: Button = findViewById(R.id.button5)
//        val button6: Button = findViewById(R.id.button6)
//        val button7: Button = findViewById(R.id.button7)
//        val button8: Button = findViewById(R.id.button8)
//        val button9: Button = findViewById(R.id.button9)
//        val buttonDot: Button = findViewById(R.id.buttonDot)
//
//        //Operators Button
//        val buttonEquals = findViewById<Button>(R.id.buttonEqual)
//        val buttonPlus = findViewById<Button>(R.id.buttonPlus)
//        val buttonMinus = findViewById<Button>(R.id.buttonMinus)
//        val buttonDivide = findViewById<Button>(R.id.buttonDivide)
//        val buttonMultiply = findViewById<Button>(R.id.buttonmultiply)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            Log.d("listener" , b.text.toString())
            newNumber.append(b.text)
        }

        buttonNeg.setOnClickListener({v ->
            val value = newNumber.text.toString();
            if(value.isEmpty()){
                newNumber.setText("-")
            }else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e : NumberFormatException){
                    newNumber.setText("")
                }
            }
        })

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                opertionPerformValue(value , op)
            } catch (e: NumberFormatException){
                    newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }
        buttonEqual.setOnClickListener(opListener)
        buttonmultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
//        buttonMultiply.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
    }

    private fun opertionPerformValue(value: Double , operation : String){
        Log.d("opertionPerformValue" , operation)
        if(operand1 == null){
            operand1 = value;
        } else {
            operand2 = value
            if(pendingOperation == "="){
                pendingOperation = operation
            }

            when(pendingOperation){
                "=" -> operand1 = operand2
                "/" -> if(operand2 == 0.0){
                        operand1 = Double.NaN // handle attempt to divide by zero
                        } else {
                            operand1 = operand1!! / operand2
                        }
                "*" -> operand1 = operand1!! * operand2
                "-" -> operand1 = operand1!! - operand2
                "+" -> operand1 = operand1!! + operand2
            }

        }
        Log.d("result" , operand1.toString())
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        if(operand1 != null){
            outState.putDouble(STATE_OPERAND1 , operand1!!)
            outState.putBoolean(STATE_OPERAND_STORE , true)
        }
        outState.putString(STATE_PENDING_OPERATION , pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if(savedInstanceState.getBoolean(STATE_OPERAND_STORE , false)){
            savedInstanceState.getDouble(STATE_OPERAND1)
        }else {
            null
        }
        operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        Log.d("pendingOperation " , pendingOperation)
        operation.text = pendingOperation
    }
}
