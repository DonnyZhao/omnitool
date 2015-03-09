//   Copyright 2014 Commonwealth Bank of Australia
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package au.com.cba.omnia.omnitool

import scalaz.syntax.monad._

/** Convenient operations that you can do on the companion of a [[ResultantMonad]]. */
final class ResultantOps[O, M[_]](self: O)(implicit val M: ResultantMonad[M]) {
  /** Build an operation from a value. The resultant DB operation will not throw an exception. */
  def value[A](v: => A): M[A] =
    M.point(v)

  /** Builds an operation from a [[Result]]. */
  def result[A](v: => Result[A]): M[A] =
    M.rPoint(v)

  /** Build a failed operation from the specified message. */
  def fail[A](message: String): M[A] =
    M.rPoint(Result.fail(message))

  /** Build a failed M operation from the specified exception. */
  def exception[A](t: Throwable): M[A] =
    M.rPoint(Result.exception(t))

  /** Build a failed M operation from the specified exception and message. */
  def error[A](message: String, t: Throwable): M[A] =
    M.rPoint(Result.error(message, t))

  /**
    * Fails if condition is not met
    *
    * Provided instead of [[scalaz.MonadPlus]] typeclass, as M does not
    * quite meet the required laws.
    */
  def guard(ok: Boolean, message: String): M[Unit] =
    M.rPoint(Result.guard(ok, message))

  /**
    * Fails if condition is met
    *
    * Provided instead of [[scalaz.MonadPlus]] typeclass, as M does not
    * quite meet the required laws.
    */
  def prevent(fail: Boolean, message: String): M[Unit] =
    M.rPoint(Result.prevent(fail, message))

  /**
    * Ensures a M operation returning a boolean success flag fails if unsuccessful
    *
    * Provided instead of [[scalaz.MonadPlus]] typeclass, as M does not
    * quite meet the required laws.
    */
  def mandatory(action: M[Boolean], message: String): M[Unit] =
    action flatMap (guard(_, message))

  /**
    * Ensures a M operation returning a boolean success flag fails if successful
    *
    * Provided instead of [[scalaz.MonadPlus]] typeclass, as M does not
    * quite meet the required laws.
    */
  def forbidden(action: M[Boolean], message: String): M[Unit] =
    action flatMap (prevent(_, message))
}
