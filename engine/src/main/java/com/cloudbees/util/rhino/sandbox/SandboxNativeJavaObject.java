/*
 * Copyright 2013, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.util.rhino.sandbox;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 * A sandboxed {@link NativeJavaObject} that prevents using reflection to escape a sandbox.
 */
public class SandboxNativeJavaObject extends NativeJavaObject {

	private static final long serialVersionUID = 1L;

	public SandboxNativeJavaObject(Scriptable scope, Object javaObject,
                                   Class<?> staticType) {
        super(scope, javaObject, staticType);
    }

    @Override
    public Object get(String name, Scriptable start) {
        if ("getClass".equals(name)) {
            return NOT_FOUND;
        }
        return super.get(name, start);
    }
}
