package com.idyria.osi.tea.listeners

import org.scalatest.BeforeAndAfterAll
import org.scalatest.GivenWhenThen
import org.scalatest.FunSuite
import org.scalatest.GivenWhenThen

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
    expectResult(1,"Closure must be in map")(supporter.listeningPoints("test").size)
    expectResult(2,"Closure must be in map")(supporter.listeningPointsWith("test").size)
    
    // Deregister
    //------------------
    supporter.deregister(cl)
    expectResult(0,"Closure must not be in map anymore")(supporter.listeningPoints("test").size)
    
    supporter.deregister(cl2)
    expectResult(1,"Closure must not be in map anymore")(supporter.listeningPointsWith("test").size)
    
    supporter.deregister(cl3)
    expectResult(0,"Closure must not be in map anymore")(supporter.listeningPointsWith("test").size)
    
    


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
    expect(true)(success)

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
    expect("Hello")(success)

    supporter.@->("test2", 10)
    expect("int")(success)

    supporter.@->("test2", "test")
    expect("string")(success)

    supporter.@->("test2", new InputObject)
    expect("unmatched")(success)

  }
}

