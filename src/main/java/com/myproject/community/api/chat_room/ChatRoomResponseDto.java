package com.myproject.community.api.chat_room;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private long id;
    private String title;
    private int capacity;
    private int memberCount;

    @Builder
    @QueryProjection
    public ChatRoomResponseDto(long id, String title, int capacity, int memberCount) {
        this.id = id;
        this.title = title;
        this.capacity = capacity;
        this.memberCount = memberCount;
    }

    public void memberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public void updateMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }


}
