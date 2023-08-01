package xsj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.format.annotation.DateTimeFormat;
import xsj.utils.VeDate;

import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("t_user")
public class User implements Serializable {
    private int id;
    private String userName;
    private String password;
    private String mobilePhone;
    private int status;
    private String realName;
    @Past(message = "日期不能晚于今天")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;
    @Past(message = "日期不能晚于今天")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDate;
    @Past(message = "日期不能晚于今天")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastLoginDate;


}

