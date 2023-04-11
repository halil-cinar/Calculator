package com.example.calculator.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.calculator.IClickListener
import com.example.calculator.R
import com.example.calculator.RecyclerViewAdapter
import com.example.calculator.databinding.ActivityMainBinding

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
var TAG="app"
class MainActivity : AppCompatActivity(), IClickListener {
    var dataBinding: ActivityMainBinding?=null
    var buttonList=HashMap<Int,String>()
    var islemler= arrayListOf<String>()
    var işlemButtonList= HashMap<Int,String>()
    lateinit var adapter:RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.activity_main)
         adapter= RecyclerViewAdapter(arrayListOf(),dataBinding!!.recyclerView)
        var list= arrayListOf<String>()
        list.add("abc")
        list.add("abc")
        list.add("abc")
        list.add("abc")

        adapter.setItems(islemler)
        dataBinding!!.adapter=adapter!!;
        dataBinding?.clickListener=this

        //numberButtonList.add(R.id._0)
        buttonList.put(R.id._sıfır,"0")
        buttonList.put(R.id._bir,"1")
        buttonList.put(R.id._iki,"2")
        buttonList.put(R.id._uc,"3")
        buttonList.put(R.id._dort,"4")
        buttonList.put(R.id._bes,"5")
        buttonList.put(R.id._altı,"6")
        buttonList.put(R.id._yedi,"7")
        buttonList.put(R.id._sekiz,"8")
        buttonList.put(R.id._dokuz,"9")
        buttonList.put(R.id._artı,"+")
        buttonList.put(R.id._eksi,"-")
        buttonList.put(R.id._carpı,"*")
        buttonList.put(R.id._bolu,"/")
        buttonList.put(R.id._solParantez,"(")
        buttonList.put(R.id._sagParantez,")")

        işlemButtonList.put(R.id._artı,"+")
        işlemButtonList.put(R.id._eksi,"-")
        işlemButtonList.put(R.id._carpı,"*")
        işlemButtonList.put(R.id._bolu,"/")









    }


    override fun click(view: View) {
        try {


            if (işlemButtonList.contains(view.id)) {
                var text = dataBinding!!.textView.text.toString()

                if (text != "" && !işlemButtonList.containsValue(if (text.isNotEmpty()) text[text.length - 1].toString() else "") &&
                    (if (text.isNotEmpty()) text[text.length - 1] != '.' else false)
                ) {
                    dataBinding?.textView?.text = text + işlemButtonList[view.id].toString()
                } else {
                    dataBinding?.textView?.text = text.substring(0, text.length - 1) +
                            işlemButtonList[view.id].toString()
                }
            } else if (buttonList.contains(view.id)) {
                dataBinding?.textView?.text = dataBinding!!.textView.text.toString() +
                        buttonList[view.id].toString()
            } else if (view.id == R.id._delete) {
                dataBinding?.textView?.text = "";
            }else if(view.id==R.id._backspace){
                var işlem = dataBinding!!.textView.text.toString()
                dataBinding?.textView?.text=işlem.substring(0,işlem.length-1)
            }else if (view.id == R.id._eşittir) {
                var text = dataBinding!!.textView.text.toString()
                if (denge(text)) {
                    println(text)
                    println(infixToPostfix(dataBinding!!.textView.text.toString()))
                    var işlem = dataBinding!!.textView.text.toString()
                    var sonuc = postfixHesaplama(infixToPostfix(işlem))
                    dataBinding?.textView?.text = if (sonuc == null) "HATA" else {
                        işlem += "=";
                        işlem += sonuc.toString()
                        islemEkle(işlem);
                        sonuc.toString();
                    }


                } else {
                    dataBinding!!.textView.text = "HATA"
                }

            } else if (view.id == R.id._virgul) {
                var str = dataBinding?.textView?.text.toString()
                val i = str.lastIndexOfAny(arrayListOf("+", "-", "*", "/"))
                var str2 = str.substring((if (i < 0) 0 else i), str.length)
                if (!str2.contains('.') && str != "") {
                    dataBinding?.textView?.text = str + ".";
                }
            }
        }catch (e:Exception){
            e.printStackTrace()

            dataBinding!!.textView.text="";
        }
    }

    private fun islemEkle(islem: String) {
        islemler.add(islem)
        Log.e(TAG, "islemEkle: "+islemler[islemler.size-1], )
        adapter.setItems(islemler)
        adapter.notifyDataSetChanged()

    }

    private fun postfixHesaplama(ifade:String): Double? {
try {


    var ifade = ifade.replace("  ", " ")
    var stack = Stack<Double>()
    var islemList = ArrayList<String>()
    islemList.add("+")
    islemList.add("-")
    islemList.add("*")
    islemList.add("/")

    ifade.split(" ").forEach {
        if (it != "") {


            if (islemList.contains(it)) {
                stack.add(dortIslem(it, stack.pop(), stack.pop()))

            } else {
                stack.add(it.toDouble())
            }
        }
    }
    return stack.pop();
}catch (e:Exception){
    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
    dataBinding!!.textView.text="HATA"
}
        return null
    }

    private fun denge(islem: String): Boolean {
        var stack=Stack<Char>()
        islem.forEach {
            if(it=='('){
                stack.add(it)
            }
            else if(it==')'){
                if(stack.empty()){
                    return false
                }
               stack.pop()
            }
        }
        return stack.empty()
    }

    private fun dortIslem(islem: String, sayı1: Double, sayı2: Double):Double? {
        if(islem=="+"){
            return sayı1+sayı2;
        }else if(islem=="-"){
            return sayı2-sayı1;
        }else if(islem=="*"){
            return sayı1*sayı2;
        }else if(islem=="/"){
            return sayı2/sayı1;
        }
        return null

    }


    private fun infixToPostfix(ifade:String):String {

        var stack=Stack<String>()
        var postfix=""
        var islemList=ArrayList<String>()
        islemList.add("+")
        islemList.add("-")
        islemList.add("*")
        islemList.add("/")
        islemList.add("(")
        islemList.add(")")
        var numberList=ArrayList<String>()
for (i in 0..9){
    numberList.add(i.toString())
}
        numberList.add(".");


        var operator=ifade.split(*numberList.toTypedArray(),"")
        var ifade=ifade.split(*islemList.toTypedArray())
        var temp= arrayListOf<String>()
        operator.toTypedArray().forEach {
            if(it!=""){

                Log.e(TAG, "infixToPostfix: "+it )

                temp.add(it)
            }
        }

        operator=temp;
        temp= arrayListOf<String>()
        ifade.toTypedArray().withIndex().forEach {
            temp.add(it.value)
            try {

                temp.add(operator.get(it.index))
            }catch (e:Exception){

            }
        }
        ifade=temp;
        println()
        ifade.toTypedArray().forEach {
            print(it)
        }
        println()
        var islemöncelik=HashMap<Char,Int>()
        islemöncelik.put('+',1)
        islemöncelik.put('-',1)
        islemöncelik.put('*',2)
        islemöncelik.put('/',2)

        islemöncelik.put('(',5)
        islemöncelik.put(')',0)


        ifade.forEach {
            //println("A"+it)
            if(islemList.contains(it.toString())){
              //  println("B"+it)
                if(stack.empty()){
                    stack.push(it.toString())
                    postfix+=" "

                }
                else {
                //    println("C"+stack.peek())
                    if (it==")"){
                        while (!stack.empty()&&stack.peek()!="("){
                            postfix+=" "
                            postfix+=stack.pop()
                            postfix+=" "
                        }
                        if(stack.peek()=="("){
                            stack.pop()
                        }
                    }
                    else if(islemöncelik.get(stack.peek().toCharArray()[0])!! < islemöncelik[it.toCharArray()[0]]!!){
                        stack.add(it.toString())
                        postfix+=" ";
                    }

                    else {
                        while (!stack.empty()){
                            if(stack.peek().equals("(")){
                                break;
                            }

                            postfix+=" "
                            postfix+=stack.pop()
                            postfix+=" "



                        }
                        stack.add(it.toString())
                    }
                }
            }
            else{
                postfix+=" ";
                postfix+=it;

            }
        }
        while (!stack.empty()){
            postfix+=" "
            postfix+=stack.pop()

        }




return postfix;
    }
}