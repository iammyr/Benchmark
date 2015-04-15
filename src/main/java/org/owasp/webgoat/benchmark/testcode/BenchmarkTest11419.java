package org.owasp.webgoat.benchmark.testcode;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BenchmarkTest11419")
public class BenchmarkTest11419 extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String param = "";
		java.util.Enumeration<String> names = request.getParameterNames();
		if (names.hasMoreElements()) {
			param = names.nextElement(); // just grab first element
		}

		String bar = new Test().doSomething(param);
		
		// javax.servlet.http.HttpSession.setAttribute(java.lang.String,java.lang.Object^)
		request.getSession().setAttribute( "foo", bar);
	}  // end doPost

    private class Test {

        public String doSomething(String param) throws ServletException, IOException {

		String bar = "safe!";
		java.util.HashMap<String,Object> map69868 = new java.util.HashMap<String,Object>();
		map69868.put("keyA-69868", "a_Value"); // put some stuff in the collection
		map69868.put("keyB-69868", param.toString()); // put it in a collection
		map69868.put("keyC", "another_Value"); // put some stuff in the collection
		bar = (String)map69868.get("keyB-69868"); // get it back out
		bar = (String)map69868.get("keyA-69868"); // get safe value back out

            return bar;
        }
    } // end innerclass Test

} // end DataflowThruInnerClass