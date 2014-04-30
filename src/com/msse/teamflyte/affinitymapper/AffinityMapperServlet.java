package com.msse.teamflyte.affinitymapper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequestInitializer;

public class AffinityMapperServlet extends HttpServlet {

	private static final String API_KEY = "AIzaSyBuuPqpcvJREb3mGNr9am-mD3d0_R4TdTQ";

	private static final long serialVersionUID = 1;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		GoogleAuthorizationCodeFlow authFlow = Utils.initializeFlow();
		Credential credential = authFlow.loadCredential("udeeb");
		if (credential == null) {
			// If we don't have a token in store, redirect to authorization
			// screen.
			resp.sendRedirect(authFlow.newAuthorizationUrl()
					.setRedirectUri(Utils.getRedirectUri(req)).build());
			return;
		}

		// If we do have stored credentials, build the Plus object using them.
		Plus plus = new Plus.Builder(Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY,
				credential)
				.setApplicationName("Affinity Mapper")
				.setGoogleClientRequestInitializer(
						new PlusRequestInitializer(API_KEY)).build();
		// Make the API call
		com.google.api.services.plus.model.Person profile = plus.people()
				.get("me").execute();
		// Send the results as the response
		PrintWriter respWriter = resp.getWriter();
		resp.setStatus(200);
		resp.setContentType("text/html");
		respWriter.println("<img src='" + profile.getImage().getUrl() + "'>");
		respWriter.println("<a href='" + profile.getUrl() + "'>"
				+ profile.getDisplayName() + "</a>");
	}

}
