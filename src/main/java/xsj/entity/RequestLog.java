package xsj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Data
@TableName("t_log")
public class RequestLog {
    private Integer id;
    private String requestPromoter;
    private String requestUrl;
    @Past(message = "日期不能晚于今天")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime requestDate;
    private Long requestDuration;
}
