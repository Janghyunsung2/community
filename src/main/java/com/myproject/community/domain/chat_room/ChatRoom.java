package com.myproject.community.domain.chat_room;

import com.myproject.community.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member host;

    @Builder
    public ChatRoom(String title, int capacity, Member host) {
        this.title = title;
        this.capacity = capacity;
        this.host = host;
    }
}
