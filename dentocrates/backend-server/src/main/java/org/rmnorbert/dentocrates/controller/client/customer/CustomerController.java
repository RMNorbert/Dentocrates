package org.rmnorbert.dentocrates.controller.client.customer;


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
import org.rmnorbert.dentocrates.dto.client.customer.CustomerAppointmentResponseDTO;
import org.rmnorbert.dentocrates.dto.client.customer.CustomerResponseDTO;
import org.rmnorbert.dentocrates.service.client.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.rmnorbert.dentocrates.controller.ApiResponseConstants.*;

@Tag(name = "Customer", description = "Customer management APIs")
@RestController
@RequestMapping("/client")
public class CustomerController {
    private final CustomerService customerService;
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @Operation(
            summary = "Return all customer as a list",
            description = "Return the customer list. The response is a CustomerAppointmentResponseDTO list.",
            tags = { "Customer", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Send the list of all customer.",
                    content = { @Content( array = @ArraySchema( schema = @Schema(implementation = CustomerAppointmentResponseDTO.class)), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/all")
    public List<CustomerAppointmentResponseDTO> getAllCustomer(){
        return customerService.getAllCustomer();
    }
    @Operation(
            summary = "Return a customer by id",
            description = "Return a customer by the provided id. The response is a CustomerResponseDTO.",
            tags = { "Customer", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Successfully Found and returned the searched customer.",
                    content = { @Content(schema = @Schema(implementation = CustomerResponseDTO.class), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Customer not found by the provided id" + NOT_FOUND_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Customer" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomer(@PathVariable Long id){
        return customerService.getCustomerResponse(id);
    }
    @Operation(
            summary = "Delete a customer by id",
            description = "Delete a customer by the provided id. The response is a String.",
            tags = { "Customer", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = OK_RESPONSE_CODE, description = "Customer" + DELETE_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Customer" + DELETE_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = UNAUTHORIZED_RESPONSE_CODE, description = INVALID_CREDENTIALS_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = INVALID_CREDENTIALS_EXCEPTION_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE_CODE, description = "Customer not found by the provided id" + NOT_FOUND_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Customer" + NOT_FOUND_RESPONSE_CONTENT), mediaType = RESPONSE_MEDIA_TYPE) }),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_RESPONSE_CODE, description = INTERNAL_SERVER_ERROR_RESPONSE_CONTENT,
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = RESPONSE_MEDIA_TYPE) })})
    @DeleteMapping("/")
    public ResponseEntity<String> deleteCustomer(@Valid @RequestBody DeleteDTO dto){
        return customerService.deleteCustomerById(dto);
    }
}
