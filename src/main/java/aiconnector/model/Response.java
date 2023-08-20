package aiconnector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Response {
    String message;
    Map<String,Object> result;
}
