/**
* OWASP Benchmark Project v1.1
*
* This file is part of the Open Web Application Security Project (OWASP)
* Benchmark Project. For details, please see
* <a href="https://www.owasp.org/index.php/Benchmark">https://www.owasp.org/index.php/Benchmark</a>.
*
* The Benchmark is free software: you can redistribute it and/or modify it under the terms
* of the GNU General Public License as published by the Free Software Foundation, version 2.
*
* The Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details
*
* @author Nick Sanidas <a href="https://www.aspectsecurity.com">Aspect Security</a>
* @created 2015
*/

package org.owasp.benchmark.testcode;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BenchmarkTest05254")
public class BenchmarkTest05254 extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		org.owasp.benchmark.helpers.SeparateClassRequest scr = new org.owasp.benchmark.helpers.SeparateClassRequest( request );
		String param = scr.getTheParameter("foo");
		
		
		// Chain a bunch of propagators in sequence
		String a54251 = param; //assign
		StringBuilder b54251 = new StringBuilder(a54251);  // stick in stringbuilder
		b54251.append(" SafeStuff"); // append some safe content
		b54251.replace(b54251.length()-"Chars".length(),b54251.length(),"Chars"); //replace some of the end content
		java.util.HashMap<String,Object> map54251 = new java.util.HashMap<String,Object>();
		map54251.put("key54251", b54251.toString()); // put in a collection
		String c54251 = (String)map54251.get("key54251"); // get it back out
		String d54251 = c54251.substring(0,c54251.length()-1); // extract most of it
		String e54251 = new String( new sun.misc.BASE64Decoder().decodeBuffer( 
		    new sun.misc.BASE64Encoder().encode( d54251.getBytes() ) )); // B64 encode and decode it
		String f54251 = e54251.split(" ")[0]; // split it on a space
		org.owasp.benchmark.helpers.ThingInterface thing = org.owasp.benchmark.helpers.ThingFactory.createThing();
		String g54251 = "barbarians_at_the_gate";  // This is static so this whole flow is 'safe'
		String bar = thing.doSomething(g54251); // reflection
		
		
		String sql = "{call verifyUserPassword('foo','"+bar+"')}";
				
		try {
			java.sql.Connection connection = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
			java.sql.CallableStatement statement = connection.prepareCall( sql );
		    statement.execute();
		} catch (java.sql.SQLException e) {
			throw new ServletException(e);
		}
	}
}
