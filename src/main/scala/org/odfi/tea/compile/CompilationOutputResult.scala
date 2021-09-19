package org.odfi.tea.compile

import dotty.tools.dotc.interfaces.{AbstractFile, CompilerCallback, SourceFile}

import java.io.IOException
import scala.reflect.ClassTag

class CompilationOutputResult(val outputClassDomain: ClassDomain) extends CompilerCallback {

  var generatedTypes: List[String] = List()


  override def onClassGenerated(src: SourceFile, cl: AbstractFile, name: String) {
    println("Generated Class: " + name)
    this.generatedTypes = this.generatedTypes :+ name

  }

  def hasGeneratedTypes = this.generatedTypes.nonEmpty

  def getFirstGeneratedType = this.generatedTypes.head

  def loadFirstGeneratedClass = this.outputClassDomain.loadClass(this.getFirstGeneratedType)

  def loadFirstGeneratedClassOfType[T](implicit tag: ClassTag[T]): Either[Class[T], Throwable] = this.loadFirstGeneratedClass match {
    case null => Right(new IOException(s"Cannot find class $loadFirstGeneratedClass"))
    case cl if ( tag.runtimeClass.isAssignableFrom(cl)/* || (
      tag.runtimeClass.isInterface && cl.getInterfaces.find(_ == tag.runtimeClass).isDefined
      )*/) => Left(cl.asInstanceOf[Class[T]])
    case other => Right(new IllegalArgumentException(s"Loaded class $other is not of type ${tag.runtimeClass}"))
  }

  def loadFirstGeneratedClassInstanceOfType[T](implicit tag: ClassTag[T]): Either[T, Throwable] = this.loadFirstGeneratedClassOfType match {
    case Left(cl) =>
      val inst = cl.getDeclaredConstructor().newInstance().asInstanceOf[T]
      Left(inst)
    case Right(e) => Right(e)
  }

}