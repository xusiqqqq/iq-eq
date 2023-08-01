package xsj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xsj.entity.Book;
import xsj.mapper.BookMapper;
import xsj.service.BookService;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService  {
}
