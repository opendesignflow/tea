/*-
 * #%L
 * Tea Scala Utils Library
 * %%
 * Copyright (C) 2006 - 2017 Open Design Flow
 * %%
 * This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package scala

import com.idyria.osi.tea.errors.TError

sealed abstract class ErrorOption[+A] extends Product {

  self =>

  //----------------------
  // Error Support
  //----------------------
  var errors = List[TError]()
  
  def addError(e:TError) = errors = errors:+e

  //------------------------------------------
  // Scala Impl
  // Copied from Source of Scala
  //------------------------------------------

  /**
   * Returns true if the ErrorOption is $ENone, false otherwise.
   */
  def isEmpty: Boolean

  /**
   * Returns true if the ErrorOption is an instance of $ESome, false otherwise.
   */
  def isDefined: Boolean = !isEmpty

  /**
   * Returns the ErrorOption's value.
   *  @note The ErrorOption must be nonempty.
   *  @throws java.util.NoSuchElementException if the ErrorOption is empty.
   */
  def get: A

  /**
   * Returns the ErrorOption's value if the ErrorOption is nonempty, otherwise
   * return the result of evaluating `default`.
   *
   *  @param default  the default expression.
   */
  @inline final def getOrElse[B >: A](default: => B): B =
    if (isEmpty) default else this.get

  /**
   * Returns the ErrorOption's value if it is nonempty,
   * or `null` if it is empty.
   * Although the use of null is discouraged, code written to use
   * $ErrorOption must often interface with code that expects and returns nulls.
   * @example {{{
   * val initialText: ErrorOption[String] = getInitialText
   * val textField = new JComponent(initialText.orNull,20)
   * }}}
   */
  @inline final def orNull[A1 >: A](implicit ev: Null <:< A1): A1 = this getOrElse ev(null)

  /**
   * Returns a $ESome containing the result of applying $f to this $ErrorOption's
   * value if this $ErrorOption is nonempty.
   * Otherwise return $ENone.
   *
   *  @note This is similar to `flatMap` except here,
   *  $f does not need to wrap its result in an $ErrorOption.
   *
   *  @param  f   the function to apply
   *  @see flatMap
   *  @see foreach
   */
  @inline final def map[B](f: A => B): ErrorOption[B] =
    if (isEmpty) ENone else ESome(f(this.get))

  /**
   * Returns the result of applying $f to this $ErrorOption's
   *  value if the $ErrorOption is nonempty.  Otherwise, evaluates
   *  expression `ifEmpty`.
   *
   *  @note This is equivalent to `$ErrorOption map f getOrElse ifEmpty`.
   *
   *  @param  ifEmpty the expression to evaluate if empty.
   *  @param  f       the function to apply if nonempty.
   */
  @inline final def fold[B](ifEmpty: => B)(f: A => B): B =
    if (isEmpty) ifEmpty else f(this.get)

  /**
   * Returns the result of applying $f to this $ErrorOption's value if
   * this $ErrorOption is nonempty.
   * Returns $ENone if this $ErrorOption is empty.
   * Slightly different from `map` in that $f is expected to
   * return an $ErrorOption (which could be $ENone).
   *
   *  @param  f   the function to apply
   *  @see map
   *  @see foreach
   */
  @inline final def flatMap[B](f: A => ErrorOption[B]): ErrorOption[B] =
    if (isEmpty) ENone else f(this.get)

  def flatten[B](implicit ev: A <:< ErrorOption[B]): ErrorOption[B] =
    if (isEmpty) ENone else ev(this.get)

  /**
   * Returns this $ErrorOption if it is nonempty '''and''' applying the predicate $p to
   * this $ErrorOption's value returns true. Otherwise, return $ENone.
   *
   *  @param  p   the predicate used for testing.
   */
  @inline final def filter(p: A => Boolean): ErrorOption[A] =
    if (isEmpty || p(this.get)) this else ENone

  /**
   * Returns this $ErrorOption if it is nonempty '''and''' applying the predicate $p to
   * this $ErrorOption's value returns false. Otherwise, return $ENone.
   *
   *  @param  p   the predicate used for testing.
   */
  @inline final def filterNot(p: A => Boolean): ErrorOption[A] =
    if (isEmpty || !p(this.get)) this else ENone

  /**
   * Returns false if the ErrorOption is $ENone, true otherwise.
   *  @note   Implemented here to avoid the implicit conversion to Iterable.
   */
  final def nonEmpty = isDefined

  /**
   * Necessary to keep $ErrorOption from being implicitly converted to
   *  [[scala.collection.Iterable]] in `for` comprehensions.
   */
  @inline final def withFilter(p: A => Boolean): WithFilter = new WithFilter(p)

  /**
   * We need a whole WithFilter class to honor the "doesn't create a new
   *  collection" contract even though it seems unlikely to matter much in a
   *  collection with max size 1.
   */
  class WithFilter(p: A => Boolean) {
    def map[B](f: A => B): ErrorOption[B] = self filter p map f
    def flatMap[B](f: A => ErrorOption[B]): ErrorOption[B] = self filter p flatMap f
    def foreach[U](f: A => U): Unit = self filter p foreach f
    def withFilter(q: A => Boolean): WithFilter = new WithFilter(x => p(x) && q(x))
  }

  /**
   * Tests whether the ErrorOption contains a given value as an element.
   *
   *  @example {{{
   *  // Returns true because ESome instance contains string "something" which equals "something".
   *  ESome("something") contains "something"
   *
   *  // Returns false because "something" != "anything".
   *  ESome("something") contains "anything"
   *
   *  // Returns false when method called on ENone.
   *  ENone contains "anything"
   *  }}}
   *
   *  @param elem the element to test.
   *  @return `true` if the ErrorOption has an element that is equal (as
   *  determined by `==`) to `elem`, `false` otherwise.
   */
  final def contains[A1 >: A](elem: A1): Boolean =
    !isEmpty && this.get == elem

  /**
   * Returns true if this ErrorOption is nonempty '''and''' the predicate
   * $p returns true when applied to this $ErrorOption's value.
   * Otherwise, returns false.
   *
   *  @param  p   the predicate to test
   */
  @inline final def exists(p: A => Boolean): Boolean =
    !isEmpty && p(this.get)

  /**
   * Returns true if this ErrorOption is empty '''or''' the predicate
   * $p returns true when applied to this $ErrorOption's value.
   *
   *  @param  p   the predicate to test
   */
  @inline final def forall(p: A => Boolean): Boolean = isEmpty || p(this.get)

  /**
   * Apply the given procedure $f to the ErrorOption's value,
   *  if it is nonempty. Otherwise, do nothing.
   *
   *  @param  f   the procedure to apply.
   *  @see map
   *  @see flatMap
   */
  @inline final def foreach[U](f: A => U) {
    if (!isEmpty) f(this.get)
  }

  /**
   * Returns a $ESome containing the result of
   * applying `pf` to this $ErrorOption's contained
   * value, '''if''' this ErrorOption is
   * nonempty '''and''' `pf` is defined for that value.
   * Returns $ENone otherwise.
   *
   *  @example {{{
   *  // Returns ESome(HTTP) because the partial function covers the case.
   *  ESome("http") collect {case "http" => "HTTP"}
   *
   *  // Returns ENone because the partial function doesn't cover the case.
   *  ESome("ftp") collect {case "http" => "HTTP"}
   *
   *  // Returns ENone because the ErrorOption is empty. There is no value to pass to the partial function.
   *  ENone collect {case value => value}
   *  }}}
   *
   *  @param  pf   the partial function.
   *  @return the result of applying `pf` to this $ErrorOption's
   *  value (if possible), or $ENone.
   */
  @inline final def collect[B](pf: PartialFunction[A, B]): ErrorOption[B] =
    if (!isEmpty) pf.lift(this.get) else ENone

  /**
   * Returns this $ErrorOption if it is nonempty,
   *  otherwise return the result of evaluating `alternative`.
   *  @param alternative the alternative expression.
   */
  @inline final def orElse[B >: A](alternative: => ErrorOption[B]): ErrorOption[B] =
    if (isEmpty) alternative else this

  /**
   * Returns a singleton iterator returning the $ErrorOption's value
   * if it is nonempty, or an empty iterator if the ErrorOption is empty.
   */
  def iterator: Iterator[A] =
    if (isEmpty) collection.Iterator.empty else collection.Iterator.single(this.get)

  /**
   * Returns a singleton list containing the $ErrorOption's value
   * if it is nonempty, or the empty list if the $ErrorOption is empty.
   */
  def toList: List[A] =
    if (isEmpty) List() else new ::(this.get, Nil)

  /**
   * Returns a [[scala.util.Left]] containing the given
   * argument `left` if this $ErrorOption is empty, or
   * a [[scala.util.Right]] containing this $ErrorOption's value if
   * this is nonempty.
   *
   * @param left the expression to evaluate and return if this is empty
   * @see toLeft
   */
  @inline final def toRight[X](left: => X) =
    if (isEmpty) Left(left) else Right(this.get)

  /**
   * Returns a [[scala.util.Right]] containing the given
   * argument `right` if this is empty, or
   * a [[scala.util.Left]] containing this $ErrorOption's value
   * if this $ErrorOption is nonempty.
   *
   * @param right the expression to evaluate and return if this is empty
   * @see toRight
   */
  @inline final def toLeft[X](right: => X) =
    if (isEmpty) Right(right) else Left(this.get)
}

//final case class ESome

case object ENone extends ErrorOption[Nothing] {
  def isEmpty = true
  def get = throw new NoSuchElementException("ENone.get")
  
  
}

final case class ESome[+A](value: A) extends ErrorOption[A] {
  def isEmpty = false
  def get = value
  
  def apply(t:Throwable) = {
    var e = new ESome
    e.addError(new TError(t))
    e
  }

}

final case class EError(value: TError) extends ErrorOption[Nothing] {
  def isEmpty = true
  def get = throw new NoSuchElementException("ENone.get")
  
  

}

object EError {
  
   def apply(x: Throwable): EError =  {
    var e = new EError(new TError(x))
    e.addError(new TError(x))
    e
  }
  
}

object ErrorOption {

  import scala.language.implicitConversions

  implicit def optionToErrorOption[A](xo: Option[A]): ErrorOption[A] = {
    xo match {
      case Some(v) => ESome(v)
      case None => ENone
    }
  }

  /**
   * An implicit conversion that converts an ErrorOption to an iterable value
   */
  implicit def option2Iterable[A](xo: ErrorOption[A]): Iterable[A] = xo.toList

  /**
   * An ErrorOption factory which creates ESome(x) if the argument is not null,
   *  and ENone if it is null.
   *
   *  @param  x the value
   *  @return   ESome(value) if value != null, ENone if value == null
   */
  def apply[A](x: A): ErrorOption[A] = if (x == null) ENone else ESome(x)

  def apply(x: Throwable): EError =  {
    var e = new EError(new TError(x))
    e.addError(new TError(x))
    e
  }
  
  /**
   * An ErrorOption factory which returns `ENone` in a manner consistent with
   *  the collections hierarchy.
   */
  def empty[A]: ErrorOption[A] = ENone
}
