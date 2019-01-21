package com.girgin.ramazan.springdataelasticsearchgeosearch.domain.repository;

import com.girgin.ramazan.springdataelasticsearchgeosearch.domain.model.entity.CategoryBranchLocationDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

public interface CategoryBranchLocationDocumentRepository extends ElasticsearchRepository<CategoryBranchLocationDocument, String> {
    Page<CategoryBranchLocationDocument> findByCategoryAndBranchLocationWithin(String category, Point branchLocation, Distance distance, Pageable pageable);
}
