package com.myproject.community.domain.category;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(unique = true)
    private int displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();


    @Builder
    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }


    private void addChild(Category category) {
        children.add(category);
        category.parent = this;
    }

    public void updateDisplayOrder(int newDisplayOrder) {
        this.displayOrder = newDisplayOrder;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
