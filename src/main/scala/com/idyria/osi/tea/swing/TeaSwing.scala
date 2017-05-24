package com.idyria.osi.tea.swing

import javax.swing._

/**
    A few utils for tea
*/
object TeaSwing {


    /**
        Runs a provided closure on Swing Thread using SwingUtilities.invokeLater

    */
    def invokeLater( closure : => Unit) =  {

        SwingUtilities.invokeLater(new Runnable() {
                def run =  {
                    closure
                }
        })


    }

}
