package com.myproject.community.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    CHAT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "올바른 메세지가 아닙니다."),
    PERIOD_BAD_REQUEST(HttpStatus.BAD_REQUEST, "올바른 Period가 아닙니다."),

    CATEGORY_NAME_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다"),
    BOARD_TITLE_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 존재하는 게시판입니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    PASSWORD_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "refresh token이 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "인증되지 않은 토큰입니다."),

    /* 403 FORBIDDEN : 권한이 없는 사용자 */
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INACTIVE_ACCOUNT(HttpStatus.FORBIDDEN, "휴면 계정입니다."),
    QUIT_ACCOUNT(HttpStatus.FORBIDDEN, "탈퇴한 사용자입니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "맴버를 찾을 수 없음"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시판을 찾을 수 없음"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없음"),

    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지 저장 실패"),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없음"),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없음"),
    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */

    /* 500 INTERNAL_SERVER_ERROR : 서버오류 */
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
