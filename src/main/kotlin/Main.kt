package org.example
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException
import java.io.File
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*

suspend fun main() {
    println("Application running on ${Thread.currentThread().name}")
    val port = 9999
    val targetPath = "downloads"
    val server = ServerSocket(port)
    println("Server listening on port $port")

    downloadFile(port, targetPath, server)
}

suspend fun downloadFile(port: Int, targetPath: String, server: ServerSocket){
    coroutineScope {
        launch(Dispatchers.IO) {
            try {
                while (true){
                    val client = server.accept()
                    println("Request running on ${Thread.currentThread().name}")
                    println("Client connected: ${client.inetAddress.hostAddress}")

                    val inputStream = DataInputStream(client.getInputStream())
                    val fileName = inputStream.readUTF()
                    val byteContent = inputStream.readBytes()
//            val targetDirectory = File(targetPath)
//        println(targetDirectory.exists())

                    val outputFile = File(targetPath,fileName)
//                    println(byteContent.contentToString())
                    outputFile.writeBytes(byteContent)

                    inputStream.close()
                    println("Client ${client.inetAddress.hostAddress} disconnected")
                    client.close()
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

    }
}