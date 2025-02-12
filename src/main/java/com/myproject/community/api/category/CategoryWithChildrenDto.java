package com.myproject.community.api.category;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithChildrenDto {

    private CategoryDto parentCategory;

    private List<CategoryDto> children;
}
