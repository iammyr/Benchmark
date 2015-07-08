/**
 * OWASP Benchmark Project
 *
 * This file is part of the Open Web Application Security Project (OWASP)
 * Benchmark Project For details, please see
 * <a href="https://www.owasp.org/index.php/Benchmark">https://www.owasp.org/index.php/Benchmark</a>.
 *
 * The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, version 2.
 *
 * The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details
 *
 * @author Nick Sanidas <a href="https://www.aspectsecurity.com">Aspect Security</a>
 * @created 2015
 */

package org.owasp.benchmark.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class Utils {

	public static String getOSCommandString(String append) {

		String command = null;
		String osName = System.getProperty("os.name");
		if (osName.indexOf("Windows") != -1) {
			command = "cmd.exe /c " + append + " ";
		} else {
			command = "sh -c " + append + " ";
		}

		return command;
	}

	public static List<String> getOSCommandArray(String append) {

		ArrayList<String> cmds = new ArrayList<String>();

		String osName = System.getProperty("os.name");
		if (osName.indexOf("Windows") != -1) {
			cmds.add("cmd.exe");
			cmds.add("/c");
			if (append != null) {
				cmds.add(append);
			}
		} else {
			cmds.add("sh");
			cmds.add("-c");
			if (append != null) {
				cmds.add(append);
			}
		}

		return cmds;
	}

	public static void printOSCommandResults(java.lang.Process proc) {
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
				proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(
				proc.getErrorStream()));

		try {
			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out
					.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static javax.naming.directory.InitialDirContext getInitialDirContext()
			throws NamingException {
		/* Deprecated use LDAPHelper.getInitialDirContext() */
		InitialDirContext idc = Mockito.mock(InitialDirContext.class);

		@SuppressWarnings("unchecked")
		NamingEnumeration<SearchResult> ne = (NamingEnumeration<SearchResult>) Mockito
				.mock(NamingEnumeration.class);
		Mockito.when(
				idc.search(Mockito.anyString(), Mockito.anyString(),
						Mockito.any(SearchControls.class))).thenReturn(ne);
		Mockito.when(
				idc.search(Mockito.anyString(), Mockito.anyString(),
						Mockito.any(Object[].class),
						Mockito.any(SearchControls.class))).thenReturn(ne);

		return idc;
	}

	public static javax.naming.directory.DirContext getDirContext()
			throws NamingException {
		/* Deprecated use LDAPHelper.getDirContext() */
		DirContext dc = Mockito.mock(DirContext.class);

		@SuppressWarnings("unchecked")
		NamingEnumeration<SearchResult> ne = (NamingEnumeration<SearchResult>) Mockito
				.mock(NamingEnumeration.class);
		Mockito.when(
				dc.search(Mockito.anyString(), Mockito.anyString(),
						Mockito.any(SearchControls.class))).thenReturn(ne);
		Mockito.when(
				dc.search(Mockito.anyString(), Mockito.anyString(),
						Mockito.any(Object[].class),
						Mockito.any(SearchControls.class))).thenReturn(ne);

		return dc;
	}

	public static java.sql.Statement getSqlStatement() throws SQLException {

		java.sql.Statement s = Mockito.mock(java.sql.Statement.class);
		java.sql.ResultSet rs = Mockito.mock(java.sql.ResultSet.class);

		Mockito.stubVoid(s).toAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).on().addBatch(Mockito.anyString());
		Mockito.when(s.execute(Mockito.anyString())).thenReturn(true);
		Mockito.when(s.execute(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(true);
		Mockito.when(s.execute(Mockito.anyString(), Mockito.any(int[].class)))
				.thenReturn(true);
		Mockito.when(
				s.execute(Mockito.anyString(), Mockito.any(String[].class)))
				.thenReturn(true);
		Mockito.when(s.executeQuery(Mockito.anyString())).thenReturn(rs);
		Mockito.when(s.executeUpdate(Mockito.anyString())).thenReturn(1);
		Mockito.when(s.executeUpdate(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(1);
		Mockito.when(
				s.executeUpdate(Mockito.anyString(), Mockito.any(int[].class)))
				.thenReturn(1);
		Mockito.when(
				s.executeUpdate(Mockito.anyString(),
						Mockito.any(String[].class))).thenReturn(1);

		return s;
	}

	public static java.sql.Connection getSqlConnection() throws SQLException {

		java.sql.Connection c = Mockito.mock(java.sql.Connection.class);

		java.sql.CallableStatement cs = Mockito
				.mock(java.sql.CallableStatement.class);
		Mockito.when(c.prepareCall(Mockito.anyString())).thenReturn(cs);
		Mockito.when(
				c.prepareCall(Mockito.anyString(), Mockito.anyInt(),
						Mockito.anyInt())).thenReturn(cs);
		Mockito.when(
				c.prepareCall(Mockito.anyString(), Mockito.anyInt(),
						Mockito.anyInt(), Mockito.anyInt())).thenReturn(cs);
		Mockito.when(cs.execute()).thenReturn(true);

		java.sql.PreparedStatement ps = Mockito
				.mock(java.sql.PreparedStatement.class);
		Mockito.when(c.prepareStatement(Mockito.anyString())).thenReturn(ps);
		Mockito.when(c.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(ps);
		Mockito.when(
				c.prepareStatement(Mockito.anyString(), Mockito.anyInt(),
						Mockito.anyInt())).thenReturn(ps);
		Mockito.when(
				c.prepareStatement(Mockito.anyString(), Mockito.anyInt(),
						Mockito.anyInt(), Mockito.anyInt())).thenReturn(ps);
		Mockito.when(
				c.prepareStatement(Mockito.anyString(),
						Mockito.any(int[].class))).thenReturn(ps);
		Mockito.when(
				c.prepareStatement(Mockito.anyString(),
						Mockito.any(String[].class))).thenReturn(ps);
		Mockito.when(ps.execute()).thenReturn(true);

		return c;
	}

	public static File getFileFromClasspath(String fileName,
			ClassLoader classLoader) {
		URL url = classLoader.getResource(fileName);
		try {
			return new File(url.toURI().getPath());
		} catch (URISyntaxException e) {
			System.out.println("The file form the classpath cannot be loaded.");
		}
		return null;

	}

	public static List<String> getLinesFromFile(File file) {
		if (!file.exists()) {
			System.out.println("Can't find file to get lines from File.");
			return null;
		}

		FileReader fr = null;
		BufferedReader br = null;

		List<String> sourceLines = new ArrayList<String>();

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				sourceLines.add(line);
			}
		} catch (Exception e) {
			//
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (Exception ex) {
			}
		}

		return sourceLines;
	}

	public static final String testfileDir = System.getProperty("user.dir")
			+ File.separator + "testfiles" + File.separator;
}
