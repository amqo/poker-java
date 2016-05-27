/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.utils;

import java.text.MessageFormat;

/**
 *
 * @author alberto
 */
public final class ExceptionUtil {

    public static final String NULL_ERR_MSG = "El argumento {0} no puede ser nulo.";
    public static final String LENGTH_ERR_MSG
            = "El argumento {0} no puede ser nulo y debe tener una longitud de {1}.";
    public static final String MIN_ERR_MSG
            = "El argumento {0} no tiene un valor válido.";

    private ExceptionUtil() {
    }

    public static void checkNullArgument(Object o, String name) {
        if (o == null) {
            throw new IllegalArgumentException(
                    MessageFormat.format(NULL_ERR_MSG, name));
        }
    }
    
    public static <T> void checkArrayLengthArgument(T[] a, String name, int l) {
        if (a == null || a.length != l) {
            throw new IllegalArgumentException(
                    MessageFormat.format(LENGTH_ERR_MSG, name, l));
        }
    }

    public static void checkArgument(boolean throwEx, String msg, Object... args) {
        if (throwEx) {
            throw new IllegalArgumentException(
                    MessageFormat.format(msg, args));
        }
    }
    
    public static void checkMinValueArgument(long arg1, long arg2, String name) {
        if (arg1 < arg2) {
            throw new IllegalArgumentException(
                    MessageFormat.format(MIN_ERR_MSG, name));
        }
    }
}
