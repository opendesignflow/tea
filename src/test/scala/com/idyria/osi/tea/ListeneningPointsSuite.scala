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

