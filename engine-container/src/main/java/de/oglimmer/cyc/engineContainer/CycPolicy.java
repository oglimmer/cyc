package de.oglimmer.cyc.engineContainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * I would agree that it is a bad idea to implement your own Policy class, but Oracle's internal Policy class
 * implementation is holding (and KEEPING) references to all ClassLoaders it got. That means, if you need to garbage
 * collect ClassLoaders (and its loaded classes) Oracle's Policy class won't work. BUMMER.
 * 
 * @author oli
 */
@Slf4j
public class CycPolicy extends Policy {

	@Value
	static class PolicyEntry {
		private String className;
		private String name;
		private String actions;

		public boolean matchActions(String actionsToSearch) {
			if (actions == null) {
				return true;
			}
			int found = 0;
			for (String ss : actionsToSearch.split(",")) {
				for (String s : this.actions.split(",")) {
					if (s.equalsIgnoreCase(ss)) {
						found++;
					}
				}
			}
			return found == actionsToSearch.split(",").length;
		}
	}

	private Map<String, Collection<PolicyEntry>> permissions = new HashMap<>();
	private Policy delegate;

	@SneakyThrows
	public CycPolicy(Policy del) {
		this.delegate = del;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(
				"/security.cyc-policy")))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tmp = line.split(";");
				if (tmp[1].contains("${cyc.home}")) {
					String cycHome = System.getProperty("cyc.home");
					cycHome = cycHome.replace(".", "\\.");
					cycHome = cycHome.replace("/", "\\/");
					tmp[1] = tmp[1].replace("${cyc.home}", cycHome);
				}
				PolicyEntry pe;
				if (tmp.length == 2) {
					pe = new PolicyEntry(tmp[0], tmp[1], null);
				} else {
					pe = new PolicyEntry(tmp[0], tmp[1], tmp[2]);
				}
				// log.debug("Add:{}", pe);
				if (!permissions.containsKey(tmp[0])) {
					Collection<PolicyEntry> col = new ArrayList<>();
					col.add(pe);
					permissions.put(tmp[0], col);
				} else {
					Collection<PolicyEntry> col = permissions.get(tmp[0]);
					col.add(pe);
				}
			}
		}
	}

	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		PermissionCollection pc;
		if (delegate != null) {
			pc = delegate.getPermissions(codesource);
		} else {
			pc = super.getPermissions(codesource);
		}
		return pc;
	}

	@Override
	public boolean implies(ProtectionDomain domain, Permission permission) {
		String perClassName = permission.getClass().getName();
		Collection<PolicyEntry> col = permissions.get(perClassName);
		if (col == null) {
			log.debug("No permission at all for {}", permission.getClass().getName());
			return false;
		}
		String pName = permission.getName();
		if (isFilePermission(permission)) {
			pName = reduce(pName, false);
		}
		for (PolicyEntry pe : col) {
			String peName = pe.getName();
			if (isFilePermission(permission)) {
				peName = reduce(peName, true);
				// log.debug("Check IO: {} ==? {} => {}", pName, peName, pName.matches(peName));
			}
			if (pName.matches(peName) && pe.matchActions(permission.getActions())) {
				// log.debug("GRANTED for {}:{}:{}", permission.getClass().getName(), pName, permission.getActions());
				return true;
			}
		}
		log.debug("No permission for {}:{}:{}", permission.getClass().getName(), pName, permission.getActions());
		return false;
	}

	private boolean isFilePermission(Permission permission) {
		return "java.io.FilePermission".equals(permission.getClass().getName());
	}

	protected String reduce(String fileName, boolean escape) {
		if (escape) {
			fileName = fileName.replace("\\/", "/");
			fileName = fileName.replace("\\.", ".");
		}

		if (!fileName.startsWith("/")) {
			File current = new File(".");
			String currentAbs = current.getAbsolutePath();
			fileName = currentAbs + "/" + fileName;
		}
		String[] fileSegs = fileName.split("/");
		for (int i = 0; i < fileSegs.length; i++) {
			String fSeg = fileSegs[i];
			if (".".equals(fSeg)) {
				fileSegs = (String[]) ArrayUtils.remove(fileSegs, i);
				i--;
			} else if ("..".equals(fSeg)) {
				fileSegs = (String[]) ArrayUtils.remove(fileSegs, i);
				fileSegs = (String[]) ArrayUtils.remove(fileSegs, i - 1);
				i -= 2;
			}
		}
		String finalFileName = StringUtils.join(fileSegs, "/");

		if (escape) {
			fileName = fileName.replace("/", "\\/");
			fileName = fileName.replace(".", "\\.");
		}

		return finalFileName;
	}
}
