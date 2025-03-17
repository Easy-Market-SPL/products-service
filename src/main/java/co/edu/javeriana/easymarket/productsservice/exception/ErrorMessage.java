package co.edu.javeriana.easymarket.productsservice.exception;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private String message;
    private Instant timestamp;
    private Integer status;
    private String reason;
}
