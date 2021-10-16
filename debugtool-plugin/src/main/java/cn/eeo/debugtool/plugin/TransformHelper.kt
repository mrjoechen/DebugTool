package cn.eeo.debugtool.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * Created by chenqiao on 2021/10/12.
 * e-mail : mrjctech@gmail.com
 */
object TransformHelper {

  fun transformDirectoryFiles(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider) {
    directoryInput.file.walkTopDown()
      .filter { it.isFile && it.name.endsWith(".class")}
      .forEach { file ->
        FileInputStream(file).use { fis ->
          print(file.absolutePath)
          val classReader = ClassReader(fis)
          val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
          classReader.accept(DebugToolClassVisitor(classWriter), EXPAND_FRAMES)

          file.writeBytes(classWriter.toByteArray())
        }
      }
    val dest = outputProvider.getContentLocation(
      directoryInput.name,
      directoryInput.contentTypes,
      directoryInput.scopes,
      Format.DIRECTORY)

    FileUtils.copyDirectory(directoryInput.file, dest)
  }

  fun transformJarFiles(jarInput: JarInput, outputProvider: TransformOutputProvider) {
    if (!jarInput.file.absolutePath.endsWith(".jar")) {
      println(jarInput.file.absolutePath)
      return
    }

    val tmpFile = File(jarInput.file.parentFile, "${jarInput.file.name}.temp").also { it.createNewFile() }

    JarFile(jarInput.file).use { jarFile ->
      JarOutputStream(FileOutputStream(tmpFile)).use { jarOutputStream ->
        jarFile.entries().iterator().forEach { jarEntry ->
          val zipEntry = ZipEntry(jarEntry.name)
          jarFile.getInputStream(jarEntry).use { inputStream ->
            if (jarEntry.name.endsWith(".class")) {
              jarOutputStream.putNextEntry(zipEntry)
              val classReader = ClassReader(IOUtils.toByteArray(inputStream))
              val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
              classReader.accept(DebugToolClassVisitor(classWriter), EXPAND_FRAMES)
              jarOutputStream.write(classWriter.toByteArray())
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
      Format.JAR)
    FileUtils.copyFile(tmpFile, dest)
  }

}