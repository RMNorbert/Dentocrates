package com.rmnnorbert.dentocrates.controller.clinic;

import com.rmnnorbert.dentocrates.dto.clinic.ClinicRegisterDTO;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicResponseDTO;
import com.rmnnorbert.dentocrates.dto.clinic.ClinicUpdateDTO;
import com.rmnnorbert.dentocrates.service.clinic.ClinicService;
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
@Tag(name = "Clinic", description = "Clinic management APIs")
@RestController
@RequestMapping("/clinic")
public class ClinicController {
    private final ClinicService clinicService;
    @Autowired
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @Operation(
            summary = "Return all clinic as a list",
            description = "Return all clinic as a list. The response is a list of ClinicResponseDTO.",
            tags = { "Clinic", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Return of the clinic list.",
                    content = { @Content( array = @ArraySchema( schema = @Schema(implementation = ClinicResponseDTO.class)), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/all")
    public List<ClinicResponseDTO> getAllClinic(){
        return clinicService.getAllClinic();
    }

    @Operation(
            summary = "Return all clinic related to the dentist as a list",
            description = "Return all clinic related to the dentist by the dentist id as a list. The response is a list of ClinicResponseDTO.",
            tags = { "Clinic", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Return of the clinic list related to the dentist.",
                    content = { @Content( array = @ArraySchema( schema = @Schema(implementation = ClinicResponseDTO.class)), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/dentist/{id}")
    public List<ClinicResponseDTO> getAllClinicByDentist(@PathVariable long id){
        return clinicService.getAllClinicByDentist(id);
    }

    @Operation(
            summary = "Return the clinic by the provided id",
            description = "Return the clinic by the provided id. The response is a ClinicResponseDTO.",
            tags = { "Clinic", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Return of the clinic by the provided id.",
                    content = { @Content( schema = @Schema(implementation = ClinicResponseDTO.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Clinic" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Clinic" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/{id}")
    public ClinicResponseDTO getClinicById(@PathVariable Long id){
        return clinicService.getClinicResponseDTOById(id);
    }

    @Operation(
            summary = "Register a clinic",
            description = "Register a clinic by providing properties according to ClinicRegisterDTO. The response is a String.",
            tags = { "Clinic", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successfully register a clinic.",
                    content = { @Content( schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Clinic" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "DENTIST" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "DENTIST" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = BAD_REQUEST_RESPONSE_CODE, description = "Location" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Location" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/register")
    public ResponseEntity<String> registerClinic(@Valid @RequestBody ClinicRegisterDTO clinicRegisterDTO){
        return clinicService.registerClinic(clinicRegisterDTO);
    }

    @Operation(
            summary = "Delete the clinic by the provided id",
            description = "Delete the clinic by the provided id. The response is a String.",
            tags = { "Clinic", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Delete the clinic by the provided id.",
                    content = { @Content( schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Clinic" + DELETE_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Clinic" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Clinic" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClinic(@PathVariable Long id){
        return clinicService.deleteClinicById(id);
    }

    @Operation(
            summary = "Update a clinic",
            description = "Update a clinic by providing properties according to ClinicUpdateDTO. The response is a String.",
            tags = { "Clinic", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successfully updated a clinic.",
                    content = { @Content( schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Clinic" + SUCCESSFUL_UPDATE_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "CLINIC" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "CLINIC" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = BAD_REQUEST_RESPONSE_CODE, description = "Location" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Location" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateClinicById(@PathVariable Long id,
            @Valid @RequestBody ClinicUpdateDTO dto){
        return clinicService.updateClinicById(id,dto);
    }

}
