package org.jetbrains.psiesmanager.processing

import com.fasterxml.jackson.core.type.TypeReference
import org.jetbrains.psiesmanager.io.DirectoryWalker
import org.jetbrains.psiesmanager.structures.EsDocument
import org.jetbrains.psiesmanager.structures.Tree
import java.io.File

class PsiLoader {
    companion object {
        fun walkPsiDirectory(psiDirectory: String, callback: (File) -> Unit) {
            DirectoryWalker(psiDirectory).run {
                if (it.isFile && it.name.endsWith(".kt.json")) {
                    callback(it)
                }
            }
        }
    }

    fun load (file: File): EsDocument {
        val treeReference = object: TypeReference<ArrayList<Tree>>() {}
        val psi = JsonFilesReader.readFile<ArrayList<Tree>>(file, treeReference)[0]
        val esDocument = EsDocument(file.absolutePath, psi)

        return esDocument
    }
}