package com.team01.realestate.controller.business;

import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.TourRequestStatus;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.ReportResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.service.business.AdvertService;
import com.team01.realestate.service.business.ReportService;
import com.team01.realestate.service.business.TourRequestService;
import com.team01.realestate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final AdvertService advertService;
    private final TourRequestService tourRequestService;
    private final UserService userService;
    //G01
    // get reports
    //http://localhost:8080/report + GET
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<ReportResponse> getStatistics(){
        return reportService.getStatistic();
    }

    //G03
    //http://localhost:8080/report/most-popular-properties?amount=10
    @GetMapping("/most-popular-properties")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<List<AdvertResponse>> getPopularAdverts(
            @RequestParam(value = "amount", required = true) Integer amount){
        return advertService.getPopularAdvertsForAdmin(amount);
    }

    //G02
//http://localhost:8080/report/adverts?date1=2023-02-01&date2=2023-02-28&category=1&type=2&status=PENDING
    @GetMapping("/adverts")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<List<AdvertResponse>> getAdvertsReports(
            @RequestParam(value = "date1") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
            @RequestParam(value = "date2") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2,
            @RequestParam(value = "category") Long categoryId,
            @RequestParam(value = "type") Long advertTypeId,
            @RequestParam(value = "status") AdvertStatus status
    ){
        return advertService.getAdvertsReports(
                date1.atStartOfDay(), date2.atTime(23, 59, 59), categoryId, advertTypeId, status
        );
    }


    //G04
    //http://localhost:8080/report/users?role=manager
    @GetMapping("/users/{role}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<Set<UserResponse>> getReportUsers(@PathVariable String role) {
        try {
            Set<UserResponse> foundUsers = reportService.getUsersWithRole(role);

            return ResponseMessage.<Set<UserResponse>>builder()
                    .object(foundUsers)
                    .message(SuccessMessages.USER_FOUND)
                    .httpStatus(HttpStatus.OK)
                    .build();

        } catch (IllegalArgumentException e) {
            return ResponseMessage.<Set<UserResponse>>builder()
                    .message(e.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }


    //G05
//http://localhost:8080/report/tour-requests?date1=2023-02-01&date2=2023-02-28&status=pending
    @GetMapping("/tour-requests")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<List<TourRequestResponse>> getTourRequestReports(
            @RequestParam(value = "date1") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
            @RequestParam(value = "date2") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2,
            @RequestParam(value = "status") TourRequestStatus status
    ){
        return tourRequestService.getTourRequestReports(
                date1, date2, status
        );
    }

}
