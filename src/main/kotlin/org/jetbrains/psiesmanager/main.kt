package org.jetbrains.psiesmanager

import com.xenomachina.argparser.ArgParser

fun main(args : Array<String>) {
    val parser = ArgParser(args)
    val psiDirectory by parser.storing("-d", "--psi_directory", help="path to folder with PSI")

    Runner.run(psiDirectory)
}