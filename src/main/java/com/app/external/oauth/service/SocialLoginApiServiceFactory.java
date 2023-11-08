package com.app.external.oauth.service;

import com.app.domain.member.constant.MemberType;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SocialLoginApiServiceFactory {

  private static Map<String, SocialLoginApiService> socialLoginApiServices;

  public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServices) {
    this.socialLoginApiServices = socialLoginApiServices;
  }

  public static SocialLoginApiService getSocialLoginApiService(MemberType memberType) {
    String socialLoginApiServiceBeanName = "";

    if (MemberType.KAKAO.equals(memberType)) {
      socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";
    }
    return socialLoginApiServices.get(socialLoginApiServiceBeanName);
  }
}
