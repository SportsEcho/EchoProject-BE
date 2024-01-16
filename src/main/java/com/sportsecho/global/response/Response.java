package com.sportsecho.global.response;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class Response<T> {
    private String statusCode;
    private T result;

    public static <T> Response<Void> error(String statusCode){
        return new Response<>(statusCode, null);
    }
    public static <T> Response<T> error(String statusCode, T result){
        return new Response<>(statusCode, result);
    }
    //요청은 성공했지만 리턴해줄 값이 없는 경우
    public static Response<Void> success() {
        return new Response<Void>("SUCCESS", null);
    }

    //요청에 성공했고 결과값도 리턴해주는 경우
    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }
}
