package com.rmnnorbert.dentocrates.controller.clinic;

import com.rmnnorbert.dentocrates.dto.leave.LeaveDTO;
import com.rmnnorbert.dentocrates.dto.leave.LeaveDeleteDTO;
import com.rmnnorbert.dentocrates.dto.leave.LeaveRegisterDTO;
import com.rmnnorbert.dentocrates.service.clinic.leave.LeaveService;
import com.rmnnorbert.dentocrates.service.client.communicationServices.AppointmentNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.rmnnorbert.dentocrates.controller.ApiResponseConstants.*;
@Tag(name = "Leave", description = "Leave management APIs")
@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;
    private final AppointmentNotificationService notificationService;
    @Autowired
    public LeaveController(LeaveService leaveService, AppointmentNotificationService notificationService) {
        this.leaveService = leaveService;
        this.notificationService = notificationService;
    }

    @Operation(
            summary = "Return all a clinic's all leave as a list.",
            description = "Return a clinic's all leave as a list by id. The response is a list of LeaveDTO.",
            tags = { "Leave", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Return the leave list of the clinic.",
                    content = { @Content( array = @ArraySchema( schema = @Schema(implementation = LeaveDTO.class)), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/{id}")
    public List<LeaveDTO> getLeavesFromDateOfClinic(@PathVariable long id) {
        return leaveService.getAllLeavesFromDateOfClinic(id);
    }

    @Operation(
            summary = "Register a leave to a clinic.",
            description = "Register a leave to a clinic by providing a LeaveRegisterDTO. The response is a String.",
            tags = { "Leave", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Register a leave to a clinic.",
                    content = { @Content(schema = @Schema(implementation = LeaveRegisterDTO.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Clinic" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Clinic" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/")
    public ResponseEntity<String> registerLeaveToClinic(@Valid @RequestBody LeaveRegisterDTO dto) {
        ResponseEntity<String> registeredLeave = leaveService.registerLeave(dto);
        notificationService.sendLeaveNotifications(dto);
        return registeredLeave;
    }

    @Operation(
            summary = "Delete a leave of a clinic.",
            description = "Delete a leave of a clinic by providing a LeaveDeleteDTO. The response is a String.",
            tags = { "Leave", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Delete a leave of a clinic.",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Leave" + DELETE_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Clinic" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Clinic" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = BAD_REQUEST_RESPONSE_CODE, description = "Invalid delete request",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Invalid delete request."), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @DeleteMapping("/")
    public ResponseEntity<String> deleteLeaveOfClinic(@Valid @RequestBody LeaveDeleteDTO dto) {
        return leaveService.deleteLeave(dto);
    }
}
