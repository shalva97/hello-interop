package wasm

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

interface GenerateWasmTestRunner : Task {
    @get:InputFile
    val instantiateFile: Property<String>

    @get:InputFile
    val inputLibraryFile: RegularFileProperty

    @get:Input
    val inputLibraryName: Property<String>

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty

    @get:Internal
    val testRunnerFile: Provider<RegularFile>
}

open class DefaultGenerateWasmTestRunner @Inject constructor(
    objectFactory: ObjectFactory,
) : GenerateWasmTestRunner, DefaultTask() {
    override val instantiateFile: Property<String> = objectFactory.property()
    override val inputLibraryFile: RegularFileProperty = objectFactory.fileProperty()
    override val inputLibraryName: Property<String> = objectFactory.property()
    override val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.buildDirectory.dir("wasmTestRunner")
    )

    override val testRunnerFile: Provider<RegularFile>
        get() = outputDirectory.file("run-test.mjs")

    @TaskAction
    fun generate() {
        val libraryName = inputLibraryName.get()
        testRunnerFile.get().asFile.run {
            parentFile.mkdirs()
            writeText(
                """|
                   |import { instantiate } from '${instantiateFile.get()}';
                   |import Module from '${inputLibraryFile.get()}';
                   |var wasmSetup = new Promise(function(resolve, reject) {
                   |     Module['onRuntimeInitialized'] = _ => {
                   |         resolve(Module);
                   |    };
                   |});
                   |await wasmSetup;
                   |export default (await instantiate({ '$libraryName': Module['asm'], Module })).exports;
                   |""".trimMargin()
            )
        }
    }
}
