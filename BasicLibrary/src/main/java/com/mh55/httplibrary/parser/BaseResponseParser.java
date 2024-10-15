package com.mh55.httplibrary.parser;

import com.mh55.httplibrary.common.ApiList;
import com.mh55.httplibrary.common.ApiResult;
import com.mh55.httplibrary.common.HttpConstants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import rxhttp.wrapper.annotation.Parser;
import rxhttp.wrapper.parse.TypeParser;
import rxhttp.wrapper.utils.Converter;
import rxhttp.wrapper.utils.GsonUtil;

/**
 * 输入T,输出T,并对code统一判断
 */
@Parser(name = "ApiResult", wrappers = {ApiList.class})
public class BaseResponseParser<T> extends TypeParser<T> {

    /**
     * 此构造方法适用于任意Class对象，但更多用于带泛型的Class对象，如：List<Student>
     * <p>
     * 用法:
     * Java: .asParser(new ResponseParser<List<Student>>(){})
     * Kotlin: .asParser(object : ResponseParser<List<Student>>() {})
     * <p>
     * 注：此构造方法一定要用protected关键字修饰，否则调用此构造方法将拿不到泛型类型
     */
    protected BaseResponseParser() {
        super();
    }

    /**
     * 此构造方法仅适用于不带泛型的Class对象，如: Student.class
     * <p>
     * 用法
     * Java: .asParser(new ResponseParser<>(Student.class))   或者  .asResponse(Student.class)
     * Kotlin: .asParser(ResponseParser(Student::class.java)) 或者  .asResponse(Student::class.java)
     */
    public BaseResponseParser(Type type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T onParse(@NotNull okhttp3.Response response) throws IOException {
        ApiResult<String> data = Converter.convertTo(response, ApiResult.class, String.class);
        T t = null;
        if (data.getCode() == HttpConstants.SUCCESS_CODE) {
            t = GsonUtil.getObject(data.getData(), types[0]);
        }
        if (t == null && types[0] == String.class) {
            t = (T) (data.getData() == null ? "" : data.getData());
        }
        if (data.getCode() != HttpConstants.SUCCESS_CODE) {
            MyParseException exception = new MyParseException(data.getCode() + "", data.getMsg(), response, t == null ? "" : t.toString());
            throw exception;
        }
        return t;
    }
}
