import openssl.OpensslExtension
import openssl.OpensslRootPlugin

rootProject.apply<OpensslRootPlugin>()

extensions.create("openssl", OpensslExtension::class.java, rootProject)
