package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.LeaveRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant/leave-request/")
@AllArgsConstructor
public class LeaveRequestController extends BaseController<LeaveRequest, Integer> {
    private final LeaveRequestService svc;
    @Override
    protected BaseService<LeaveRequest, Integer> getService() {
        return svc;
    }
   // @Override
    //protected LeaveRequestService getServiceSpecific() {
      //  return svc;
    //}

//    @PutMapping("/{id}/approve")
//    //@PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
//    public ResponseEntity<String> approve(@PathVariable Integer id) {
//        svc.approveLeaveRequest(id);
//        return ResponseEntity.ok("Leave request approved successfully.");
//    }
//
//    @PutMapping("/{id}/reject")
//   // @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
//    public ResponseEntity<String> reject(@PathVariable Integer id) {
//        svc.rejectLeaveRequest(id);
//        return ResponseEntity.ok("Leave request rejected successfully.");
//    }



}
