package com.myproject.community.api.auth.service.auth;

import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.auth.dto.MemberLoginDto;
import com.myproject.community.api.auth.dto.PasswordRequestDto;
import com.myproject.community.api.auth.jwt.TokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    TokenInfo authenticate(MemberLoginDto memberLoginDto, HttpServletResponse response);

    boolean passwordCheckByMember(PasswordRequestDto passwordRequestDto, HttpServletRequest request);

    String getMemberUsername(long memberId);

    MemberAuthDto getAuthMember(long memberId);
}
