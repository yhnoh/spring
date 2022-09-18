package com.example.springrestdocs.docs;

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Snippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class ApiDocumentUtils {

    public static RestDocumentationResultHandler document(Snippet... snippets){

        return MockMvcRestDocumentation.document("{class-name}/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                snippets
        );
    }

    private static OperationRequestPreprocessor getDocumentRequest(){
        return preprocessRequest(
                modifyUris()
                        .scheme("https")
                        .host("docs.api.com")
                        .removePort(),
                prettyPrint()
        );
    }

    private static OperationResponsePreprocessor getDocumentResponse(){
        return preprocessResponse(
                prettyPrint()
        );
    }

}
