package es.uvigo.esei.dai.hybridserver.utils;


public class Tools {

    public static void error(Exception e) {
        System.err.println("\n\t[_ERROR_] " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }

    public static void info(String messagge) {
        System.out.println("\n[::] " + messagge);
    }

    public static void result(String messagge) {
        System.out.println("\n\t[--> " + messagge);
    }
}
