package edu.uc.group.rankine.utilities

import android.content.Context
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class JsonHandler {
    private var jsonObject: JSONObject = JSONObject()

    fun createJsonFile(name:String, context: Context): File {
        var file: File = File(context.filesDir, "rankine_data_$name.json")
        var fileWriter: FileWriter = FileWriter(file)
        fileWriter.close()
        return file


    }

    fun saveJsonFile(file : File, attribute : String){
        var fileWriter: FileWriter = FileWriter(file)
        var bufferedWriter: BufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(attribute)
        bufferedWriter.close()
    }
}