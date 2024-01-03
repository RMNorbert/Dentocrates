package com.rmnnorbert.dentocrates.controller.clinic;

import com.rmnnorbert.dentocrates.dto.location.LocationDTO;
import com.rmnnorbert.dentocrates.service.clinic.location.LocationService;
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

@Tag(name = "Location", description = "Location management APIs")
@RestController
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(
            summary = "Return all location as a list.",
            description = "Return all location as a list. The response is a list of LocationDTO.",
            tags = { "Location", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Return all of the location as list.",
                    content = { @Content( array = @ArraySchema( schema = @Schema(implementation = LocationDTO.class)), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/all")
    public List<LocationDTO> getAllLocation(){
        return locationService.getAllLocation();
    }

    @Operation(
            summary = "Register a location.",
            description = "Register a location by providing a LocationDTO. The response is a String.",
            tags = { "Location", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Register a location.",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Location" + SUCCESSFUL_REGISTER_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = DATA_INTEGRATION_RESPONSE_CODE, description = DATA_INTEGRATION_RESPONSE_DESCRIPTION,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = DATA_INTEGRATION_RESPONSE_DESCRIPTION), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @PostMapping("/register")
    public ResponseEntity<String> registerLocation(@Valid @RequestBody LocationDTO locationDTO){
        return locationService.registerLocation(locationDTO);
    }

    @Operation(
            summary = "Delete a location by id.",
            description = "Delete a location by id. The response is a String.",
            tags = { "Location", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Location" + DELETE_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Location" + DELETE_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Location" + NOT_FOUND_RESPONSE_CONTENT + " by the provided id",
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Location" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id){
        return locationService.deleteLocationById(id);
    }
}
