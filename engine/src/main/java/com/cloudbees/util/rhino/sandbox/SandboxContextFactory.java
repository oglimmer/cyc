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

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

/**
 * A sandboxed {@link ContextFactory} that prevents access to all native classes.
 */
public class SandboxContextFactory extends ContextFactory {

	@Override
	protected Context makeContext() {
		Context context = new TimeBoxedContext(this);

		// Use pure interpreter mode to allow for
		// observeInstructionCount(Context, int) to work
		context.setOptimizationLevel(-1);
		// Make Rhino runtime to call observeInstructionCount
		// each 10000 bytecode instructions
		context.setInstructionObserverThreshold(10000);

		context.setWrapFactory(new SandboxWrapFactory());
		context.setClassShutter(new SandboxClassShutter());
		return context;
	}

	@Override
	protected Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		TimeBoxedContext mcx = (TimeBoxedContext) cx;
		mcx.startTime = System.currentTimeMillis();

		return super.doTopCall(callable, cx, scope, thisObj, args);
	}

	@Override
	protected void observeInstructionCount(Context cx, int instructionCount) {
		// http://stackoverflow.com/questions/93911/how-can-you-run-javascript-using-rhino-for-java-in-a-sandbox

		TimeBoxedContext mcx = (TimeBoxedContext) cx;
		long currentTime = System.currentTimeMillis();
		if (currentTime - mcx.startTime > 5 * 1000) {
			// More then 10 seconds from Context creation time:
			// it is time to stop the script.
			// Throw Error instance to ensure that script will never
			// get control back through catch or finally.
			throw new Error();
		}
	}

	class TimeBoxedContext extends Context {

		private long startTime;

		public TimeBoxedContext(SandboxContextFactory sandboxContextFactory) {
			super(sandboxContextFactory);
			startTime = System.currentTimeMillis();
		}

	}
}
