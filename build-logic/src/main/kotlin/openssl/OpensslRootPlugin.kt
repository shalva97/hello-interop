package openssl

import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registering

class OpensslRootPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        check(this == rootProject)

        val downloadOpenssl by tasks.registering(Download::class) {
            src("https://github.com/whyoleg/openssl-builds/releases/download/3.0.8-build-2/openssl3-all.zip")
            onlyIfModified(true)
            dest(layout.buildDirectory.file("openssl/prebuilt.zip"))
        }

        tasks.register(PREPARE_OPENSSL_TASK_NAM, Sync::class) {
            from(downloadOpenssl.map { zipTree(it.outputFiles.single()) })
            into(layout.buildDirectory.dir("openssl/prebuilt"))
        }
    }

    companion object {
        val PREPARE_OPENSSL_TASK_NAM = "prepareOpenssl"
    }
}
