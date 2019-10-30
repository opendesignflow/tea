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
package com.idyria.osi.tea

import org.scalatest.GivenWhenThen
import org.scalatest.FunSuite
import org.scalatest.GivenWhenThen
import com.idyria.osi.tea.listeners.ListeningSupport

class ListeningPointsSuite extends FunSuite with GivenWhenThen {

  test("Register Deregister") {

    class TestSupporter extends ListeningSupport {

    }

    // Instanciate
    //--------------------
    var supporter = new TestSupporter

    // Register
    //--------------
    var cl = supporter.on("test") {

    }
    var cl2 = supporter.onWith("test") {
    	
      i : Int => 
      
    }
    var cl3 = supporter.onMatch("test") {
      case i:Int =>
        
      case _ =>
    }
    
    // Check
    //--------------
    assertResult(1,"Closure must be in map")(supporter.listeningPoints("test").size)
    assertResult(2,"Closure must be in map")(supporter.listeningPointsWith("test").size)
    
    // Deregister
    //------------------
    supporter.deregister(cl)
    assertResult(0,"Closure must not be in map anymore")(supporter.listeningPoints("test").size)
    
    supporter.deregister(cl2)
    assertResult(1,"Closure must not be in map anymore")(supporter.listeningPointsWith("test").size)
    
    supporter.deregister(cl3)
    assertResult(0,"Closure must not be in map anymore")(supporter.listeningPointsWith("test").size)
    
    


  }

  test("Run a single listenning point") {

    class TestSupporter extends ListeningSupport {

    }

    // Instanciate
    //----------------
    var success = false
    var supporter = new TestSupporter {

      on("test") {
        success = true
      }

    }
    supporter.@->("test")

    // Test
    //---------------
    assertResult(true)(success)

  }

  test("Run a listenning point with object input") {

    class TestSupporter extends ListeningSupport {}

    class InputObject { var str = "Hello" }

    // Instanciate
    //----------------
    var success = "hi"
    var supporter = new TestSupporter {

      onWith("test") {

        in: InputObject =>

          success = in.str
      }

      onMatch("test2") {

        case in: String => success = "string"
        case in: Int    => success = "int"
        case _          => success = "unmatched"

      }

    }

    // Test
    //---------------
    supporter.@->("test", new InputObject)
    assertResult("Hello")(success)

    supporter.@->("test2", 10)
    assertResult("int")(success)

    supporter.@->("test2", "test")
    assertResult("string")(success)

    supporter.@->("test2", new InputObject)
    assertResult("unmatched")(success)

  }
}

