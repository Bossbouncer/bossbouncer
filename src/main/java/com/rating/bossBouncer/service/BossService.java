package com.rating.bossbouncer.service;

import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.repository.BossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BossService {

    @Autowired
    private BossRepository bossRepository;

    public List<Boss> typeAheadSearch(String organization, String query, String field) {
        Specification<Boss> spec = (root, query1, cb) ->
                cb.equal(cb.lower(root.get("organization")), organization.toLowerCase());

        switch (field) {
            case "firstName":
                spec = spec.and((root, query1, cb) -> cb.like(cb.lower(root.get("firstName")), "%" + query.toLowerCase() + "%"));
                break;
            case "lastName":
                spec = spec.and((root, query1, cb) -> cb.like(cb.lower(root.get("lastName")), "%" + query.toLowerCase() + "%"));
                break;
            case "email":
                spec = spec.and((root, query1, cb) -> cb.like(cb.lower(root.get("email")), "%" + query.toLowerCase() + "%"));
                break;
            case "title":
                spec = spec.and((root, query1, cb) -> cb.like(cb.lower(root.get("title")), "%" + query.toLowerCase() + "%"));
                break;
            default:
                throw new IllegalArgumentException("Invalid field for search");
        }

        return bossRepository.findAll(spec);
    }
}
