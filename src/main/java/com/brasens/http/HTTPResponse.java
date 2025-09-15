package com.brasens.http;

import com.brasens.http.objects.HttpStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class HTTPResponse {
    private HttpStatusCode code;
    private String content;
}
