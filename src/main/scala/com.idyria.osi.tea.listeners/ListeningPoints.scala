package com.idyria.osi.tea.listeners



/**

    Trait to be implemented by classes whishing to offer support for listening points

*/
trait ListeningSupport {

    // Register Closures
    //-------------------

    // All the registered closures
    var listeningPoints = Map[String,scala.collection.mutable.Set[( () => Unit )]]()

    var listeningPointsWith = Map[String,scala.collection.mutable.Set[( Any => Unit )]]()

    /**
        Used to provide a closure to be run on a specific listening point

        Example:

            instance.on "listening.point.name" {
                println("Hello World from closure")
            }

    */
    def on(point:String)(closure: => Unit ) = {

       // println("Registering Closure for point "+point)

        // - Create Set if non existend
        // - Update otherwise
        //-------------------
        this.listeningPoints.get(point) match {
            case Some(set) => set += { () => closure }
            case None => {

                this.listeningPoints += (point -> scala.collection.mutable.Set[( () => Unit )]({ () => closure }) )

                //println("Setting up set, size is now: "+this.listeningPoints.size)
            }
        }
    }


    def onWith[T <: Any](point:String)(closure: T => Unit ) = {

       // println("Registering Closure for point "+point)

        // - Create Set if non existend
        // - Update otherwise
        //-------------------
        this.listeningPointsWith.get(point) match {
            case Some(set) => set += { (in : Any) => closure(in.asInstanceOf[T]) }
            case None => {

                this.listeningPointsWith += (point -> scala.collection.mutable.Set[( Any => Unit )]({ (in : Any) => closure(in.asInstanceOf[T]) }) )

                //println("Setting up set, size is now: "+this.listeningPoints.size)
            }
        }
    }


    // Call Listenening point
    //-----------------

    /**
        This method is used to call the closures registered for the listenerning point

        @param point The Listenening point name to be called
    */
    def @->[T <: Any] (point: String) = {

        //println("Running point "+point+", size: "+this.listeningPoints.size)

        this.listeningPoints.get(point) match {
            case Some(set) => set.foreach( cl => cl() )
            case None =>
        }
    }

    def @->[T <: Any](point: String,input:T) = {

        //println("Running point "+point+", size: "+this.listeningPoints.size)

        this.listeningPointsWith.get(point) match {
            case Some(set) => set.foreach( cl => cl(input) )
            case None =>
        }
    }



}
