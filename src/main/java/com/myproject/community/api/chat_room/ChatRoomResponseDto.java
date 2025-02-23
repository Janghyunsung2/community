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
    public ChatRoomResponseDto(long id, String title, int capacity) {
        this.id = id;
        this.title = title;
        this.capacity = capacity;
    }

    public void updateMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }


}
