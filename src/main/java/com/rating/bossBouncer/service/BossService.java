package com.rating.bossbouncer.service;

import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.repository.BossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BossService {

    @Autowired
    private BossRepository bossRepository;

    public List<Boss> typeAheadSearch(String query, String field) {
        if (query == null || query.isBlank()) {
            return List.of(); // ✅ return empty list if query is empty
        }

        String normalizedQuery = query.trim().toLowerCase();
        String searchTerm = "%" + normalizedQuery + "%";

        Specification<Boss> spec = Specification.where(null); // start with empty spec

        // ✅ Allowed searchable fields
        Set<String> allowedFields = Set.of("firstName", "lastName", "email", "title");

        if (field != null && !field.isBlank() && allowedFields.contains(field)) {
            // ✅ Search only in the specific field
            spec = spec.and((root, query1, cb) -> cb.like(cb.lower(root.get(field)), searchTerm));
        } else {
            // ✅ Search across all allowed fields (OR condition)
            spec = spec.and((root, query1, cb) -> cb.or(
                    cb.like(cb.lower(root.get("firstName")), searchTerm),
                    cb.like(cb.lower(root.get("lastName")), searchTerm),
                    cb.like(cb.lower(root.get("email")), searchTerm),
                    cb.like(cb.lower(root.get("title")), searchTerm)
            ));
        }

        return bossRepository.findAll(spec);
    }

}
