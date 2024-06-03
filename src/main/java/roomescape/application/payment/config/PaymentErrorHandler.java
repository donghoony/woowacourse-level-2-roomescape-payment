package roomescape.application.payment.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import roomescape.infrastructure.payment.TossErrorResponse;
import roomescape.exception.payment.PaymentException;

public class PaymentErrorHandler implements ResponseErrorHandler {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        TossErrorResponse errorResponse = mapper.readValue(response.getBody(), TossErrorResponse.class);
        throw new PaymentException(errorResponse.message());
    }
}
