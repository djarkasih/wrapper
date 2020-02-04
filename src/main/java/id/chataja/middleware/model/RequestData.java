/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.middleware.model;

import java.util.Map;
import org.springframework.http.HttpMethod;

/**
 *
 * @author ahmad
 */
public class RequestData {
    
    private final HttpMethod method;
    private final String requestURI;
    private final Map<String,String> headers;
    private final Map<String,String[]> queries;
    private Map<String,Object> body = null;

    @Override
    public String toString() {
        return "RequestData{" + "method=" + method + ", requestURI=" + requestURI + ", headers=" + headers + ", queries=" + queries + ", body=" + body + '}';
    }

    public RequestData(HttpMethod method, String requestURI, Map<String, String> headers, Map<String, String[]> queries) {
        this.method = method;
        this.requestURI = requestURI;
        this.headers = headers;
        this.queries = queries;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String[]> getQueries() {
        return queries;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
    
}
