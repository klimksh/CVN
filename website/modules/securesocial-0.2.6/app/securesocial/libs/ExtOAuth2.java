package securesocial.libs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import play.exceptions.UnexpectedException;
import play.libs.OAuth2;
import play.libs.WS;
import play.mvc.Scope;
import play.mvc.results.Redirect;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Extended version of OAuth2 class providing the followed improvment :
 * <ul>
 * <li>Choose between GET/POST to request tokenURL (default is GET)</li>
 * <li>Allow extra parameters to request authorizationURL (scope, ...)</li>
 * <li>Allow extra parameters to request tokenURL (scope, ...)</li>
 * </ul>
 *
 * @author cspada
 */
public class ExtOAuth2 extends OAuth2 {

    // Json response properties
    private static final String ACCESS_TOKEN = "access_token";
    static  String clientid="201851680458-sumhf8v1qua2s1bn33dk9o8q34b0kvav.apps.googleusercontent.com";
    static String secret="vod0-K3Hz9kDTKaJkBqNqnvG";
    private static final String OAUTH_TOKEN = "oauth_token";

    public static final String GET_METHOD = "get";

    public static final String POST_METHOD = "post";

    public String accessTokenURLMethod = GET_METHOD;

    public Map<String, Object> authURLParams = new HashMap<String, Object>();

    public Map<String, Object> tokenURLParams = new HashMap<String, Object>();

    public ExtOAuth2(String authorizationURL, String accessTokenURL, String clientid, String secret) {
    	
    	super(authorizationURL, accessTokenURL, clientid, secret);
       
    }

    public void addAuthorizationURLExtraParam(String param, Object value) {
        this.authURLParams.put(param, value);
    }

    public void addTokenURLExtraParam(String param, Object value) {
        this.tokenURLParams.put(param, value);
    }

    /**
     * First step of the OAuth2 process: redirects the user to the authorization page
     *
     * @param callbackURL
     */
    public void retrieveVerificationCode(String callbackURL) {
        authURLParams.put("client_id", clientid);
          authURLParams.put("redirect_uri", callbackURL);
        String url = buildURL(authorizationURL, authURLParams);
        throw new Redirect(url);
    }

    public Response retrieveAccessToken(String callbackURL) {
        String accessCode = Scope.Params.current().get("code");
        tokenURLParams.put("client_id", clientid);
        tokenURLParams.put("client_secret", secret);
        tokenURLParams.put("redirect_uri", "postmessage");
        tokenURLParams.put("code", accessCode);
        tokenURLParams.put ("scope", "https://www.googleapis.com/auth/plus.login");
        tokenURLParams.put("grant_type", "authorization_code");
        accessTokenURL="https://accounts.google.com/o/oauth2/token";
      
        WS.HttpResponse response = POST_METHOD.equalsIgnoreCase(accessTokenURLMethod) ?
                WS.url(accessTokenURL).params(tokenURLParams).post() :
                WS.url(accessTokenURL).params(tokenURLParams).get();
                System.out.println(response.getStream().toString());
        return new Response(response);
    }

    private String buildURL(String url, Map<String, Object> params) {
       // try {
    
    	url="https://accounts.google.com/o/oauth2/auth";
            StringBuilder sb = new StringBuilder(url);
           
            
          
            if (params != null && !params.isEmpty()) {
                boolean first = true;
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                	
                    sb.append(first ? "?" : "&");
                   // System.out.println("\n\n\n\n\nn\\n\n\n\n\n\\nvalue:"+String.valueOf(entry.getValue()));
                    if(String.valueOf(entry.getKey()).equals("client_id"))
                    {
                    	                      // System.out.println("\n\n\n\n\nn\\n\n\n\n\n\\nvalue:"+String.valueOf(entry.getValue()));
                         sb.append(entry.getKey()).append("=")
                         .append("201851680458-sumhf8v1qua2s1bn33dk9o8q34b0kvav.apps.googleusercontent.com");
                         continue;
                    }
                    sb.append(entry.getKey()).append("=")
                    .append(String.valueOf(entry.getValue()));
                           // .append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
                    // .append(URLEncoder.encode(String.valueOf(entry.getValue())));
                    first = false;
                   
                }
                sb.append(first ? "?" : "&");
                sb.append("scope").append("=")
                .append("https://www.googleapis.com/auth/plus.login");
            }
         //   System.out.println(sb);
            return sb.toString();
       // } catch (Exception e) {
         //   throw new UnexpectedException(e);
        	
       // }
    }

    /**
     * Extract the access_token from the given Response (which should be a Json response).
     * @param response
     * @return
     */
    public String extractAccessToken(Response response) {
        if (response.error != null) {
        	System.out.println("it is OK");
            if (response.error.type == OAuth2.Error.Type.UNKNOWN) {
                // the OAuth2 class is expecting the access token in the query string.
                // this is not what the OAuth2 spec says.  Facebook works, but Foursquare fails for example.
                // So check if the token is there before throwing the exception.
                JsonElement asJson = response.httpResponse.getJson();
               //as

                if (asJson != null) {
                    JsonObject body = asJson.getAsJsonObject();
                    
                    if (body != null) {
                        // this is what many libraries expect (probably because Facebook returns it)
                        JsonElement token = body.get(ACCESS_TOKEN);
                        //System.out.println(body.get("email").getAsString());
                        if (token != null) {
                        	//System.out.println("notnull"+token.getAsString());
                            return token.getAsString();
                        } else {
                            // this is what should be returned as defined in the OAuth2 spec
                            token = body.get(OAUTH_TOKEN);
                            if (token != null) {
                            	//token.getAsString();
                                return token.getAsString();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    public String getClientid() {
        return this.clientid;
    }

    public String getAuthorizationURL() {
        return this.authorizationURL;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getAccessTokenURL() {
        return this.accessTokenURL;
    }
}
