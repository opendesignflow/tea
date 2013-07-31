package com.idyria.osi.tea.listeners


import org.scalatest.BeforeAndAfterAll
import org.scalatest.GivenWhenThen
import org.scalatest.FunSuite
import org.scalatest.GivenWhenThen

class ListeningPointsSuite extends FunSuite with GivenWhenThen {


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

        class TestSupporter extends ListeningSupport { }

        class InputObject { var str = "Hello" }

        // Instanciate
        //----------------
        var success = "hi"
        var supporter = new TestSupporter {

            onWith("test") {

                in : InputObject =>

                success = in.str
            }

        }
        supporter.@->("test",new InputObject)


        // Test
        //---------------
        expect("Hello")(success)

    }
}

