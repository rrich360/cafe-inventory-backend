package com.backend.cafe.restImpl;

import com.backend.cafe.rest.DashboardRest;
import com.backend.cafe.service.DashboardService;
import com.backend.cafe.serviceImpl.DashboardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardRestImpl implements DashboardRest {

    @Autowired
    DashboardServiceImpl dashboardServiceImpl;


    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
                return dashboardServiceImpl.getCount();
    }
}
