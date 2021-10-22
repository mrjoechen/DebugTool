package cn.eeo.debugtool.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.io.*
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * Created by chenqiao on 2021/10/17.
 * e-mail : mrjctech@gmail.com
 */
abstract class BaseInjection {

  private val PLUGIN_LIBRARY = "cn.eeo.debug.lib"


  open fun checkClass(className: String): Boolean {
    return className.endsWith(".class") &&
        ! className.contains("R$") &&
        ! className.contains("R.class") &&
        ! className.contains("BuildConfig.class") &&
        ! className.contains(PLUGIN_LIBRARY)
  }

  fun transformDirectoryFiles(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider) {
    directoryInput.file.walkTopDown()
      .filter {it.isFile && checkClass(it.name)}
      .forEach {file ->
        transformClassFile(file)
      }
    val dest = outputProvider.getContentLocation(
      directoryInput.name,
      directoryInput.contentTypes,
      directoryInput.scopes,
      Format.DIRECTORY
    )

    FileUtils.copyDirectory(directoryInput.file, dest)
  }


  fun transformSingleClassFile(inputFile: File, outputFile: File?, inputBaseDir: String) {
    var inputBaseDir = inputBaseDir
    if (! inputBaseDir.endsWith(File.separator)) inputBaseDir += File.separator
    if (checkClass(inputFile.absolutePath.replace(inputBaseDir, "").replace(File.separator, "."))) {
      org.apache.commons.io.FileUtils.touch(outputFile)
      val inputStream: InputStream = FileInputStream(inputFile)
      val bytes: ByteArray = transformClassFile(inputStream)
      val fos = FileOutputStream(outputFile)
      fos.write(bytes)
      fos.close()
      inputStream.close()
    } else {
      if (inputFile.isFile) {
        org.apache.commons.io.FileUtils.touch(outputFile)
        org.apache.commons.io.FileUtils.copyFile(inputFile, outputFile)
      }
    }
  }

  fun transformClassFile(file: File) {
    FileInputStream(file).use {fis ->
      file.writeBytes(transformClassFile(fis))
    }
  }

  open fun transformClassFile(inputStream: InputStream): ByteArray {
    val classReader = ClassReader(inputStream)
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(getClassVisitor(classWriter), ClassReader.EXPAND_FRAMES)
    return classWriter.toByteArray()
  }

  fun transformJarFiles(jarInput: JarInput, outputProvider: TransformOutputProvider) {
    if (! jarInput.file.absolutePath.endsWith(".jar")) {
      println(jarInput.file.absolutePath)
      return
    }

    val tmpFile = File(jarInput.file.parentFile, "${jarInput.file.name}.temp").also {it.createNewFile()}

    JarFile(jarInput.file).use {jarFile ->
      JarOutputStream(FileOutputStream(tmpFile)).use {jarOutputStream ->
        jarFile.entries().iterator().forEach {jarEntry ->
          val zipEntry = ZipEntry(jarEntry.name)
          jarFile.getInputStream(jarEntry).use {inputStream ->
            if (checkClass(jarEntry.name)) {
              jarOutputStream.putNextEntry(zipEntry)
              jarOutputStream.write(transformClassFile(inputStream))
            } else {
              jarOutputStream.putNextEntry(zipEntry)
              jarOutputStream.write(IOUtils.toByteArray(inputStream))
            }
          }

          jarOutputStream.closeEntry()
        }
      }
    }

    val dest = outputProvider.getContentLocation(
      jarInput.file.nameWithoutExtension + DigestUtils.md5Hex(jarInput.file.absolutePath),
      jarInput.contentTypes,
      jarInput.scopes,
      Format.JAR
    )
    FileUtils.copyFile(tmpFile, dest)
  }


  abstract fun getClassVisitor(classWriter: ClassWriter): ClassVisitor
}