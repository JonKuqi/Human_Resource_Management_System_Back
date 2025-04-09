package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Position;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/positions")
@AllArgsConstructor
public class PositionController extends BaseController<Position, Integer> {
    private final PositionService svc;

    @Override
    protected PositionService getService() {
        return svc;
    }
}

