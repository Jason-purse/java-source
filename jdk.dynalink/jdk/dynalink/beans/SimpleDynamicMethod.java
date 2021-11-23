/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 */
/*
   Copyright 2009-2013 Attila Szegedi

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:
   * Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
   * Neither the name of the copyright holder nor the names of
     contributors may be used to endorse or promote products derived from
     this software without specific prior written permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
   IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
   TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
   PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDER
   BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
   SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
   BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
   WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
   OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
   ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package jdk.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import jdk.dynalink.CallSiteDescriptor;

/**
 * A dynamic method bound to exactly one Java method or constructor that is not caller sensitive. Since its target is
 * not caller sensitive, this class pre-caches its method handle and always returns it from the call to
 * {@link #getTarget(CallSiteDescriptor)}. Can be used in general to represents dynamic methods bound to a single method handle,
 * even if that handle is not mapped to a Java method, i.e. as a wrapper around field getters/setters, array element
 * getters/setters, etc.
 */
class SimpleDynamicMethod extends SingleDynamicMethod {
    private final MethodHandle target;
    private final boolean constructor;

    /**
     * Creates a new simple dynamic method, with a name constructed from the class name, method name, and handle
     * signature.
     *
     * @param target the target method handle
     * @param clazz the class declaring the method
     * @param name the simple name of the method
     */
    SimpleDynamicMethod(final MethodHandle target, final Class<?> clazz, final String name) {
        this(target, clazz, name, false);
    }

    /**
     * Creates a new simple dynamic method, with a name constructed from the class name, method name, and handle
     * signature.
     *
     * @param target the target method handle
     * @param clazz the class declaring the method
     * @param name the simple name of the method
     * @param constructor does this represent a constructor?
     */
    SimpleDynamicMethod(final MethodHandle target, final Class<?> clazz, final String name, final boolean constructor) {
        super(getName(target, clazz, name, constructor));
        this.target = target;
        this.constructor = constructor;
    }

    private static String getName(final MethodHandle target, final Class<?> clazz, final String name, final boolean constructor) {
        return getMethodNameWithSignature(target.type(), constructor ? name : getClassAndMethodName(clazz, name), !constructor);
    }

    @Override
    boolean isVarArgs() {
        return target.isVarargsCollector();
    }

    @Override
    MethodType getMethodType() {
        return target.type();
    }

    @Override
    MethodHandle getTarget(final CallSiteDescriptor desc) {
        return target;
    }

    @Override
    boolean isConstructor() {
        return constructor;
    }
}
