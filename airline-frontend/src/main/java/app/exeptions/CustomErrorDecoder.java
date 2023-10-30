package app.exeptions;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorDecoder errorDecoder = new Default();
        switch (response.status()) {
            case 400:
                return new FeignException.BadRequest(response.status() + " Bad Request", response.request(), null, response.headers());
            case 404:
                return new FeignException.NotFound(response.status() + " Not Found", response.request(), null, response.headers());
            case 405:
                return new FeignException.MethodNotAllowed(response.status() + " Method Not Allowed", response.request(), null, response.headers());
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}
