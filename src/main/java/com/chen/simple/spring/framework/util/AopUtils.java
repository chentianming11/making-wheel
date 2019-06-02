package com.chen.simple.spring.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 陈添明
 * @date 2019/6/1
 */
public class AopUtils {

    public static Object invokeJoinpointUsingReflection(Object target, Method method, Object[] args)
            throws Throwable {

        // Use reflection to invoke the method.
        try {
            method.setAccessible(true);
            return method.invoke(target, args);
        }
        catch (InvocationTargetException ex) {
            // Invoked method threw a checked exception.
            // We must rethrow it. The client won't see the interceptor.
            throw ex.getTargetException();
        }
        catch (IllegalArgumentException ex) {
            throw new RuntimeException("AOP configuration seems to be invalid: tried calling method [" +
                    method + "] on target [" + target + "]", ex);
        }
        catch (IllegalAccessException ex) {
            throw new RuntimeException("Could not access method [" + method + "]", ex);
        }
    }
}
