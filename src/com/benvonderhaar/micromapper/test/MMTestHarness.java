package com.benvonderhaar.micromapper.test;

import static org.junit.Assert.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.benvonderhaar.micromapper.annotation.RequestMethod;
import com.benvonderhaar.micromapper.base.MMMethodPayload;
import com.benvonderhaar.micromapper.base.MicroMapperRouter;
import com.benvonderhaar.micromapper.test.annotation.MMControllerReference;

public class MMTestHarness {

	private MicroMapperRouter router;
	
	public MMTestHarness(Class<? extends MicroMapperRouter> router) {
		try {
			this.router = router.newInstance();
			this.router.init();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// TODO can this be more cleanly integrated as an extension of Assert?
	
	public void assertMapping(String url, String referenceId) {
		
		System.out.println("about to fail");
		
		// TODO use context path and requestURL more cleanly?
		HttpServletRequest req = new HttpServletRequestBuilder("/mm-test-harness", "http://localhost:8080/mm-test-harness" + url);
		HttpServletResponse resp = new HttpServletResponseBuilder();
		
		try {
			MMMethodPayload methodPayload = router.getIntendedControllerMethod(req, resp, RequestMethod.Verb.GET);
			
			System.out.println(methodPayload.getMethod());
			
			if (null == methodPayload.getMethod().getAnnotation(MMControllerReference.class)) {
				fail("Matched method (" + methodPayload.getMethod().toGenericString() + ") is not annotated with @MMControllerReference");
			}
			
			assertTrue("Matched method (" + methodPayload.getMethod().toGenericString() + ") referenceId \""
						+ methodPayload.getMethod().getAnnotation(MMControllerReference.class).referenceId()
						+ "\" does not match provided referenceId \"" + referenceId + "\"",
					methodPayload.getMethod().getAnnotation(MMControllerReference.class).referenceId().equals(referenceId));
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}		
	}
}
