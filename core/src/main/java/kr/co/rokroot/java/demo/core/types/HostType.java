package kr.co.rokroot.java.demo.core.types;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HostType {

    NAVER("naver", "https://www.naver.com"),
    KAKAO("kakao", "https://www.kakao.com");

    private final String type;
    private final String url;
}
