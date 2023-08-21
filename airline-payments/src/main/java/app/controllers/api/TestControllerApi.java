package app.controllers.api;

import app.dto.TestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Api(tags = "Test REST")
@Tag(name = "Test REST", description = "API для операций с пользователем")
@RequestMapping("/api/Tests")
public interface TestControllerApi {
    @GetMapping
    @ApiOperation(value = "Get list of all Tests")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tests found"),
            @ApiResponse(code = 204, message = "Tests not found")})
    ResponseEntity<Page> getAllTestsPages(@PageableDefault(sort = {"id"})
                                             @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(10) Integer size);

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Test by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Test found"),
            @ApiResponse(code = 404, message = "Test not found")})
    ResponseEntity<TestDTO> getTestDTOById(
            @ApiParam(
                    name = "id",
                    value = "Test.id"
            )
            @PathVariable Long id);

    @PostMapping
    @ApiOperation(value = "Create Test")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Test created")})
    ResponseEntity<TestDTO> createTestDTO(
            @ApiParam(
                    name = "Test",
                    value = "Test model"
            )
            @RequestBody @Valid TestDTO testDTO);

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update Test by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Test updated"),
            @ApiResponse(code = 404, message = "Test not found")})
    ResponseEntity<TestDTO> updateTestDTOById(
            @ApiParam(
                    name = "id",
                    value = "Test.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "TestDTO",
                    value = "Test model"
            )
            @RequestBody TestDTO testDTO);
}
