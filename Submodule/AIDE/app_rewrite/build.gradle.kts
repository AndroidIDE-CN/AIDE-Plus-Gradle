import java.util.Locale

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("aide-plus.project")
}


val allVariants = mutableListOf<String>()
android {

    buildFeatures {
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }

    libraryVariants.all {
        val variant = this
        val variantName = variant.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        allVariants.add(variantName)
        // 注册生成JAR的任务
        val createJarTask=tasks.register<Jar>("create${variantName}Jar") {
            println(variantName)
            val outputDir = rootDir.resolve("Submodule/AIDE/AIDE-Plus/app_rewrite/AS")
            println(outputDir)
            //delete(delete())
            group = "build"
            description = "生成 $variantName 版本的 JAR"
            archiveFileName.set("AIDE-AS-${name}.jar")
            destinationDirectory.set(outputDir)
            val javaClasses = javaCompileProvider.get().destinationDirectory.get().asFile
            val kotlinCompileTask = project.tasks.named(
                "compile${variantName}Kotlin",
                org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java
            )
            val kotlinClasses = kotlinCompileTask.get().destinationDirectory.get().asFile
            from(javaClasses, kotlinClasses)
            include(
                "abcd/v7.class",
                "io/github/zeroaicy/aide/completion/XmlCompletionUtilsKt.class"
            )
            //exclude()
            dependsOn(javaCompileProvider)
            dependsOn(kotlinCompileTask)
        }

        variant.assembleProvider.get().finalizedBy(createJarTask)

    }

}


dependencies {
    api(projects.submodule.aide.appAideBase)

    api(projects.submodule.compiletion.xml.aaptcompiler)
    api(projects.submodule.eclipse.javaFormatter)

    api(projects.submodule.kotlin.compiler)


    api(projects.submodule.github.richpath)
    api(projects.submodule.github.treeView)
    api(projects.submodule.github.hideApi)
    api(projects.submodule.github.fastScrollEverywhere)
    api(projects.submodule.github.appIconloader)


    // 新增内容 2025.05.11
    api(libs.io.github.itsaky.nb.javac.android)
    api(libs.bundles.tinypinyin)
    implementation(libs.io.github.bumptech.glide)
    implementation(libs.com.github.megatronking.svg.support)
    implementation(libs.com.romainpiel.svgtoandroid)
    implementation(libs.io.github.bigBadaboom.androidsvg)

    implementation(libs.io.github.scwang90.refresh.layout.kernel)
    implementation(libs.io.github.scwang90.refresh.header.material)



    //多语言工具
    implementation(libs.io.github.getActivity.multiLanguages)
    // okhttp
    implementation(libs.io.github.squareup.okhttp3)
    implementation(libs.io.github.squareup.okhttp3.logging.interceptor)
    //工具库
    implementation(libs.io.github.blankj.android.utilcodex)
    // svg库
    implementation(libs.com.github.megatronking.svg.support)
    implementation(libs.com.romainpiel.svgtoandroid)
    // brv比较强大的一个recycler的框架
    implementation(libs.io.github.liangjingkanji.recyclerBRV)
    // sora-editor代码编辑框
    implementation(libs.io.github.rosemoe.sora.editor.editor)


    // 新版本的文件浏览用到了
    api(libs.bundles.libsu)


    // appAideBase 项目的依赖
    api(libs.bundles.rikkax.shizuku)

    api(libs.androidx.browser)

    api(libs.bundles.google.androidx)

    api(libs.androidx.legacy.support.v4)

    api(libs.androidx.multidex)


    // app_rewrite的依赖
    compileOnly(libs.org.eclipse.jdt.ecj)
    // jgit 7.0.0.202409031743-r
    compileOnly(libs.org.eclipse.jgit)

    api(libs.bundles.google.androidx)

    // 颜色选择控件
    api(libs.net.margaritov.preference.colorpickercolorpickerpreference)

    api(libs.org.jetbrains.annotations)

    // java-formatter依赖库 后期与java-formatter合并
    api(libs.org.eclipse.platform.text)
    compileOnly(libs.org.osgi.framework)

    // 依赖解析库
    api(libs.org.apache.maven.model)
    api(libs.org.codehaus.plexus.utils)

    api(libs.mmkv)

    // BC加密库
    api(libs.org.bouncycastle.bcprov.jdk18on)
    // apk签名
    api(libs.com.android.tools.build.apksig)

    // https://mvnrepository.com/artifact/com.google.prefab/api
    implementation(libs.com.google.prefab.api)
    //出现错误，使用本地jar
    api(files("libs/prefab-plugin-2.1.0.jar"))

    val aideLibraryDir = project.rootDir.resolve("AIDELibrary")


    aideLibraryDir.resolve("libs").listFiles()?.forEach {
        if (it.name.endsWith(".jar")) {
            api(files(it))
        }
    }

    aideLibraryDir.resolve("provider").listFiles()?.forEach {
        if (it.name.endsWith(".jar")) {
            compileOnly(files(it))
        }
    }


}

