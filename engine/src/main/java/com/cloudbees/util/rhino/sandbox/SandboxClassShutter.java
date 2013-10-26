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

import org.mozilla.javascript.ClassShutter;

/**
 * A {@link ClassShutter} that locks out access to all native classes.
 */
public class SandboxClassShutter implements ClassShutter {
	public boolean visibleToScripts(String fullClassName) {
		switch (fullClassName) {
		case "de.oglimmer.cyc.api.Company":
		case "de.oglimmer.cyc.api.HumanResources":
		case "de.oglimmer.cyc.api.ApplicationProfiles":
		case "de.oglimmer.cyc.api.ApplicationProfile":
		case "de.oglimmer.cyc.api.RealEstateProfile":
		case "de.oglimmer.cyc.api.RealEstateProfiles":
		case "de.oglimmer.cyc.api.Menu":
		case "de.oglimmer.cyc.api.MenuEntry":
		case "de.oglimmer.cyc.api.Grocer":
		case "de.oglimmer.cyc.api.Employee":
		case "de.oglimmer.cyc.api.Establishment":
		case "de.oglimmer.cyc.api.JobPosition":
		case "de.oglimmer.cyc.api.FoodDelivery":
		case "de.oglimmer.cyc.api.FoodUnit":
		case "de.oglimmer.cyc.api.UnmodifiableIterator":
		case "de.oglimmer.cyc.api.DebugAdapter":
		case "java.lang.String":
		case "java.util.HashSet":
		case "java.util.ArrayList":
		case "java.util.Collections$UnmodifiableSet":
		case "java.util.Collections$UnmodifiableCollection$1":
		case "java.util.Collections$UnmodifiableRandomAccessList":
		case "java.io.PrintStream":
		case "java.lang.Object":
			return true;
		}
		return false;
	}
}
