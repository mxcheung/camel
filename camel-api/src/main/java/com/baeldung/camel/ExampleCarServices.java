package com.baeldung.camel;

/**
 * a Mock class to show how some other layer
 * (a persistence layer, for instance)
 * could be used insida a Camel
 * 
 */
public class ExampleCarServices {

    public static void example(MyCar bodyIn) {
        bodyIn.setModel( "Car Model " + bodyIn.getModel() );
        bodyIn.setId(bodyIn.getId()*10);
    }
}
