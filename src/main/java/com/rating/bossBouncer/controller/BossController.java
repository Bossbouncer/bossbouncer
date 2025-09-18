package com.rating.bossbouncer.controller;

import com.rating.bossbouncer.entity.Boss;
import com.rating.bossbouncer.service.BossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bosses")
public class BossController {

    @Autowired
    private BossService bossService;
    /**
     * GET /lookUp - Typeahead search for Boss entities.
     * <p>
     * Performs a case-insensitive search across firstName, lastName, email, and title.
     * If a valid field is provided, the search is restricted to that field;
     * otherwise, it searches across all fields. Leading/trailing spaces in the query
     * are ignored. Returns an empty list if no matches are found.
     * <p>
     * Optional pagination parameters (page, size) can be added for large datasets.
     *
     * @param query the search term (required)
     * @param field the optional field to search in (firstName, lastName, email, title)
     * @return a list of matching Boss objects
     */
    @GetMapping("/lookUp")
    public ResponseEntity<List<Boss>> typeAhead(
            @RequestParam String query,
            @RequestParam(required = false) String field) {

        // Early exit if query is blank
        if (query == null || query.isBlank()) {
            return ResponseEntity.ok(List.of());
        }

        List<Boss> results = bossService.typeAheadSearch(query, field);
        return ResponseEntity.ok(results);
    }

}
