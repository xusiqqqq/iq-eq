package xsj.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xsj.common.R;
import xsj.entity.Book;
import xsj.entity.User;
import xsj.service.BookService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    //获取所有图书信息
    @RequestMapping("/toList")
    public String toList(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("LOGIN_USER");
        System.out.println(user.toString());
        List<Book> bookList = this.bookService.list();
        model.addAttribute("BOOK_LIST",bookList);
        return "book/list";
    }

    //添加图书
    @ResponseBody
    @RequestMapping(value = "/add.do",method = RequestMethod.POST,produces = "application/json")
    public R addBook(@Valid Book book, BindingResult bindingResult,Model model, MultipartFile coverUrlFile, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            //说明验证没有通过，则把错误信息绑定前端的页面
            final List<FieldError> fieldErrors=bindingResult.getFieldErrors();
            for(FieldError fe : fieldErrors){
                System.out.println(fe.getField()+"=>"+fe.getDefaultMessage());
            }
            return R.validError(fieldErrors);
        }else {
            System.out.println(book.toString());
            saveBookImg(book, coverUrlFile, request);
            boolean b = this.bookService.save(book);
            if(!b) return R.error("图书添加失败");
            return R.success(book);
        }
    }


    //预更新图书
    @ResponseBody
    @RequestMapping(value = "/toUpdate.do",method = RequestMethod.GET,produces = "application/json")
    public Book toUpdate(int id){
        LambdaQueryWrapper<Book> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getId, id);
        Book book = this.bookService.getOne(queryWrapper);
        return book;
    }

    //更新图书
    @ResponseBody
    @RequestMapping(value = "update.do",method =RequestMethod.POST,produces = "application/json")
    public R<Book> updateBook(Book book,MultipartFile coverUrlFile,HttpServletRequest request) {
        saveBookImg(book,coverUrlFile,request);
        boolean b = this.bookService.updateById(book);
        if(!b) return R.error("图书更新失败");
        return R.success(book);
    }

    //根据id删除单本图书
    @ResponseBody
    @RequestMapping(value = "/delete.do",method = RequestMethod.GET,produces = "application/json")
    public String deleteBookById(int id){
        boolean b = this.bookService.removeById(id);
        if(!b) return "图书删除失败";
        return "图书删除成功";
    }

    //批量删除
    @ResponseBody
    @RequestMapping(value = "/deleteSelect.do",method = RequestMethod.POST,produces = "application/json")
    public String deleteBookBySelect(@RequestBody List<Integer> ids){
        boolean b = this.bookService.removeByIds(ids);
        if(!b) return "批量删除失败";
        return "批量删除成功";
    }

    //查询图书
    @RequestMapping(value = "/search.do",method =RequestMethod.GET )
    public String serachBook(Model model,String search){
        LambdaQueryWrapper<Book> qw=new LambdaQueryWrapper<>();
        qw.like(Book::getName,search)
                .or().like(Book::getAuthor,search)
                .or().like(Book::getPrice,search)
                .or().like(Book::getPublisher,search)
                .or().like(Book::getPublishDate,search);
        List<Book> list = this.bookService.list(qw);
        model.addAttribute("search",search);
        model.addAttribute("BOOK_LIST",list);
        return "/book/list";
    }



    //存储图片
    private static void saveBookImg(Book book, MultipartFile coverUrlFile, HttpServletRequest request) {
        String filename = coverUrlFile.getOriginalFilename();
        String realPath= request.getServletContext().getRealPath("images");
        if(realPath!=null&&filename!=null&&filename.length()>0){
            String newFileName= UUID.randomUUID()+filename.substring(filename.lastIndexOf("."));
            File path= null;
            try {
                path = new File(ResourceUtils.getURL("classpath:").getPath());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            File upload = new File(path.getAbsolutePath(),"static/images/book_cover_img/");
            if(!upload.exists()) upload.mkdirs();
            File newFile=new File(upload+"\\"+newFileName);

            book.setCoverUrl(newFileName);
            try {
                coverUrlFile.transferTo(newFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else log.info("File Error!");
    }

}
