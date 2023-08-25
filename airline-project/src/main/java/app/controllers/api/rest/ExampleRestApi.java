package app.controllers.api.rest;

import app.dto.ExampleDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Example REST")
@Tag(name = "Example REST", description = "API example description")
public interface ExampleRestApi {

    @GetMapping("/api/example/")
    @ApiOperation(value = "Get Page of Examples")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Examples Page found"),
            @ApiResponse(code = 204, message = "Examples Page not present")}
    )
    ResponseEntity<List<ExampleDto>> getPage(@ApiParam(name = "page") @RequestParam(required = false) Integer page,
                                             @ApiParam(name = "size") @RequestParam(required = false) Integer size);

    @GetMapping("/api/example/{id}")
    @ApiOperation(value = "Get Example by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Example found"),
            @ApiResponse(code = 404, message = "Example not found")}
    )
    ResponseEntity<ExampleDto> get(@ApiParam(name = "id", value = "Example.id") @PathVariable Long id);

    @PostMapping("/api/example/")
    @ApiOperation(value = "Create Example")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Example created"),
            @ApiResponse(code = 400, message = "Example not created")}
    )
    ResponseEntity<ExampleDto> create(@ApiParam(name = "example", value = "ExampleDto") @Valid @RequestBody ExampleDto exampleDto);

    @PatchMapping("/api/example/{id}")
    @ApiOperation(value = "Update Example")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Example updated"),
            @ApiResponse(code = 400, message = "Example not updated")}
    )
    ResponseEntity<ExampleDto> update(@ApiParam(name = "id", value = "Example.id") @PathVariable Long id,
                                      @ApiParam(name = "example", value = "ExampleDto") @Valid @RequestBody ExampleDto exampleDto);

    @DeleteMapping("/api/example/{id}")
    @ApiOperation(value = "Delete Example by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Example deleted"),
            @ApiResponse(code = 404, message = "Example not found")}
    )
    ResponseEntity<Void> delete(@ApiParam(name = "id", value = "Example.id") @PathVariable Long id);
}