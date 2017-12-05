package com.github.chen0040.bootslingshot.components;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by xschen on 21/12/2016.
 */
@Component
public class SpringAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

   @Override
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
           throws IOException, ServletException {


      String ajax = request.getParameter("ajax");

      String username = authentication.getName();

      System.out.println("User: "+username);

      if(ajax != null && ajax.equalsIgnoreCase("true"))
      {
         CsrfToken csrf = (CsrfToken)request.getAttribute(CsrfToken.class
                 .getName());
         response.getWriter().println("APP-AJAX-LOGIN-SUCCESS;"+csrf.getToken());
      }
      else
      {
         super.onAuthenticationSuccess(request, response, authentication);
      }
   }
}
