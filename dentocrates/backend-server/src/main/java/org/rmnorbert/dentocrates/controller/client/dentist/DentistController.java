package org.rmnorbert.dentocrates.controller.client.dentist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.rmnorbert.dentocrates.dto.DeleteDTO;
import org.rmnorbert.dentocrates.dto.client.dentist.DentistResponseDTO;
import org.rmnorbert.dentocrates.service.client.dentist.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.*;


@Tag(name = "Dentist", description = "Dentist management APIs")
@RestController
@RequestMapping("/dentist")
public class DentistController {
    private final DentistService dentistService;
    @Autowired
    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @Operation(
            summary = "Return all dentist as a list",
            description = "Return all dentist as a list. The response is a list of DentistResponseDTO.",
            tags = { "Dentist", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Return the dentist list.",
                    content = { @Content( array = @ArraySchema( schema = @Schema(implementation = DentistResponseDTO.class)), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/all")
    public List<DentistResponseDTO> getAllDentist(){
        return dentistService.getAllDentist();
    }

    @Operation(
            summary = "Return a dentist by id",
            description = "Return a dentist by the provided id. The response is a DentistResponseDTO.",
            tags = { "Dentist", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successfully Found and returned the searched dentist.",
                    content = { @Content(schema = @Schema(implementation = DentistResponseDTO.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Dentist" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Dentist" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/{id}")
    public DentistResponseDTO getDentistById(@PathVariable Long id){
        return dentistService.getDentistById(id);
    }
    @Operation(
            summary = "Delete a dentist by id",
            description = "Delete a dentist by the provided id. The response is a String.",
            tags = { "Dentist", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Dentist" + DELETE_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = DentistResponseDTO.class),
                            examples = @ExampleObject(value = "Dentist" + DELETE_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Dentist" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Dentist" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @DeleteMapping("/")
    public ResponseEntity<String> deleteDentist(@Valid @RequestBody DeleteDTO dto){
        return dentistService.deleteDentistById(dto);
    }
}
