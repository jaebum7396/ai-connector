package gptconnector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class KarloResponse {
    String id;
    String model_version;
    List<KarloImage> images;
}
