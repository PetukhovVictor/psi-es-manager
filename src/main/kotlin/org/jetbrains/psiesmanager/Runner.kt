package org.jetbrains.psiesmanager

import com.google.gson.Gson
import java.net.InetAddress
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.elasticsearch.common.settings.Settings
import org.jetbrains.psiesmanager.processing.PsiLoader
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.common.xcontent.XContentType
import org.jetbrains.psiesmanager.helpers.TimeLogger


object Runner {
    fun run(psiDirectory: String) {
        val client = PreBuiltTransportClient(Settings.builder().put("cluster.name", "test").put("node.name", "main").build())
                .addTransportAddress(TransportAddress(InetAddress.getByName("localhost"), 9300))
        val psiLoader = PsiLoader()

        PsiLoader.walkPsiDirectory(psiDirectory) {
            val timeLogger = TimeLogger(task_name = "PSI $it RECORD")
            val esDocument = psiLoader.load(it)
            val indexRequest = IndexRequest("github_1_0", "psi")
            indexRequest.source(Gson().toJson(esDocument), XContentType.JSON)
            client.index(indexRequest).actionGet()
            timeLogger.finish()
        }

        client.close()
    }
}