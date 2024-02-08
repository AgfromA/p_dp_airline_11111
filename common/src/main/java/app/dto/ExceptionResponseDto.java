package app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ExceptionResponseDto {

    String message;
    String traceId;
}
