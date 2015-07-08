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
* @author Dave Wichers <a href="https://www.aspectsecurity.com">Aspect Security</a>
* @created 2015
*/

package org.owasp.benchmark.score.parsers;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class FindbugsReader extends Reader {
	
	public TestResults parse( File f ) throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		InputSource is = new InputSource( new FileInputStream(f) );
		Document doc = docBuilder.parse(is);
		
		TestResults tr = new TestResults();
		
		// If the filename includes an elapsed time in seconds (e.g., TOOLNAME-seconds.xml), set the compute time on the scorecard.
		tr.setTime(f);

		NodeList nl = doc.getDocumentElement().getChildNodes();
		for ( int i = 0; i < nl.getLength(); i++ ) {
			Node n = nl.item( i );
			if ( n.getNodeName().equals( "BugInstance")) {
				TestCaseResult tcr = parseFindBugsBug( n );
				if ( tcr != null ) {
					tr.put( tcr );
				}
			}
		}
		
		return tr;
	}
	
	private TestCaseResult parseFindBugsBug(Node n) {
		TestCaseResult tcr = new TestCaseResult();

		NamedNodeMap attrs = n.getAttributes();
		if ( attrs.getNamedItem( "category" ).getNodeValue().equals( "SECURITY") ) {
			Node cl = getNamedNode( "Class", n.getChildNodes() );
			String classname = cl.getAttributes().getNamedItem("classname").getNodeValue();
			classname = classname.substring( classname.lastIndexOf('.') + 1);
			if ( classname.startsWith( "BenchmarkTest" ) ) {
				String testNumber = classname.substring( "BenchmarkTest".length() );
				tcr.setNumber( Integer.parseInt( testNumber ) );
			}
			
			Node cwenode = attrs.getNamedItem("cweid");
			Node catnode = attrs.getNamedItem("abbrev");
			tcr.setCWE( figureCWE( tcr, cwenode, catnode ) );
			
			String type = attrs.getNamedItem("type").getNodeValue();
			tcr.setCategory( type );
			
			return tcr;
		}
		return null;
	}
	
	
	private static int figureCWE( TestCaseResult tcr, Node cwenode, Node catnode) {
		String cwe = null;
		if ( cwenode != null ) {
			cwe = cwenode.getNodeValue();
		}
		
		String cat = null;
		if ( catnode != null ) {
			cat = catnode.getNodeValue();
		}
		tcr.setEvidence( "FB:" + cwe + "::" + cat);

		if ( cwe != null ) {
			// FIX path traversal CWEs
			if ( cwe.equals( "23" ) || cwe.equals( "36" ) ) {
				cwe = "22";
			}
			return Integer.parseInt( cwe );
		}
		
		switch( cat ) {
		case "SECCU" : 		return 614;  // insecure cookie use
		case "SECPR" : 		return 330;  // weak random
		case "SECLDAPI" : 	return 90;   // ldap injection
		case "SECPTO" : 	return 22;   // path traversal
		case "SECPTI" : 	return 22;   // path traversal
		case "CIPINT" : 	return 327;	 // weak encryption - cipher with no integrity
		case "PADORA" : 	return 327;  // padding oracle -- FIXME: probably wrong
		case "SECXPI" : 	return 643;  // xpath injection
		case "SECWMD" : 	return 328;  // weak hash
		case "SECCI" : 		return 78;   // command injection
		case "SECDU" : 		return 327;  // weak encryption DES
		case "SECXRW" :		return 79;   // xss
		case "SECXSS1" :	return 79;   // xss
		case "SECXSS2" :	return 79;   // xss

		case "SECSP" : 		return 00;	 // servlet parameter - not a vuln
		case "SECSH" : 		return 00;   // servlet header -- not a vuln
		case "SECSSQ" : 	return 00;   // servlet query - not a vuln
		
		default : System.out.println( "Unknown category: " + cat );
		}

		return 0;
		
	}

}
