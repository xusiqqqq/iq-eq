package xsj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("t_book")
public class Book implements Serializable {
    private Integer id;

    @NotEmpty(message = "{name.error.notnull}")
    @Size(max = 25, message = "{name.error.max}")
    private String name;
//^[0-9]+(\.[0-9]+)?$
    @Min(value=0, message = "价格必需大于0")
    private Float price;

    @NotEmpty(message = "{author.error.notnull}")
    private String author;

    @NotEmpty(message = "{publisher.error.notnull}")
    private String publisher;

    @Past(message = "出版日期不能晚于今天")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    @NotEmpty(message = "{coverUrl.error.notnull}")
    private String coverUrl;
}
