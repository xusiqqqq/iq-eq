package xsj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xsj.entity.RequestLog;
import xsj.mapper.RequestLogMapper;
import xsj.service.RequestLogService;

@Service
public class RequestLogServiceImpl extends ServiceImpl<RequestLogMapper, RequestLog> implements RequestLogService {
}
